
package dd.android.yeshi_trader.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.wishlist.Toaster;
import com.google.inject.Inject;
import dd.android.common.ImageUtil;
import dd.android.common.SDCard;
import dd.android.yeshi_trader.R;
import dd.android.yeshi_trader.core.Commodity;
import dd.android.yeshi_trader.core.Group;
import dd.android.yeshi_trader.core.PictureImageLoader;
import dd.android.yeshi_trader.core.ServiceYS;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static dd.android.yeshi_trader.core.Constants.Extra.*;
import static dd.android.yeshi_trader.core.Constants.Setting.*;
import static dd.android.yeshi_trader.core.Constants.Picture.*;

/**
 * Activity to authenticate the ABUser against an API (example API on Parse.com)
 */
public class ActivityFormCommodity extends
        ActivityYS {

    protected Group group = null;

    protected Commodity commodity = null, p_commodity;
    @InjectView(R.id.iv_picture)
    private ImageView iv_picture;

    @InjectView(R.id.et_category_list)
    private EditText et_category_list;
    @InjectView(R.id.et_name)
    private EditText et_name;
    @InjectView(R.id.et_price)
    private EditText et_price;
    @InjectView(R.id.et_reserve)
    private EditText et_reserve;
    @InjectView(R.id.et_desc)
    private EditText et_desc;

    @InjectView(R.id.btn_submit)
    private Button btn_submit;

    @Inject
    private PictureImageLoader avatars;

    private TextWatcher watcher = validationTextWatcher();

    private RoboAsyncTask<Boolean> authenticationTask;

    Boolean isUpdate = false, isPictureChange = false;

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    // 创建一个以当前时间为名称的文件
    Uri uri_image;
    String strFilePath = null;
    final CharSequence[] items = {"相册", "相机"};

    final int SELECT_PICTURE = 0, SELECT_CAMER = 1;
    Bitmap bmpScale = null;

    /**
     * 临时存储位置
     */
    private String tempPicPath, scalePicPath;

    int screenWidth;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.act_form_commodity);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getScreenWidth();


        get_extra();
        et_category_list.setSelection(et_category_list.getText().toString().length());

        et_desc.setOnKeyListener(new OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event != null && ACTION_DOWN == event.getAction()
                        && keyCode == KEYCODE_ENTER && btn_submit.isEnabled()) {
                    handleSubmit(btn_submit);
                    return true;
                }
                return false;
            }
        });

        et_desc.setOnEditorActionListener(new OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == IME_ACTION_DONE && btn_submit.isEnabled()) {
                    handleSubmit(btn_submit);
                    return true;
                }
                return false;
            }
        });

        add_watcher();

        String sd_path = Environment
                .getExternalStorageDirectory() + SDCARD_PATH + "/tmp/" + String.valueOf(System.currentTimeMillis());
        File file = new File(sd_path);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        tempPicPath = sd_path + ".jpg";
        scalePicPath = sd_path + "_scale.jpg";

//        TextView signupText = (TextView) findViewById(R.id.tv_signup);
//        signupText.setMovementMethod(LinkMovementMethod.getInstance());
//        signupText.setText(Html.fromHtml(getString(R.string.signup_link)));
    }

    private void add_watcher() {
        et_category_list.addTextChangedListener(watcher);
        et_name.addTextChangedListener(watcher);
        et_price.addTextChangedListener(watcher);
        et_reserve.addTextChangedListener(watcher);
        et_desc.addTextChangedListener(watcher);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
//选择图片
                Uri uri = data.getData();
                set_path_and_picture(uri);
                isPictureChange = true;
            } else if (requestCode == SELECT_CAMER) {
                strFilePath = tempPicPath;
                recycle_bmp(bmpScale);
                bmpScale = scalePictureToTargetWidth(tempPicPath, scalePicPath, screenWidth);
                iv_picture.setImageBitmap(bmpScale);
                isPictureChange = true;
            }
        }
