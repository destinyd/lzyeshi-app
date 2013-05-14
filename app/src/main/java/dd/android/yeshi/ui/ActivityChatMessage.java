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
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.Map;

import static dd.android.yeshi.core.Constants.Extra.CHAT_MESSAGE;
import static dd.android.yeshi.core.Constants.Extra.COMMODITY;
import static dd.android.yeshi.core.Constants.Extra.TRADER;
//import com.umeng.analytics.MobclickAgent;


public class ActivityChatMessage extends ActivityUserYS {

    @InjectView(R.id.label_content)
    protected TextView label_content;
    @InjectView(R.id.tv_content)
    protected TextView tv_content;

    @InjectView(R.id.label_chatable)
    protected TextView label_chatable;

    @InjectView(R.id.et_chatable)
    protected TextView et_chatable;
    @InjectView(R.id.et_user_name)
    protected EditText et_user_name;
    @InjectView(R.id.et_content)
    protected EditText et_content;
    @InjectView(R.id.btn_submit)
    protected Button btn_submit;

    //    @InjectExtra(COMMODITY)
    protected Commodity commodity = null;

    //    @InjectExtra(TRADER)
    protected Trader trader = null;

    protected ChatMessage chat_message = null;

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

        setContentView(R.layout.act_chat_message);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        get_extra();
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

    private void get_extra() {
        Intent intent = getIntent();
        commodity = (Commodity) intent.getSerializableExtra(COMMODITY);
        trader = (Trader) intent.getSerializableExtra(TRADER);
        chat_message  = (ChatMessage) intent.getSerializableExtra(CHAT_MESSAGE);
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
                try {
                    if (result != null) {
                        for (Map.Entry<String, Object> obj : JSON.parseObject(result).entrySet()) {
                            str_errors += String.format(format_chat_errors_line, getStringResourceByName(String.format(format_labels, obj.getKey())), obj.getValue());
                        }
                    }
                    Toaster.showLong(ActivityChatMessage.this, str_errors);
                } catch (Exception ex) {
                    Toaster.showLong(ActivityChatMessage.this, "发生未知错误");
                }
//                Throwable cause = e.getCause() != null ? e.getCause() : e;

                Toaster.showLong(ActivityChatMessage.this, str_errors);
            }

            @Override
            public void onSuccess(Boolean authSuccess) {
                if (authSuccess) {
                    Toaster.showLong(ActivityChatMessage.this, "发送成功！");
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
            et_chatable.setText(commodity.getName());
        } else {
            label_chatable.setVisibility(View.GONE);
            et_chatable.setVisibility(View.GONE);
        }
        if (trader != null) {
            et_user_name.setText(trader.getUser_name());
            et_user_name.setEnabled(false);
        }
        if (chat_message != null){
            tv_content.setText(chat_message.getContent());
            et_user_name.setText(chat_message.getUser_name());
            if(chat_message.getRead_at() == null){
                read(chat_message);
            }
        }
        else{
            label_content.setVisibility(View.GONE);
            tv_content.setVisibility(View.GONE);
        }
    }

    private void read(ChatMessage chat_message) {
        new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {

                ChatMessage chat_message = ServiceYS.GotChatMessage(chatMessage.get_id());

                if(chat_message != null)
                    return true;
                else
                    return false;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {

            }

            @Override
            public void onSuccess(Boolean authSuccess) {

            }

            @Override
            protected void onFinally() throws RuntimeException {
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUIWithValidation();
        if (!isLogin()) {
            start_login();
        }
    }

    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }
}
