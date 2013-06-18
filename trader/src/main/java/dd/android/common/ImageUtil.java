package dd.android.common;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: dd
 * Date: 13-5-23
 * Time: 下午1:25
 * To change this template use File | Settings | File Templates.
 */
public class ImageUtil {
    private static final int QUALITY = 80;
//    public static Bitmap zoom(Bitmap bmp, int width, int height) {
//        int bmpWidth = bmp.getWidth();
//        int bmpHeght = bmp.getHeight();
//        Matrix matrix = new Matrix();
//        matrix.postScale((float) width / bmpWidth, (float) height / bmpHeght);
//        return Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeght, matrix, true);
//    }
//
//    public static Bitmap zoomIfBmpGt(Bitmap bmp, int width, int height) {
//        if (bmp.getWidth() > width && bmp.getHeight() > height)
//            return zoom(bmp, width, height);
//        else
//            return bmp;
//    }

    /**
     * 将给定图片维持宽高比，指定宽缩放。
     * @param bitmap      原图
     * @return  缩放截取正中部分后的位图。
     */

    public static Bitmap scaleBitmapToTargetWidth(Bitmap bitmap, int targetWidth)
    {
        if(null == bitmap || targetWidth <= 0)
        {
            return  null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if(widthOrg > targetWidth)
        {
            //压缩到一个最小长度是edgeLength的bitmap
            float scale = targetWidth / (float)widthOrg;
            int scaledWidth = targetWidth;
            int scaledHeight = (int)(heightOrg * scale);

            try{
                return  Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            }
            catch(Exception e){
                return null;
            }
        }

        return result;
    }

    public static void writeBitmapToJpg(Bitmap data, String pathName) {
        File file = new File(pathName);
        try {
            file.createNewFile();
            // BufferedOutputStream os = new BufferedOutputStream(
            // new FileOutputStream(file));

            FileOutputStream os = new FileOutputStream(file);
            data.compress(Bitmap.CompressFormat.JPEG, QUALITY, os);
            os.flush();
            os.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
//    /**
//     * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
//     * @param bitmap      原图
//     * @param edgeLength  希望得到的正方形部分的边长
//     * @return  缩放截取正中部分后的位图。
//     */
//
//    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength)
//    {
//        if(null == bitmap || edgeLength <= 0)
//        {
//            return  null;
//        }
//
//        Bitmap result = bitmap;
//        int widthOrg = bitmap.getWidth();
//        int heightOrg = bitmap.getHeight();
//
//        if(widthOrg > edgeLength && heightOrg > edgeLength)
//        {
//            //压缩到一个最小长度是edgeLength的bitmap
//            int longerEdge = (int)(edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
//            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
//            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
//            Bitmap scaledBitmap;
//
//            try{
//                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
//            }
//            catch(Exception e){
//                return null;
//            }
//
//            //从图中截取正中间的正方形部分。
//            int xTopLeft = (scaledWidth - edgeLength) / 2;
//            int yTopLeft = (scaledHeight - edgeLength) / 2;
//
//            try{
//                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
//                scaledBitmap.recycle();
//            }
//            catch(Exception e){
//                return null;
//            }
//        }
//
//        return result;
//    }


}
