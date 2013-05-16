package dd.android.yeshi.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;
import dd.android.yeshi.R;
import android.os.Bundle;
import android.view.View;
import dd.android.yeshi.core.PropertiesController;
import dd.android.yeshi.core.Settings;
import roboguice.inject.InjectView;
//import com.umeng.analytics.MobclickAgent;


public class ActivitySettings extends ActivityYS {

    @InjectView(R.id.ll_login)
    protected LinearLayout ll_login;
    @InjectView(R.id.ll_userinfo)
    protected LinearLayout ll_userinfo;
    @InjectView(R.id.tv_name)
    protected TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_settings);

        set_ll();
    }

    private void set_ll() {
        if(Settings.getFactory().isLogin())
        {
            ll_login.setVisibility(View.GONE);
            ll_userinfo.setVisibility(View.VISIBLE);
            tv_name.setText(Settings.getFactory().getName());
        }
        else{
            ll_login.setVisibility(View.VISIBLE);
            ll_userinfo.setVisibility(View.GONE);
        }
    }

    public void handleLogin(View v){
        startActivity(new Intent(this, ActivityLogin.class));
    }

    public void handleReg(View v){
        startActivity(new Intent(this, ActivityReg.class));
    }

    public void handleLogout(View v) {
        new AlertDialog.Builder(this).setTitle("登出")
                .setMessage("你确定要登出吗？")
                .setNegativeButton("取消", null).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                }).show();
    }

    private void logout() {
        Settings.getFactory().setAuthToken(null);
        Settings.getFactory().setUser(null);
        PropertiesController.writeConfiguration();
        set_ll();
    }

    public void handleSendChatMessage(View v){
        startActivity(new Intent(this, ActivityChatMessage.class));
    }

    public void handleChatMessages(View v){
        startActivity(new Intent(this, ActivityChatMessages.class));
    }

    public void handleExit(View v){
        System.exit(0);
    }

    @Override
    protected void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        set_ll();
//        MobclickAgent.onResume(this);
    }
}
