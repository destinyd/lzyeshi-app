package dd.android.yeshi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.wishlist.Toaster;
import dd.android.yeshi.R;
import dd.android.yeshi.core.*;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.Map;

import static dd.android.yeshi.core.Constants.Extra.COMMODITY;
import static dd.android.yeshi.core.Constants.Extra.TRADER;
//import com.umeng.analytics.MobclickAgent;


public class ActivityChat extends ActivityYS {

    @InjectView(R.id.label_chatable)
    protected TextView label_chatable;

    @InjectView(R.id.et_chatable)
    protected TextView tv_chatable;
    @InjectView(R.id.et_user_name)
    protected EditText et_user_name;
    @InjectView(R.id.et_content)
    protected EditText et_content;
    @InjectView(R.id.btn_submit)
    protected Button btn_submit;

    @InjectExtra(COMMODITY)
    protected Commodity commodity;

    @InjectExtra(TRADER)
    protected Trader trader;

    private RoboAsyncTask<Boolean> postTask;

    ChatMessage chatMessage = null;

    String result = null;

    final String format_labels = "label_%s";
    final String format_chat_errors_line = "%s: %s\n";

//    @Inject
//    private PictureImageLoader avatars;


    private TextWatcher watcher = validationTextWatcher();

    private TextWatcher validationTextWatcher() {
        return new AdapterTextWatcher() {
            public void afterTextChanged(Editable gitDirEditText) {
                updateUIWithValidation();
            }

        };
    }

    private void updateUIWithValidation() {
        boolean populated = populated(et_user_name) && populated(et_content);
        btn_submit.setEnabled(populated);
    }

    private boolean populated(EditText editText) {
        return editText.length() > 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_chat);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        extra_to_view();

        et_user_name.addTextChangedListener(watcher);
        et_content.addTextChangedListener(watcher);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handle_post_chat_message(v);
            }
        });
    }

    private void handle_post_chat_message(View v) {
        if (postTask != null)
            return;

        progressDialogShow(this);

        String user_name = et_user_name.getText().toString();
        String content = et_content.getText().toString();
        if (commodity != null)
            chatMessage = new ChatMessage(user_name, content, commodity.get_id());
        else
            chatMessage = new ChatMessage(user_name, content);

        postTask = new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {

                HttpRequest request = ServiceYS.postChatMessage(chatMessage);
                result = request.body();
                switch (request.code()) {
                    case 422:
                        throw new Exception();
                    case 401:
                        return false;
                }

                chatMessage = JSON.parseObject(result, ChatMessage.class);

//                if (chatMessage != null)
                return true;
//                else
//                    return false;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                progressDialogDismiss();
                String str_errors = "";
                if (result != null) {
                    for (Map.Entry<String, Object> obj : JSON.parseObject(result).entrySet()) {
                        str_errors += String.format(format_chat_errors_line, getStringResourceByName(String.format(format_labels, obj.getKey())), obj.getValue());
                    }
                }
//                Throwable cause = e.getCause() != null ? e.getCause() : e;

                Toaster.showLong(ActivityChat.this, str_errors);
            }

            @Override
            public void onSuccess(Boolean authSuccess) {
                if (authSuccess) {
                    Toaster.showLong(ActivityChat.this, "发送成功！");
                    finish();
                } else {
                    start_login();
                }
            }

            @Override
            protected void onFinally() throws RuntimeException {
                progressDialogDismiss();
                postTask = null;
            }
        };
        postTask.execute();
    }

    private void extra_to_view() {
        if (commodity != null) {
            tv_chatable.setText(commodity.getName());
        } else {
            label_chatable.setVisibility(View.GONE);
            tv_chatable.setVisibility(View.GONE);
        }
        if (trader != null) {
            et_user_name.setText(trader.getUser_name());
            et_user_name.setEnabled(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUIWithValidation();
        if (!isLogin()) {
            start_login();
        }
    }

    private void start_login() {
        Toaster.showLong(this, "请先登录！");
        startActivity(new Intent(this, ActivityLogin.class));
    }

    private boolean isLogin() {
        PropertiesController.readConfiguration();
        return Settings.getFactory().isLogin();
    }

    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }
}
