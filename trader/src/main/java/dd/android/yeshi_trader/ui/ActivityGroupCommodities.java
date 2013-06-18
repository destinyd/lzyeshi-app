
package dd.android.yeshi_trader.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.costum.android.widget.LoadMoreListView;
import com.github.kevinsawicki.wishlist.Toaster;
import com.google.inject.Inject;
import dd.android.yeshi_trader.R;
import dd.android.yeshi_trader.core.Commodity;
import dd.android.yeshi_trader.core.Group;
import dd.android.yeshi_trader.core.PictureImageLoader;
import dd.android.yeshi_trader.core.ServiceYS;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.ArrayList;
import java.util.List;

import static dd.android.yeshi_trader.core.Constants.Extra.COMMODITY;
import static dd.android.yeshi_trader.core.Constants.Extra.GROUP;

/**
 * Activity to authenticate the ABUser against an API (example API on Parse.com)
 */
public class ActivityGroupCommodities extends
        ActivityYS {

    @InjectView(R.id.lv_list)
    private LoadMoreListView lv_list;
    @InjectExtra(GROUP)
    protected Group group;
    @Inject
    private PictureImageLoader avatars;

    List<Commodity> commodities = new ArrayList<Commodity>();
    AdapterCommodities adapter = null;

    int page = 1, pass_page = 0;
    boolean firstPage = true;

    private RoboAsyncTask<Boolean> groupsTask = null;
    static final private int GET_CODE = 0;
    static final String formatDeleteGroupAlert = "你确定删除 %s ？";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.fragment_list);

        adapter = new AdapterCommodities(
                getLayoutInflater(), commodities,
                avatars);

        lv_list.setAdapter(adapter);

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                onListItemClick((LoadMoreListView) parent, view, position, id);
            }
        });
        lv_list.setOnLoadMoreListener(
                new LoadMoreListView.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        getNextPage();
                    }
                }
        );
        get_commodities();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.trader_group_commodities, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refresh();
                return true;
            case R.id.menu_edit_group:
                startActivityForResult(new Intent(this, ActivityFormGroup.class).putExtra(GROUP, group), GET_CODE);
                return true;
            case R.id.menu_add_commodity:
                startActivityForResult(new Intent(this, ActivityFormCommodity.class).putExtra(GROUP, group), GET_CODE);
                return true;
            case R.id.menu_delete_group:
                String tmp = String.format(formatDeleteGroupAlert, group.getName());
                new AlertDialog.Builder(this)
                        .setTitle(tmp)
                        .setNegativeButton("取消", null).setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteGroup();
                            }
                        }).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refresh() {
        page = 1;
        get_commodities();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_CODE) {
            if (resultCode == RESULT_CANCELED) {
            } else {
                if ("success".equals(data.getAction()))
                {
                    return_success();
                    refresh();
                }
            }
        }
    }

    private void getNextPage() {
        page++;
        get_commodities();
    }

    private void get_commodities() {
        if (groupsTask != null)
            return;
//        progressDialogShow(getActivity());
        setSupportProgressBarIndeterminateVisibility(true);
        groupsTask = new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {
                List<Commodity> get_commodities = ServiceYS.getTraderGroupCommodities(group.get_id(), page);
                if (page > 1 && get_commodities != null && get_commodities.size() == 0) {
                    page--;
                    if (page == 1)
                        firstPage = true;
                    lv_list.setOnLoadMoreListener(null);
                    Toaster.showLong(ActivityGroupCommodities.this, "没有数据了。");
                } else {
                    if (page == 1) {
                        commodities.clear();
                    }
                    commodities.addAll(get_commodities);
                }
                return true;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                Toaster.showLong(ActivityGroupCommodities.this, "获取商品信息失败");
                groupsTask = null;
                setSupportProgressBarIndeterminateVisibility(false);
                lv_list.onLoadMoreComplete();
            }

            @Override
            public void onSuccess(Boolean relationship) {
                set_commodities_to_list();
            }

            @Override
            protected void onFinally() throws RuntimeException {
//                progressDialogDismiss();
                lv_list.onLoadMoreComplete();
                setSupportProgressBarIndeterminateVisibility(false);
                groupsTask = null;
            }

//            @Override
//            public boolean cancel(boolean mayInterruptIfRunning) {
//                return super.cancel()
//                return task.cancel(mayInterruptIfRunning);
//            }
        };
        groupsTask.execute();
    }

    public void onListItemClick(LoadMoreListView l, View v, int position, long id) {
        Commodity commodity = ((Commodity) l.getItemAtPosition(position));
        startActivityForResult(new Intent(this, ActivityCommodity.class).putExtra(COMMODITY, commodity),GET_CODE);
    }

    private void set_commodities_to_list() {
        adapter.setItems(commodities);
        adapter.notifyDataSetChanged();
    }

    private void deleteGroup() {
        if (groupsTask != null)
            return;
        setSupportProgressBarIndeterminateVisibility(true);
        groupsTask = new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {
                return ServiceYS.apiDeleteGroup(group);
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                Toaster.showLong(ActivityGroupCommodities.this, "删除商品分组失败。");
                setSupportProgressBarIndeterminateVisibility(false);
                groupsTask = null;
            }

            @Override
            public void onSuccess(Boolean success) {
                if(success)
                {
                    Toaster.showLong(ActivityGroupCommodities.this, "商品分组成功。");
                    return_success();
                    finish();
                }
                else
                    Toaster.showLong(ActivityGroupCommodities.this, "删除商品分组失败");
            }

            @Override
            protected void onFinally() throws RuntimeException {
                setSupportProgressBarIndeterminateVisibility(false);
                groupsTask= null;
            }

//            @Override
//            public boolean cancel(boolean mayInterruptIfRunning) {
//                return super.cancel()
//                return task.cancel(mayInterruptIfRunning);
//            }
        };
        groupsTask.execute();
    }

    private void return_success(){
        setResult(RESULT_OK, (new Intent()).setAction("success"));
    }
}
