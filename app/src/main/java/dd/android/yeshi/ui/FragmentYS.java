
package dd.android.yeshi.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

/**
 * Activity to authenticate the ABUser against an API (example API on Parse.com)
 */
public class FragmentYS extends
        RoboSherlockFragment {

    ProgressDialog progressDialog = null;

    protected void progressDialogDismiss() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    protected void progressDialogShow(Activity activity) {
        progressDialogShow(activity, "正在拼命读取中...");
    }

    protected void progressDialogShow(Activity activity, String message) {
        progressDialogShow(activity, message, true);
    }

    protected void progressDialogShow(Activity activity, String message, boolean cancelable) {
        progressDialogDismiss();
        progressDialog = ProgressDialog.show(activity, "", message, true, cancelable);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                progressDialogDismiss();
            }
        });
    }

    @Override
    public void onDestroy() {
        progressDialogDismiss();
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
    }

}
