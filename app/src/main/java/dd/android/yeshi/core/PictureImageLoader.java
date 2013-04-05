package dd.android.yeshi.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import com.actionbarsherlock.app.ActionBar;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.inject.Inject;
import dd.android.yeshi.R;
import roboguice.util.RoboAsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static dd.android.yeshi.core.Constants.Http.*;
import static dd.android.yeshi.core.Constants.Http.URL_BASE;
import static android.graphics.Bitmap.CompressFormat.PNG;
import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.view.View.VISIBLE;

/**
 * Avatar utilities
 */
public class PictureImageLoader {

    private static final String TAG = "PictureAvatarLoader";

    private static final float CORNER_RADIUS_IN_DIP = 3;

    private static final int CACHE_SIZE = 75;

    private static abstract class FetchAvatarTask extends
            RoboAsyncTask<BitmapDrawable> {

        private static final Executor EXECUTOR = Executors
                .newFixedThreadPool(1);

        private FetchAvatarTask(Context context) {
            super(context, EXECUTOR);
        }

        @Override
        protected void onException(Exception e) throws RuntimeException {
            Log.d(TAG, "Avatar load failed", e);
        }
    }

    private final float cornerRadius;

    private final Map<Object, BitmapDrawable> loaded = new LinkedHashMap<Object, BitmapDrawable>(
            CACHE_SIZE, 1.0F) {

        private static final long serialVersionUID = -4191624209581976720L;

        @Override
        protected boolean removeEldestEntry(
                Entry<Object, BitmapDrawable> eldest) {
            return size() >= CACHE_SIZE;
        }
    };

    private final Context context;

    private final File avatarDir;

    private final Drawable loadingAvatar;

    private final BitmapFactory.Options options;

    private String type = "icon";

    /**
     * Create avatar helper
     *
     * @param context
     */
    @Inject
    public PictureImageLoader(final Context context) {
        this.context = context;

        loadingAvatar = context.getResources().getDrawable(R.drawable.gravatar_icon);

        avatarDir = new File(context.getCacheDir(), "avatars/" + context.getPackageName());
        if (!avatarDir.isDirectory())
            avatarDir.mkdirs();

        float density = context.getResources().getDisplayMetrics().density;
        cornerRadius = CORNER_RADIUS_IN_DIP * density;

        options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = ARGB_8888;
    }

    /**
     * Get image for Picture
     *
     * @param Picture
     * @return image
     */
    protected BitmapDrawable getImage(final Picture Picture) {
        File avatarFile = new File(avatarDir, Picture._id + type);

        if (!avatarFile.exists() || avatarFile.length() == 0)
            return null;

        Bitmap bitmap = decode(avatarFile);
        if (bitmap != null)
            return new BitmapDrawable(context.getResources(), bitmap);
        else {
            avatarFile.delete();
            return null;
        }
    }

//    /**
//     * Get image for Picture
//     *
//     * @param Picture
//     * @return image
//     */
//    protected BitmapDrawable getImage(final CommitPicture Picture) {
//        File avatarFile = new File(avatarDir, Picture.getEmail());
//
//        if (!avatarFile.exists() || avatarFile.length() == 0)
//            return null;
//
//        Bitmap bitmap = decode(avatarFile);
//        if (bitmap != null)
//            return new BitmapDrawable(context.getResources(), bitmap);
//        else {
//            avatarFile.delete();
//            return null;
//        }
//    }

    /**
     * Decode file to bitmap
     *
     * @param file
     * @return bitmap
     */
    protected Bitmap decode(final File file) {
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    /**
     * Fetch avatar from URL
     *
     * @param url
     * @param pictureId
     * @return bitmap
     */
    protected BitmapDrawable fetchAvatar(final String url, final String pictureId) {
        File rawAvatar = new File(avatarDir, pictureId + "-raw");
        HttpRequest request = HttpRequest.get(url);
        if (request.ok())
            request.receive(rawAvatar);

        if (!rawAvatar.exists() || rawAvatar.length() == 0)
            return null;

        Bitmap bitmap = decode(rawAvatar);
        if (bitmap == null) {
            rawAvatar.delete();
            return null;
        }

        bitmap = ImageUtils.roundCorners(bitmap, cornerRadius);
        if (bitmap == null) {
            rawAvatar.delete();
            return null;
        }

        File roundedAvatar = new File(avatarDir, pictureId.toString());
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(roundedAvatar);
            if (bitmap.compress(PNG, 100, output))
                return new BitmapDrawable(context.getResources(), bitmap);
            else
                return null;
        } catch (IOException e) {
            Log.d(TAG, "Exception writing rounded avatar", e);
            return null;
        } finally {
            if (output != null)
                try {
                    output.close();
                } catch (IOException e) {
                    // Ignored
                }
            rawAvatar.delete();
        }
    }

    /**
     * Sets the logo on the {@link com.actionbarsherlock.app.ActionBar} to the Picture's avatar.
     *
     * @param actionBar
     * @param Picture
     * @return this helper
     */
    public PictureImageLoader bind(final ActionBar actionBar, final Picture Picture) {
        return bind(actionBar, new AtomicReference<Picture>(Picture));
    }

