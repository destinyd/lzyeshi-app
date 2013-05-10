package dd.android.yeshi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.kevinsawicki.wishlist.Toaster;
import com.google.inject.Inject;
import dd.android.yeshi.R;
import dd.android.yeshi.core.*;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import static dd.android.yeshi.core.Constants.Extra.COMMODITY;
import static dd.android.yeshi.core.Constants.Extra.TRADER;
//import com.umeng.analytics.MobclickAgent;


public class ActivityCommodity extends ActivityYS {

    @InjectView(R.id.iv_image)
    protected ImageView iv_image;
    @InjectView(R.id.tv_name)
    protected TextView tv_name;
    @InjectView(R.id.tv_price)
    protected TextView tv_price;
    @InjectView(R.id.tv_reserve)
    protected TextView tv_reserve;
    @InjectView(R.id.tv_desc)
    protected TextView tv_desc;

    @InjectView(R.id.tv_group_name)
    protected TextView tv_group_name;
    
    @InjectView(R.id.tv_trader_name)
    protected TextView tv_trader_name;

    @InjectView(R.id.tv_location)
    protected TextView tv_location;
    @InjectView(R.id.tv_message)
    protected TextView tv_message;


    @InjectExtra(COMMODITY)
    protected Commodity commodity;

    @Inject
    private PictureImageLoader avatars;

    Group group = null;
    Trader trader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_commodity);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        commodity_to_view();
        get_group_and_trader();

        bind_click_handler();
    }

    private void bind_click_handler() {
        View.OnClickListener vcl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv_location.equals(view))
                    startActivity(new Intent(ActivityCommodity.this,ActivityTraderLocations.class).putExtra(TRADER,trader));
                else if(tv_message.equals(view))
                    startActivity(new Intent(ActivityCommodity.this,ActivityChat.class).putExtra(TRADER,trader).putExtra(COMMODITY, commodity));
            }
        };

        tv_location.setOnClickListener(vcl);
        tv_message.setOnClickListener(vcl);
    }

    private void commodity_to_view() {
        avatars.bind(iv_image, commodity.getPicture(), "android");
        tv_name.setText(commodity.getName());
        tv_price.setText(commodity.getHumanize_price());
        tv_reserve.setText(String.valueOf(commodity.getReserve()));
        tv_desc.setText(commodity.getDesc());
    }

    private void group_to_view() {
        if (group != null) {
            tv_group_name.setText(group.getName());
        }
    }

    private void trader_to_view() {
        if (trader != null) {
            tv_trader_name.setText(trader.getName());
        }
    }

    protected void get_group_and_trader() {
        new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {
                group = ServiceYS.getGroup(commodity.group_id);
                trader = ServiceYS.getTrader(commodity.trader_id);
                return true;
            }

            @Override
            protected void onPreExecute() throws Exception {
                super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
                progressDialogShow();
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                e.printStackTrace();
                Toaster.showLong(ActivityCommodity.this, "获取详细资料失败。");
//                setProgressBarVisibility(false);
            }

            @Override
            public void onSuccess(Boolean relationship) {
                group_to_view();
                trader_to_view();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                progressDialogDismiss();
            }
//            @Override
//            public boolean cancel(boolean mayInterruptIfRunning) {
//                return super.cancel()
//                return task.cancel(mayInterruptIfRunning);
//            }
        }.execute();
    }
}
