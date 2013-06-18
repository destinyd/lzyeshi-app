package dd.android.yeshi_trader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.github.kevinsawicki.wishlist.Toaster;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import dd.android.yeshi_trader.R;
import dd.android.yeshi_trader.core.Accounts;
import dd.android.yeshi_trader.core.OnlineSettings;
import dd.android.yeshi_trader.core.ServiceYS;
import dd.android.yeshi_trader.core.Settings;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

public class FragmentAccounts extends RoboSherlockFragment {


    @InjectView(R.id.tv_income_yesterday)
    protected TextView tv_income_yesterday;
    @InjectView(R.id.tv_income_today)
    protected TextView tv_income_today;
    @InjectView(R.id.tv_income_month)
    protected TextView tv_income_month;
    @InjectView(R.id.tv_income_year)
    protected TextView tv_income_year;


    private RoboAsyncTask<Boolean> task;

    Accounts accounts;


    @Override
    public void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        get_accounts();
    }

    public void get_accounts() {
        if (task != null) {
            return;
        }
        getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);

        task = new RoboAsyncTask<Boolean>(getActivity()) {
            public Boolean call() throws Exception {
                accounts = ServiceYS.apiBillDashboard();
                return true;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                Toaster.showLong(getActivity(), "获取帐目失败,请检查网络状态。");
                getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                task = null;
            }

            @Override
            public void onSuccess(Boolean relationship) {
                accounts_to_view();
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

    private void accounts_to_view() {
        tv_income_yesterday.setText(String.valueOf(accounts.getIncome_yesterday()));
        tv_income_today.setText(String.valueOf(accounts.getIncome_today()));
        tv_income_month.setText(String.valueOf(accounts.getIncome_month()));
        tv_income_year.setText(String.valueOf(accounts.getIncome_year()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.act_accounts, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        get_accounts();
    }
}