    /**
     * Sets the logo on the {@link com.actionbarsherlock.app.ActionBar} to the Picture's avatar.
     *
     * @param actionBar
     * @param pictureReference
     * @return this helper
     */
    public PictureImageLoader bind(final ActionBar actionBar,
                             final AtomicReference<Picture> pictureReference) {
        if (pictureReference == null)
            return this;

        final Picture picture = pictureReference.get();
        if (picture == null)
            return this;

        final String avatarUrl = picture.getThumbUrl();
        if (TextUtils.isEmpty(avatarUrl))
            return this;

        final String pictureId = picture._id;

        BitmapDrawable loadedImage = loaded.get(pictureId);
        if (loadedImage != null) {
            actionBar.setLogo(loadedImage);
            return this;
        }

        new FetchAvatarTask(context) {

            @Override
            public BitmapDrawable call() throws Exception {
                final BitmapDrawable image = getImage(picture);
                if (image != null)
                    return image;
                else
                    return fetchAvatar(avatarUrl, pictureId.toString());
            }

            @Override
            protected void onSuccess(BitmapDrawable image) throws Exception {
                final Picture current = pictureReference.get();
                if (current != null && pictureId.equals(current._id))
                    actionBar.setLogo(image);
            }
        }.execute();

        return this;
    }

    private PictureImageLoader setImage(final Drawable image, final ImageView view) {
        return setImage(image, view, null);
    }

    private PictureImageLoader setImage(final Drawable image, final ImageView view,
                                  Object tag) {
        view.setImageDrawable(image);
        view.setTag(R.id.iv_image, tag);
        view.setVisibility(VISIBLE);
        return this;
    }

    private String getAvatarUrl(Picture picture,String type) {
        this.type = type;
        String avatarUrl = URL_BASE;
//        if(type.equals("avatar")){
//            avatarUrl += picture.getAvatarUrl();
//        }else
        if(type.equals("thumb")){
            avatarUrl += picture.getThumbUrl();
        }else{
            avatarUrl += picture.getUrl();
        }
        return avatarUrl;
    }

//    private String getAvatarUrl(CommitPicture Picture) {
//        return getAvatarUrl(GravatarUtils.getHash(Picture.getEmail()));
//    }

    /**
     * Bind view to image at URL
     *
     * @param view
     * @param picture
     * @return this helper
     */
    public PictureImageLoader bind(final ImageView view, final Picture picture) {
        return bind(view,picture,"icon");
    }

    public PictureImageLoader bind(final ImageView view, final Picture picture,final String type) {
        if (picture == null)
            return setImage(loadingAvatar, view);

        String avatarUrl = getAvatarUrl(picture,type);
//        if(avatarUrl.startsWith(URL_BASE + "/assets/noface") || TextUtils.isEmpty(avatarUrl))
//        {
////            if (TextUtils.isEmpty(avatarUrl))
//            return setImage(loadingAvatar, view);
//        }

        final String pictureId = picture._id;

        BitmapDrawable loadedImage = loaded.get(pictureId);
        if (loadedImage != null)
            return setImage(loadedImage, view);

        setImage(loadingAvatar, view, pictureId);

        final String loadUrl = avatarUrl;
        new FetchAvatarTask(context) {

            @Override
            public BitmapDrawable call() throws Exception {
                if (!pictureId.equals(view.getTag(R.id.iv_image)))
                    return null;

                final BitmapDrawable image = getImage(picture);
                if (image != null)
                    return image;
                else
                    return fetchAvatar(loadUrl, pictureId.toString());
            }

            @Override
            protected void onSuccess(final BitmapDrawable image)
                    throws Exception {
                if (image == null)
                    return;
                loaded.put(pictureId, image);
                if (pictureId.equals(view.getTag(R.id.iv_image)))
                    setImage(image, view);
            }

        }.execute();

        return this;
    }

//    /**
//     * Bind view to image at URL
//     *
//     * @param view
//     * @param Picture
//     * @return this helper
//     */
//    public AvatarLoader bind(final ImageView view, final CommitPicture Picture) {
//        if (Picture == null)
//            return setImage(loadingAvatar, view);
//
//        String avatarUrl = getAvatarUrl(Picture);
//
//        if (TextUtils.isEmpty(avatarUrl))
//            return setImage(loadingAvatar, view);
//
//        final String pictureId = Picture.getEmail();
//
//        BitmapDrawable loadedImage = loaded.get(pictureId);
//        if (loadedImage != null)
//            return setImage(loadedImage, view);
//
//        setImage(loadingAvatar, view, pictureId);
//
//        final String loadUrl = avatarUrl;
//        new FetchAvatarTask(context) {
//
//            @Override
//            public BitmapDrawable call() throws Exception {
//                if (!userId.equals(view.getTag(id.iv_image)))
//                    return null;
//
//                final BitmapDrawable image = getImage(Picture);
//                if (image != null)
//                    return image;
//                else
//                    return fetchAvatar(loadUrl, pictureId);
//            }
//
//            @Override
//            protected void onSuccess(final BitmapDrawable image)
//                    throws Exception {
//                if (image == null)
//                    return;
//                loaded.put(pictureId, image);
//                if (pictureId.equals(view.getTag(id.iv_image)))
//                    setImage(image, view);
//            }
//
//        }.execute();
//
//        return this;
//    }
}

