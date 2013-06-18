package dd.android.common;

import android.os.Environment;

/**
 * Created with IntelliJ IDEA.
 * User: dd
 * Date: 13-4-19
 * Time: 下午1:50
 * To change this template use File | Settings | File Templates.
 */
public class SDCard {
    /**
     * 1、判断SD卡是否存在
     */
    public static boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}
