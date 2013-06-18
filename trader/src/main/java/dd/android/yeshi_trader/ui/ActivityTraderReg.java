
package dd.android.yeshi_trader.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.wishlist.Toaster;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockAccountAuthenticatorActivity;
import dd.android.yeshi_trader.R;
import dd.android.yeshi_trader.core.*;
import dd.android.yeshi_trader.service.NotificationFayeService;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.Map;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static dd.android.yeshi_trader.core.Constants.Extra.NAME;
import static dd.android.yeshi_trader.core.Constants.Http.*;

import static com.github.kevinsawicki.http.HttpRequest.*;

/**
 * Activity to authenticate the ABUser against an API (example API on Parse.com)
 */
public class ActivityTraderReg extends
        RoboSherlockAccountAuthenticatorActivity {

    @InjectView(R.id.et_name)
    private EditText et_name;

    @InjectView(R.id.et_email)
    private EditText et_email;

    @InjectView(R.id.et_phone)
    private EditText et_phone;

    @InjectView(R.id.et_password)
    private EditText et_password;

    @InjectView(R.id.et_password_confirmation)
    private EditText et_password_confirmation;

    @InjectView(R.id.et_trader_name)
    private EditText et_trader_name;

    @InjectView(R.id.et_trader_address)
    private EditText et_trader_address;

    @InjectView(R.id.btn_signup)
    private Button btn_signuup;

    private TextWatcher watcher = validationTextWatcher();

    private RoboAsyncTask<Boolean> loginTask;
    private static String authToken;
    private String authTokenType;

    String str_result;

    final String format_labels = "label_%s";
    final String format_chat_errors_line = "%s: %s\n";
    /**
     * If set we are just checking that the ABUser knows their credentials; this
     * doesn't cause the ABUser's password to be changed on the device.
     */
    private String email, password, password_confirmation, name, phone, trader_name, trader_address;


    /**
     * In this instance the token is simply the sessionId returned from Parse.com. This could be a
     * oauth token or some other type of timed token that expires/etc. We're just using the parse.com
     * sessionId to prove the example of how to utilize a token.
     */
    private static String token;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.act_trader_reg);

        et_trader_address.setOnKeyListener(new OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event != null && ACTION_DOWN == event.getAction()
                        && keyCode == KEYCODE_ENTER && btn_signuup.isEnabled()) {
                    handleReg(btn_signuup);
                    return true;
                }
                return false;
            }
        });

        et_trader_address.setOnEditorActionListener(new OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == IME_ACTION_DONE && btn_signuup.isEnabled()) {
                    handleReg(btn_signuup);
                    return true;
                }
                return false;
            }
        });

        et_name.addTextChangedListener(watcher);
        et_password.addTextChangedListener(watcher);
        et_password_confirmation.addTextChangedListener(watcher);
        et_email.addTextChangedListener(watcher);
        et_phone.addTextChangedListener(watcher);
        et_trader_address.addTextChangedListener(watcher);
        et_trader_name.addTextChangedListener(watcher);
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
        boolean populated = populated(et_name) &&
                populated(et_password) &&
                populated(et_password_confirmation) &&
                populated(et_trader_name) &&
                (populated(et_email) || populated(et_phone)) &&
                is_password_confirmation();
        btn_signuup.setEnabled(populated);
    }

    private boolean is_password_confirmation() {
        return et_password.getText().toString().equals(et_password_confirmation.getText().toString());
    }

    private boolean populated(EditText editText) {
        return editText.length() > 0;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getText(R.string.message_signing_in));
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                if (loginTask != null)
                    loginTask.cancel(true);
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
    public void handleReg(View view) {
        if (loginTask != null)
            return;

        email = et_email.getText().toString();
        password = et_password.getText().toString();
        password_confirmation = et_password_confirmation.getText().toString();
        name = et_name.getText().toString();
        phone = et_phone.getText().toString();
        trader_name = et_trader_name.getText().toString();
        trader_address = et_trader_address.getText().toString();
        showProgress();

        loginTask = new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {

//                final String query = String.format("%s=%s&%s=%s", PARAM_USERNAME, email, PARAM_PASSWORD, password);
                HttpRequest request = post(API_REG)
//                        .part(HEADER_PARSE_APP_ID, PARSE_APP_ID)
//                        .part(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
                        .part("user[email]", email)
                        .part("user[password]", password)
                        .part("user[password_confirmation]", password_confirmation)
                        .part("user[name]", name)
                        .part("user[phone]", phone)
                        .part("user[is_trader]","1")
                        .part("user[trader_attributes][name]",trader_name)
                        .part("user[trader_attributes][address]",trader_address)
                        ;


                Log.d("Auth", "response=" + request.code());

                str_result = "";
                if(request.code() == 201) {
                    str_result = request.body();
                    Log.d("response body:",str_result);
                    final AccessToken model = JSON.parseObject(str_result, AccessToken.class);
                    token = model.getAccess_token();
                    return true;
                }
                else{
                    str_result = request.body();
                    return false;
                }

            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                show_result_errors();
            }

            @Override
            public void onSuccess(Boolean authSuccess) {
                onAuthenticationResult(authSuccess);
            }

            @Override
            protected void onFinally() throws RuntimeException {
                hideProgress();
                loginTask = null;
            }
        };
        loginTask.execute();
    }

    private void show_result_errors() {
        String str_errors = "";
        try {
            if (str_result != null && !str_result.equals("")) {
                for (Map.Entry<String, Object> obj : JSON.parseObject(str_result).entrySet()) {
                    str_errors += String.format(format_chat_errors_line, getStringResourceByName(String.format(format_labels, obj.getKey())), obj.getValue());
                }
            }
            Toaster.showLong(this, str_errors);
        } catch (Exception ex) {
            Toaster.showLong(this, "发生未知错误");
        }

        Toaster.showLong(this, str_errors);
    }

    /**
     * Called when response is received from the server for authentication
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller. Also sets
     * the authToken in AccountManager for this account.
     */

    protected void finishLogin() {
        PropertiesController.readConfiguration();
        Settings.getFactory().setAuthToken(token);

        PropertiesController.writeConfiguration();
        get_me();
    }

    private void get_me() {
        showProgress();
        new RoboAsyncTask<User>(this) {
            public User call() throws Exception {

                User user = ServiceYS.getMe();

                return user;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                String message = "获取个人信息失败";

                Toaster.showLong(ActivityTraderReg.this, message);
                finish();
            }

            @Override
            public void onSuccess(User user) {
                onGetMeResult(user);
            }

            @Override
            protected void onFinally() throws RuntimeException {
                hideProgress();
                finish();
            }
        }.execute();
    }

    /**
     * Hide progress dialog
     */
    @SuppressWarnings("deprecation")
    protected void hideProgress() {
        dismissDialog(0);
    }

    /**
     * Show progress dialog
     */
    @SuppressWarnings("deprecation")
    protected void showProgress() {
        showDialog(0);
    }

    /**
     * Called when the authentication process completes (see attemptLogin()).
     *
     * @param result
     */
    public void onGetMeResult(User result) {
        if (result != null)
        {
            Settings.getFactory().setUser(result);
            PropertiesController.writeConfiguration();
            startService(new Intent(this, NotificationFayeService.class).putExtra(NAME, result.getName()));
        }
        else {
            Toaster.showLong(ActivityTraderReg.this,
                    "获取个人信息失败");
        }
    }


    /**
     * Called when the authentication process completes (see attemptLogin()).
     *
     * @param result
     */
    public void onAuthenticationResult(boolean result) {
        if (result)
            finishLogin();
        else {
            show_result_errors();
        }
    }

    public void handleLogin(View view) {
        if (loginTask != null)
            return;
        startActivity(new Intent(this,ActivityLogin.class));
    }

    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }
}
