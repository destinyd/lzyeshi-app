
package dd.android.yeshi_trader.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.github.kevinsawicki.wishlist.Toaster;
import dd.android.yeshi_trader.R;
import dd.android.yeshi_trader.core.Commodity;
import dd.android.yeshi_trader.core.Group;
import dd.android.yeshi_trader.core.ServiceYS;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static dd.android.yeshi_trader.core.Constants.Extra.GROUP;

/**
 * Activity to authenticate the ABUser against an API (example API on Parse.com)
 */
public class ActivityFormGroup extends
        ActivityYS {

//    @InjectExtra(GROUP)
    protected Group group = null;

    @InjectView(R.id.et_category_list)
    private EditText et_category_list;
    @InjectView(R.id.et_name)
    private EditText et_name;
    @InjectView(R.id.et_price)
    private EditText et_price;
    @InjectView(R.id.et_reserve)
    private EditText et_reserve;

    @InjectView(R.id.btn_submit)
    private Button btn_submit;

    private TextWatcher watcher = validationTextWatcher();

    private RoboAsyncTask<Boolean> authenticationTask;

    Boolean isUpdate = false;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.act_form_group);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        get_extra();
        et_category_list.setSelection(et_category_list.getText().toString().length());

        et_reserve.setOnKeyListener(new OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event != null && ACTION_DOWN == event.getAction()
                        && keyCode == KEYCODE_ENTER && btn_submit.isEnabled()) {
                    handleSubmit(btn_submit);
                    return true;
                }
                return false;
            }
        });

        et_reserve.setOnEditorActionListener(new OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == IME_ACTION_DONE && btn_submit.isEnabled()) {
                    handleSubmit(btn_submit);
                    return true;
                }
                return false;
            }
        });

        et_category_list.addTextChangedListener(watcher);
        et_name.addTextChangedListener(watcher);

//        TextView signupText = (TextView) findViewById(R.id.tv_signup);
//        signupText.setMovementMethod(LinkMovementMethod.getInstance());
//        signupText.setText(Html.fromHtml(getString(R.string.signup_link)));
    }

    private void get_extra() {
        Intent intent = getIntent();
        group = (Group) intent.getSerializableExtra(GROUP);
        if(group != null)
        {
            get_group();
            isUpdate = true;
        }
    }

    private void group_to_view() {
        et_category_list.setText(group.getCategory_list());
        et_name.setText(group.getName());
        et_price.setText(group.getHumanize_price());
        et_reserve.setText(String.valueOf(group.getReserve()));
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
    }

    private void updateUIWithValidation() {
        boolean populated = populated(et_category_list) && populated(et_name);
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

        progressDialogShow();

        if(group == null)
            group = new Group();
        group.setCategory_list(et_category_list.getText().toString());
        group.setName(et_name.getText().toString());
        group.setHumanize_price(et_price.getText().toString());
        group.setReserve(Integer.valueOf(et_reserve.getText().toString()));
//
        authenticationTask = new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {
                if(isUpdate)
                    return ServiceYS.updateGroup(group);
                else
                    return ServiceYS.postGroup(group);
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                Toaster.showLong(ActivityFormGroup.this, "发送失败！");
            }

            @Override
            public void onSuccess(Boolean postSuccess) {
                onPostSuccess(postSuccess);
            }

            @Override
            protected void onFinally() throws RuntimeException {
                progressDialogDismiss();
                authenticationTask = null;
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
            Toaster.showLong(this,"发送成功。");
            setResult(RESULT_OK, (new Intent()).setAction("success"));
            finish();
        } else {
            Toaster.showLong(this,
                    "发送失败！");
        }
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
                Toaster.showLong(ActivityFormGroup.this, "获取分组详细资料失败。");
                hide_progress();
            }

            @Override
            public void onSuccess(Boolean success) {
                if(success)
                    group_to_view();
                else
                    Toaster.showLong(ActivityFormGroup.this, "你不拥有此商品分组。");
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
}