//        if (requestCode == PHOTO_REQUEST_TAKEPHOTO) {
//            if (resultCode == RESULT_OK) { //使用者按下確定
//                if (requestCode == PHOTO_REQUEST_TAKEPHOTO) { //來源為圖片
//                    if(data != null){
//                        ContentResolver cr = this.getContentResolver();
//                        //get the physical path of the image
//                        Cursor c = cr.query(uri_image, null, null, null, null);
//                        c.moveToFirst();
//                        strFilePath = c.getString(c.getColumnIndex("_data"));
//
//                        //取得圖片位址
//                        uri_image = data.getData();
//                        strFilePath = uri_image.getPath();
//    //                    Bitmap bmp = null;
//                        iv_picture.setImageURI(uri_image);
//                    }
//                    else
//                    {
//                        Toaster.showLong(this, "未返回图片数据.");
//                    }
////                try {
////                    //取得圖片Bitmap
////                    bmp = MediaStore.Images.Media.getBitmap(resolver, uri);
////                } catch (FileNotFoundException e) {
////                    e.printStackTrace();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//                }
//            } else if (resultCode == RESULT_CANCELED) {
//                //使用者按下取消或離開
//            }
//        }
//        switch (requestCode) {
//            case PHOTO_REQUEST_TAKEPHOTO:
////                startPhotoZoom(Uri.fromFile(tempFile), 150);
//                uri_image = Uri.fromFile(tempFile);
//                iv_picture.setImageURI(uri_image);
//                break;
//
////            case PHOTO_REQUEST_GALLERY:
////                if (resultCode == RESULT_OK && null != data) {
////                    Uri selectedImage = data.getData();
////                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
////
////                    Cursor cursor = getContentResolver().query(selectedImage,
////                            filePathColumn, null, null, null);
////                    cursor.moveToFirst();
////                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
////                    strFilePath = cursor.getString(columnIndex);
////                    cursor.close();
////                    iv_picture.setImageBitmap(BitmapFactory.decodeFile(strFilePath));
////                }
//////                if (data != null)uri_image = data.getData(); if (data != null)startPhotoZoom(data.getData(), 150);
////                break;
////
////            case PHOTO_REQUEST_CUT:
////                if (data != null)
////                    setPicToView(data);
////                break;
//        }
    }

    private Bitmap scalePictureToTargetWidth(String strFormFilePath, String strToFilePath, int nowWidth) {
        Bitmap bmp = BitmapFactory.decodeFile(strFormFilePath);
        Bitmap bmpScale = ImageUtil.scaleBitmapToTargetWidth(bmp, SCALE_WIDTH);
        if(strToFilePath != null)
            ImageUtil.writeBitmapToJpg(bmpScale,strToFilePath);
        bmpScale.recycle();
        bmpScale = ImageUtil.scaleBitmapToTargetWidth(bmp, nowWidth);
        bmp.recycle();
        return bmpScale;
    }

    private void recycle_bmp(Bitmap bmp) {
        if(bmp != null)
        {
            bmp.recycle();
        }
    }

    private void set_path_and_picture(Uri uri) {
        if (uri != null) {
            strFilePath = getPath(uri);

            if (strFilePath != null) {
                resizePicture();
                return;
            }
        }
        Toaster.showLong(this, "没有返回任何数据.");
    }

    private void get_extra() {
        Intent intent = getIntent();
        commodity = (Commodity) intent.getSerializableExtra(COMMODITY);
        group = (Group) intent.getSerializableExtra(GROUP);
        if (commodity != null) {
            commodity_to_view();
            isUpdate = true;
        }
        else{
            if(group != null)
                get_group();
        }
    }

    private void commodity_to_view() {
        et_category_list.setText(commodity.getCategory_list());
        et_name.setText(commodity.getName());
        et_price.setText(String.valueOf(commodity.getHumanize_price()));
        et_reserve.setText(String.valueOf(commodity.getReserve()));
        et_desc.setText(commodity.getDesc());
        if(commodity.getPicture() != null)
            avatars.bind(iv_picture, commodity.getPicture(),"android");
    }

    private TextWatcher validationTextWatcher() {
        return new AdapterTextWatcher() {
            public void afterTextChanged(Editable gitDirEditText) {
                updateUIWithValidation();
            }

        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUIWithValidation();
        if(screenWidth != getWindowManager().getDefaultDisplay().getWidth())
        {
            getScreenWidth();
            resizePicture();
        }

    }

    private void getScreenWidth() {
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
    }

    private void resizePicture() {
        if(strFilePath != null)
        {
            recycle_bmp(bmpScale);

            bmpScale = scalePictureToTargetWidth(strFilePath, scalePicPath, screenWidth);
            iv_picture.setImageBitmap(bmpScale);
        }
    }

    private void updateUIWithValidation() {
        boolean populated = populated(et_category_list) && populated(et_name) && populated(et_price) && populated(et_reserve);
        btn_submit.setEnabled(populated);
    }

    private boolean populated(EditText editText) {
        return editText.length() > 0;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在提交...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                if (authenticationTask != null)
                    authenticationTask.cancel(true);
            }
        });
        return dialog;
    }

    /**
     * Handles onClick event on the Submit button. Sends username/password to
     * the server for authentication.
     * <p/>
     * Specified by android:onClick="handleReg" in the layout xml
     *
     * @param view
     */
    public void handleSubmit(View view) {
        if (authenticationTask != null)
            return;

        setSupportProgressBarIndeterminate(true);
        progressDialogShow();

//        if (commodity == null)
        p_commodity = new Commodity();

        p_commodity.setCategory_list(et_category_list.getText().toString());
        p_commodity.setName(et_name.getText().toString());
        p_commodity.setHumanize_price(et_price.getText().toString());
        p_commodity.setReserve(Integer.parseInt(et_reserve.getText().toString()));
        p_commodity.setDesc(et_desc.getText().toString());
        if(group != null)
            p_commodity.setGroup_id(group.get_id());

        try {
            if (!(isUpdate && !isPictureChange)) {
                p_commodity.setStrPicture(scalePicPath);
                p_commodity.setFilePicture(new File(scalePicPath));
            }
        } catch (NullPointerException e) {
            Toaster.showLong(this, "请选择图片,再提交.");
        }
//
        authenticationTask = new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {
                if (isUpdate)
                    return ServiceYS.updateCommodity(commodity,p_commodity);
                else
                    return ServiceYS.postCommodity(p_commodity);
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                Toaster.showLong(ActivityFormCommodity.this,
                        "请检查图片是否选择、网络是否链接！");
                progressDialogDismiss();
                setSupportProgressBarIndeterminate(false);
                authenticationTask = null;
            }

            @Override
            public void onSuccess(Boolean postSuccess) {
                onPostSuccess(postSuccess);
            }

            @Override
            protected void onFinally() throws RuntimeException {
                progressDialogDismiss();
                authenticationTask = null;
                setSupportProgressBarIndeterminate(false);
            }
        };
        authenticationTask.execute();
    }

    /**
     * Called when the authentication process completes (see attemptLogin()).
     *
     * @param result
     */
    public void onPostSuccess(boolean result) {
        if (result) {
            Toaster.showLong(this, "发送成功。");
            setResult(RESULT_OK, (new Intent()).setAction("success"));
            finish();
        } else {
            Toaster.showLong(this,
                    "请检查图片是否选择、网络是否链接！");
        }
    }

    public void handleSelectPicture(View view) {
        showDialog();
    }

    //提示对话框方法
    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("选择图片来源")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == SELECT_PICTURE) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "选择图片"), SELECT_PICTURE);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(new File(tempPicPath)));

