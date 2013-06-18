
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
import dd.android.yeshi_trader.core.*;
import dd.android.yeshi_trader.service.NotificationFayeService;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static com.github.kevinsawicki.http.HttpRequest.post;
import static dd.android.yeshi_trader.core.Constants.Extra.COMMODITY;
import static dd.android.yeshi_trader.core.Constants.Extra.NAME;

/**
 * Activity to authenticate the ABUser against an API (example API on Parse.com)
 */
public class ActivityAccounting extends
        ActivityYS {

    @InjectExtra(COMMODITY)
    protected Commodity commodity;

    @InjectView(R.id.tv_quantity)
    private TextView tv_quantity;
    @InjectView(R.id.et_price)
    private EditText et_price;
    @InjectView(R.id.et_quantity)
    private EditText et_quantity;
    @InjectView(R.id.et_total)
    private EditText et_total;

    @InjectView(R.id.btn_submit)
    private Button btn_submit;

    private TextWatcher watcher = validationTextWatcher();

    private RoboAsyncTask<Boolean> authenticationTask;

//    Float price, total;
//    Integer quantity;
    Bill bill;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.act_accounting);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bill = new Bill();
        bill.setCommodity_id(commodity.get_id());

        commodity_to_view();


        et_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //失去焦点
                if (view.hasFocus() == false) {
                    set_total();
                }
            }
        });

        et_quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //失去焦点
                if (view.hasFocus() == false) {
                    if(populated(et_quantity))
                    {
                        Integer quantity = Integer.parseInt(et_quantity.getText().toString());
                        if(quantity > commodity.getReserve()){
                            String str_reserve = String.valueOf(commodity.getReserve());
                            et_quantity.setText(str_reserve);
                            Toaster.showLong(ActivityAccounting.this,"库存只有 " + str_reserve);
                        }
                    }
                    set_total();
                }
            }
        });



        et_total.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //失去焦点
                if (view.hasFocus() == false) {
                    set_price();
                }
            }
        });


        et_total.setOnKeyListener(new OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event != null && ACTION_DOWN == event.getAction()
                        && keyCode == KEYCODE_ENTER && btn_submit.isEnabled()) {
                    handleSubmit(btn_submit);
                    return true;
                }
                return false;
            }
        });

        et_total.setOnEditorActionListener(new OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == IME_ACTION_DONE && btn_submit.isEnabled()) {
                    handleSubmit(btn_submit);
                    return true;
                }
                return false;
            }
        });

        et_total.addTextChangedListener(watcher);
        et_price.addTextChangedListener(watcher);
        et_quantity.addTextChangedListener(watcher);

//        TextView signupText = (TextView) findViewById(R.id.tv_signup);
//        signupText.setMovementMethod(LinkMovementMethod.getInstance());
//        signupText.setText(Html.fromHtml(getString(R.string.signup_link)));
    }

    private void set_total() {
        if (populated(et_price) && populated(et_quantity)) {
            et_total.setText(String.valueOf(Float.parseFloat(et_price.getText().toString()) * Integer.parseInt(et_quantity.getText().toString())));
        }
    }

    private void set_price() {
        if (populated(et_total) && populated(et_quantity)) {
            et_price.setText(String.valueOf(Float.parseFloat(et_total.getText().toString()) / Integer.parseInt(et_quantity.getText().toString())));
        }
    }

    private void commodity_to_view() {
        et_price.setText(commodity.getHumanize_price());
        tv_quantity.setText(tv_quantity.getText().toString() + " 现有：" + String.valueOf(commodity.getReserve()));
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
        boolean populated = populated(et_price) && populated(et_quantity) && populated(et_total);
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
//
        bill.setQuantity(Integer.parseInt(et_quantity.getText().toString()));
        bill.setPrice(Float.parseFloat(et_price.getText().toString()));
        bill.setTotal(Float.parseFloat(et_total.getText().toString()));
//
        authenticationTask = new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {
                return ServiceYS.postBill(bill);
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                Toaster.showLong(ActivityAccounting.this, "发送失败！");
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
            setResult(RESULT_OK, (new Intent()).setAction(bill.getQuantity().toString()));
            finish();
        } else {
            Toaster.showLong(this,
                    "发送失败！");
        }
    }

}
