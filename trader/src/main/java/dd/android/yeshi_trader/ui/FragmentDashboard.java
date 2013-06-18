package dd.android.yeshi_trader.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.github.kevinsawicki.wishlist.Toaster;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import dd.android.yeshi_trader.R;
import dd.android.yeshi_trader.core.BusinessHour;
import dd.android.yeshi_trader.core.OnlineSettings;
import dd.android.yeshi_trader.core.ServiceYS;
import dd.android.yeshi_trader.core.Settings;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.Date;

public class FragmentDashboard extends RoboSherlockFragment {

    @InjectView(R.id.tb_isOpen)
    public ToggleButton tb_isOpen;

    @InjectView(R.id.tv_income_today)
    public TextView tv_income_today;
    @InjectView(R.id.tv_income_month)
    public TextView tv_income_month;
    private RoboAsyncTask<Boolean> task = null;

    Boolean setOpen = false;

    public static FragmentDashboard factroy;

    static final int CODE_LOCATION = 21;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        factroy = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.act_dashboard, container, false);
        return v;
    }

    private Settings f() {
        return Settings.getFactory();
    }


    @Override
    public void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        get_online_settings();
    }

    private void online_settings_to_view() {
        tv_income_today.setText(String.valueOf(OnlineSettings.getFactory().getIncome_today()));
        tv_income_month.setText(String.valueOf(OnlineSettings.getFactory().getIncome_month()));
        //To change body of created methods use File | Settings | File Templates.
    }

    private void check_tb() {
        if(Settings.getFactory().isLogin()){
            tb_isOpen.setChecked(OnlineSettings.getFactory().isOpen());
        }
    }

    public void handleTbChange(View view) {
        ToggleButton tb = (ToggleButton) view;
        tb.setEnabled(false);
//        tb_isOpen.setEnabled(false);
        Boolean checked = tb.isChecked();
        if (checked) {
            startActivityForResult(new Intent(getActivity(), ActivityLocationSelect.class), CODE_LOCATION);
        } else {
            post_business_hours(checked);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CODE_LOCATION){
            if(resultCode == Activity.RESULT_OK)
            {
                tb_isOpen.setChecked(true);
                post_business_hours(true);
            }
            else
            {
                tb_isOpen.setChecked(false);
                tb_isOpen.setEnabled(true);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void post_business_hours(Boolean p_setOpen) {
        if (task != null) {
            return;
        }
        setOpen = p_setOpen;

        getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);

        task = new RoboAsyncTask<Boolean>(getActivity()) {
            public Boolean call() throws Exception {
                OnlineSettings.setFactory(ServiceYS.postBusinessHour(setOpen));
                return true;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                Toaster.showLong(getActivity(), "提交店铺状态失败,请检查网络状态。");
                tb_isOpen.setEnabled(true);
                getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                task = null;
            }

            @Override
            public void onSuccess(Boolean relationship) {
                check_tb();
                online_settings_to_view();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                task = null;
                tb_isOpen.setEnabled(true);
                getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
            }

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return task.cancel(mayInterruptIfRunning);
            }
        };
        task.execute();
    }

    public void get_online_settings() {
        if (task != null) {
            return;
        }

        getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);

        task = new RoboAsyncTask<Boolean>(getActivity()) {
            public Boolean call() throws Exception {
                OnlineSettings.setFactory(ServiceYS.getOnlineSettings());
                return true;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                Toaster.showLong(getActivity(), "获取个人信息失败,请检查网络状态。");
                getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                task = null;
            }

            @Override
            public void onSuccess(Boolean relationship) {
                tb_isOpen.setChecked(OnlineSettings.getFactory().isOpen());
                check_tb();
                online_settings_to_view();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                task = null;
            }

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return task.cancel(mayInterruptIfRunning);
            }
        };
        task.execute();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        tb_isOpen.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handleTbChange(view);
                    }
                }
        );
        get_online_settings();
    }
}