//                            if (hasImageCaptureBug()) {
//                                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/sdcard/tmp")));
//                            } else {
//                                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                            }
                            startActivityForResult(intent, SELECT_CAMER);
                        }
                    }
                })
                .create().show();
//                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        // 调用系统的拍照功能
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        // 指定调用相机拍照后照片的储存路径
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
//                        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
//                    }
//                })
//                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        Intent intent = new Intent(Intent.ACTION_PICK, null);
////                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                        intent.setType("image/*");
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
////                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
//                    }
//                }).show();
    }


    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = managedQuery(uri, projection, null, null, null);

        if (cursor == null)
            return null;

        int column_index = cursor

                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);

    }

    private void get_group() {
        new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {
                Group p_group = ServiceYS.apiGroup(group.get_id());
                if (p_group != null) {
                    group = p_group;
                    return true;
                }
                else{
                    return false;
                }
            }

            @Override
            protected void onPreExecute() throws Exception {
                super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
                setProgressBarIndeterminate(true);
                progressDialogShow();
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                e.printStackTrace();
                Toaster.showLong(ActivityFormCommodity.this, "获取分组详细资料失败。");
                hide_progress();
            }

            @Override
            public void onSuccess(Boolean success) {
                if(success)
                    group_to_view();
                else
                    Toaster.showLong(ActivityFormCommodity.this, "你不拥有此商品分组。");
            }

            @Override
            protected void onFinally() throws RuntimeException {
                hide_progress();
            }
        }.execute();
    }

    private void hide_progress() {
        setProgressBarIndeterminate(false);
        progressDialogDismiss();
    }

    private void group_to_view() {
        et_category_list.setText(group.getCategory_list());
        et_name.setText(group.getName());
        et_price.setText(group.getHumanize_price());
        et_reserve.setText(String.valueOf(group.getReserve()));
    }
}
