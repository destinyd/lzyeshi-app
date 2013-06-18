package dd.android.yeshi_trader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.github.kevinsawicki.wishlist.Toaster;
import com.google.inject.Inject;
import dd.android.yeshi_trader.R;
import dd.android.yeshi_trader.core.*;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import static dd.android.yeshi_trader.core.Constants.Extra.COMMODITY;
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


    @InjectExtra(COMMODITY)
    protected Commodity commodity;

    @Inject
    private PictureImageLoader avatars;

    Group group = null;
    Trader trader = null;
    static final private int CODE_ACCOUNTING = 21, CODE_EDIT_COMMODITY = 22;
    private RoboAsyncTask<Boolean> task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.act_commodity);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        commodity_to_view();
        get_group_and_trader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.commodity, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refresh_commodity();
                return true;
            case R.id.menu_accounting:
                intent = new Intent(this,ActivityAccounting.class).putExtra(COMMODITY,commodity);
                startActivityForResult(intent, CODE_ACCOUNTING);
                return true;
            case R.id.menu_edit_commodity:
                intent = new Intent(this,ActivityFormCommodity.class).putExtra(COMMODITY,commodity);
                startActivityForResult(intent, CODE_EDIT_COMMODITY);
                return true;
            case R.id.menu_delete_commodity:
                delete_commodity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * This method is called when the sending activity has finished, with the
     * result it supplied.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // You can use the requestCode to select between multiple child
        // activities you may have started.  Here there is only one thing
        // we launch.
        switch (requestCode) {
            case CODE_ACCOUNTING:
                if (resultCode == RESULT_OK) {
                    Integer quantity = Integer.parseInt(data.getAction());
                    commodity.setReserve(commodity.getReserve() - quantity);
                    tv_reserve.setText(String.valueOf(commodity.getReserve()));
                }
                break;
            case CODE_EDIT_COMMODITY:
                if (resultCode == RESULT_OK) {
                    if("success".equals(data.getAction()))
                        refresh_commodity();
                }
                break;

        }
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

    private void refresh_commodity() {
        if(task != null)
            return;
        task = new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {
                Commodity p_commodity = ServiceYS.apiCommodity(commodity.get_id());
                if (p_commodity != null) {
                    commodity = p_commodity;
                    return true;
                }
                else{
                    return false;
                }
            }

            @Override
            protected void onPreExecute() throws Exception {
                super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
                show_progress();
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                Toaster.showLong(ActivityCommodity.this, "获取详细资料失败。");
                hide_progress();
            }

            @Override
            public void onSuccess(Boolean success) {
                if(success)
                {
                    commodity_to_view();
                }
                else
                    Toaster.showLong(ActivityCommodity.this, "你不拥有此商品。");
            }

            @Override
            protected void onFinally() throws RuntimeException {
                hide_progress();
            }
        };
        task.execute();
    }

    private void hide_progress() {
        setSupportProgressBarIndeterminateVisibility(false);
        progressDialogDismiss();
        task = null;
    }

    private void show_progress() {
        setSupportProgressBarIndeterminateVisibility(true);
        progressDialogShow();
    }

    protected void get_group_and_trader() {
        if(task != null)
            return;

        task = new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {
                group = ServiceYS.getGroup(commodity.group_id);
                trader = ServiceYS.getTrader(commodity.trader_id);
                return true;
            }

            @Override
            protected void onPreExecute() throws Exception {
                super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
                show_progress();
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                Toaster.showLong(ActivityCommodity.this, "获取详细资料失败。");
                hide_progress();
            }

            @Override
            public void onSuccess(Boolean relationship) {
                group_to_view();
                trader_to_view();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                hide_progress();
            }
//            @Override
//            public boolean cancel(boolean mayInterruptIfRunning) {
//                return super.cancel()
//                return task.cancel(mayInterruptIfRunning);
//            }
        };
        task.execute();
    }


    protected void delete_commodity() {
        if(task != null)
            return;

        task = new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {
                return ServiceYS.deleteCommodity(commodity.get_id());
            }

            @Override
            protected void onPreExecute() throws Exception {
                super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
                show_progress();
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                Toaster.showLong(ActivityCommodity.this, "删除商品失败。");
                hide_progress();
            }

            @Override
            public void onSuccess(Boolean success) {
                if(success)
                {
                    setResult(RESULT_OK, (new Intent()).setAction("success"));
                    finish();
                }
                else
                    Toaster.showLong(ActivityCommodity.this, "删除商品失败。");
            }

            @Override
            protected void onFinally() throws RuntimeException {
                hide_progress();
            }
//            @Override
//            public boolean cancel(boolean mayInterruptIfRunning) {
//                return super.cancel()
//                return task.cancel(mayInterruptIfRunning);
//            }
        };
        task.execute();
    }
}
