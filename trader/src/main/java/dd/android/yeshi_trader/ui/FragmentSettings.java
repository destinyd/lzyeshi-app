package dd.android.yeshi_trader.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import dd.android.yeshi_trader.R;
import dd.android.yeshi_trader.core.PropertiesController;
import dd.android.yeshi_trader.core.Settings;
import roboguice.inject.InjectView;
//import com.umeng.analytics.MobclickAgent;


public class FragmentSettings extends RoboSherlockFragment {

    @InjectView(R.id.ll_login)
    protected LinearLayout ll_login;
    @InjectView(R.id.ll_userinfo)
    protected LinearLayout ll_userinfo;
    @InjectView(R.id.tv_name)
    protected TextView tv_name;

    @InjectView(R.id.rl_exit)
    protected RelativeLayout rl_exit;
    @InjectView(R.id.rl_login)
    protected RelativeLayout rl_login;
    @InjectView(R.id.rl_reg)
    protected RelativeLayout rl_reg;
    @InjectView(R.id.rl_logout)
    protected RelativeLayout rl_logout;
    @InjectView(R.id.rl_messages)
    protected RelativeLayout rl_messages;
    @InjectView(R.id.rl_send_message)
    protected RelativeLayout rl_send_message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.act_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        bind_handles();
        set_ll();
    }

    private void bind_handles() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.rl_exit:
                        handleExit(view);
                        break;
                    case R.id.rl_login:
                        handleLogin(view);
                        break;
                    case R.id.rl_reg:
                        handleReg(view);
                        break;
                    case R.id.rl_logout:
                        handleLogout(view);
                        break;
                    case R.id.rl_messages:
                        handleChatMessages(view);
                        break;
                    case R.id.rl_send_message:
                        handleSendChatMessage(view);
                        break;

                }
            }
        };
        rl_exit.setOnClickListener(listener);
        rl_login.setOnClickListener(listener);
        rl_reg.setOnClickListener(listener);
        rl_logout.setOnClickListener(listener);
        rl_messages.setOnClickListener(listener);
        rl_send_message.setOnClickListener(listener);
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
        startActivity(new Intent(getActivity(), ActivityLogin.class));
    }

    public void handleReg(View v){
        startActivity(new Intent(getActivity(), ActivityTraderReg.class));
    }

    public void handleLogout(View v) {
        new AlertDialog.Builder(getActivity()).setTitle("登出")
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
        startActivity(new Intent(getActivity(), ActivityLogin.class));
        getActivity().finish();
    }

    public void handleSendChatMessage(View v){
        startActivity(new Intent(getActivity(), ActivityChatMessage.class));
    }

    public void handleChatMessages(View v){
        startActivity(new Intent(getActivity(), ActivityChatMessages.class));
    }


    public void handleExit(View v) {
        new AlertDialog.Builder(getActivity())
                .setTitle("确定退出")
                .setNegativeButton("取消", null).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .create().show();
    }

    @Override
    public void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
//        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        set_ll();
//        MobclickAgent.onResume(this);
    }
}
