
package dd.android.yeshi.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.*;
import android.widget.TextView.OnEditorActionListener;
import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.wishlist.Toaster;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockAccountAuthenticatorActivity;
import dd.android.yeshi.R;
import dd.android.yeshi.core.*;
import dd.android.yeshi.service.NotificationFayeService;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import roboguice.util.RoboAsyncTask;
import roboguice.util.Strings;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static com.github.kevinsawicki.http.HttpRequest.post;
import static dd.android.yeshi.core.Constants.Http.*;
import static dd.android.yeshi.core.Constants.Extra.*;

/**
 * Activity to authenticate the ABUser against an API (example API on Parse.com)
 */
public class ActivityLogin extends
        RoboSherlockAccountAuthenticatorActivity {

    /**
     * PARAM_CONFIRMCREDENTIALS
     */
    public static final String PARAM_CONFIRMCREDENTIALS = "confirmCredentials";

    /**
     * PARAM_PASSWORD
     */
    public static final String PARAM_PASSWORD = "password";

    /**
     * PARAM_USERNAME
     */
    public static final String PARAM_USERNAME = "username";


    @InjectView(R.id.et_login)
    private AutoCompleteTextView et_login;

    @InjectView(R.id.et_password)
    private EditText passwordText;

    @InjectView(R.id.b_signin)
    private Button signinButton;

    private TextWatcher watcher = validationTextWatcher();

    private RoboAsyncTask<Boolean> authenticationTask;
    private static String authToken;
    private String authTokenType;

    /**
     * If set we are just checking that the ABUser knows their credentials; this
     * doesn't cause the ABUser's password to be changed on the device.
     */
    private String login;

    private String password;


    /**
     * In this instance the token is simply the sessionId returned from Parse.com. This could be a
     * oauth token or some other type of timed token that expires/etc. We're just using the parse.com
     * sessionId to prove the example of how to utilize a token.
     */
    private static String token;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.act_login);

        passwordText.setOnKeyListener(new OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event != null && ACTION_DOWN == event.getAction()
                        && keyCode == KEYCODE_ENTER && signinButton.isEnabled()) {
                    handleLogin(signinButton);
                    return true;
                }
                return false;
            }
        });

        passwordText.setOnEditorActionListener(new OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                if (actionId == IME_ACTION_DONE && signinButton.isEnabled()) {
                    handleLogin(signinButton);
                    return true;
                }
                return false;
            }
        });

        et_login.addTextChangedListener(watcher);
        passwordText.addTextChangedListener(watcher);

        TextView signupText = (TextView) findViewById(R.id.tv_signup);
        signupText.setMovementMethod(LinkMovementMethod.getInstance());
        signupText.setText(Html.fromHtml(getString(R.string.signup_link)));
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
        if(Settings.getFactory().isLogin())
            finish();
    }

    private void updateUIWithValidation() {
        boolean populated = populated(et_login) && populated(passwordText);
        signinButton.setEnabled(populated);
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
    public void handleLogin(View view) {
        if (authenticationTask != null)
            return;

        login = et_login.getText().toString();
        password = passwordText.getText().toString();
        showProgress();

        authenticationTask = new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {

//                final String query = String.format("%s=%s&%s=%s", PARAM_USERNAME, login, PARAM_PASSWORD, password);
                final String query = String.format("?%s=%s", HEADER_PARSE_GRANT_TYPE, GRANT_TYPE);
                HttpRequest request = post(URL_AUTH + query)
                        .part(HEADER_PARSE_APP_ID, PARSE_APP_ID)
                        .part(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
                        .part(PARAM_USERNAME, login)
                        .part(PARAM_PASSWORD, password)
                        ;


                Log.d("Auth", "response=" + request.code());

                if(request.ok()) {
                    String tmp = Strings.toString(request.buffer());
                    Log.d("response body:",tmp);
                    final AccessToken model = JSON.parseObject(tmp, AccessToken.class);
                    token = model.getAccess_token();
                }

                return request.ok();
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                Throwable cause = e.getCause() != null ? e.getCause() : e;

                String message;
                // A 404 is returned as an Exception with this message
                if ("Received authentication challenge is null".equals(cause
                        .getMessage()))
                    message = getResources().getString(
                            R.string.message_bad_credentials);
                else
                    message = cause.getMessage();

                Toaster.showLong(ActivityLogin.this, message);
            }

            @Override
            public void onSuccess(Boolean authSuccess) {
                onAuthenticationResult(authSuccess);
            }

            @Override
            protected void onFinally() throws RuntimeException {
                hideProgress();
                authenticationTask = null;
            }
        };
        authenticationTask.execute();
    }

    /**
     * Called when response is received from the server for authentication
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller. Also sets
     * the authToken in AccountManager for this account.
     */

    protected void finishLogin() {
        PropertiesController.readConfiguration();
//        final Account account = new Account(login, Constants.Auth.YESHI_ACCOUNT_TYPE);

//        if (requestNewAccount)
//            accountManager.addAccountExplicitly(account, password, null);
//        else
//            accountManager.setPassword(account, password);
//        final Intent intent = new Intent();
        Settings.getFactory().setAuthToken(token);

        PropertiesController.writeConfiguration();
//        intent.putExtra(KEY_ACCOUNT_NAME, login);
//        intent.putExtra(KEY_ACCOUNT_TYPE, Constants.Auth.YESHI_ACCOUNT_TYPE);
//        if (authTokenType != null
//                && authTokenType.equals(Constants.Auth.AUTHTOKEN_TYPE))
//        {
//            intent.putExtra(KEY_AUTHTOKEN, authToken);
//            accountManager.setAuthToken(account,authTokenType,authToken);
//        }
//        setAccountAuthenticatorResult(intent.getExtras());
//        setResult(RESULT_OK, intent);
        get_me();
        finish();
    }

    private void get_me() {
        showProgress();
        new RoboAsyncTask<User>(this) {
            public User call() throws Exception {

                final String query = String.format("?%s=%s", HEADER_PARSE_GRANT_TYPE, GRANT_TYPE);
                User user = ServiceYS.getMe();

                return user;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                String message = "获取个人信息失败";

                Toaster.showLong(ActivityLogin.this, message);
            }

            @Override
            public void onSuccess(User user) {
                onGetMeResult(user);
            }

            @Override
            protected void onFinally() throws RuntimeException {
                hideProgress();
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
            Toaster.showLong(ActivityLogin.this,
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
            Ln.d("onAuthenticationResult: failed to authenticate");
            Toaster.showLong(ActivityLogin.this,
                    R.string.message_auth_failed_new_account);
        }
    }

//    /**
//     * Handles onClick event on the Submit button. Sends username/password to
//     * the server for authentication.
//     * <p/>
//     * Specified by android:onClick="handleReg" in the layout xml
//     *
//     * @param view
//     */
//    public void handleTest(View view) {
//        if (authenticationTask != null)
//            return;
//        password = "";
//        showProgress();
//
//        authenticationTask = new RoboAsyncTask<Boolean>(this) {
//            public Boolean call() throws Exception {
//
//                HttpRequest request = post(URL_TEST)
//                        .part(HEADER_PARSE_APP_ID, PARSE_APP_ID)
//                        .part(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
//                        ;
//
//
//                Log.d("Auth", "response=" + request.code());
//
//                if(request.code() == 201) {
//                    String tmp = Strings.toString(request.buffer());
//                    Log.d("response body:",tmp);
//                    final AccessToken model = JSON.parseObject(tmp, AccessToken.class);
//                    token = model.getAccess_token();
//                    JSONObject j = JSON.parseObject(tmp);
//                    login = j.getString("login");
//                    return true;
//                }
//
//                return false;
//            }
//
//            @Override
//            protected void onException(Exception e) throws RuntimeException {
//                Throwable cause = e.getCause() != null ? e.getCause() : e;
//
//                String message;
//                // A 404 is returned as an Exception with this message
//                if ("Received authentication challenge is null".equals(cause
//                        .getMessage()))
//                    message = getResources().getString(
//                            string.message_bad_credentials);
//                else
//                    message = cause.getMessage();
//
//                Toaster.showLong(ZhaohaiAuthenticatorActivity.this, message);
//            }
//
//            @Override
//            public void onSuccess(Boolean authSuccess) {
//                onAuthenticationResult(authSuccess);
//            }
//
//            @Override
//            protected void onFinally() throws RuntimeException {
//                hideProgress();
//                authenticationTask = null;
//            }
//        };
//        authenticationTask.execute();
//    }

    public void handleReg(View view) {
        if (authenticationTask != null)
            return;
        startActivity(new Intent(this,ActivityReg.class));
    }
}
