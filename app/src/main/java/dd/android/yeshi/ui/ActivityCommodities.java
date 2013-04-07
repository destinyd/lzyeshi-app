
package dd.android.yeshi.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.actionbarsherlock.view.Window;
import com.costum.android.widget.LoadMoreListView;
import com.github.kevinsawicki.wishlist.Toaster;
import com.google.inject.Inject;
import dd.android.yeshi.R;
import dd.android.yeshi.core.Commodity;
import dd.android.yeshi.core.PictureImageLoader;
import dd.android.yeshi.core.ServiceYS;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to authenticate the ABUser against an API (example API on Parse.com)
 */
public class ActivityCommodities extends
        ActivityYS {

    List<Commodity> commodities = new ArrayList<Commodity>();

    @InjectView(R.id.lv_list)
    private LoadMoreListView lv_list;
    @Inject
    private PictureImageLoader avatars;
    AdapterCommodities adapter = null;
    int page = 1,pass_page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.act_commodities);

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
        getCommodities();
    }

    private void initProblems() {
        if (commodities == null)
            commodities = new ArrayList<Commodity>();
    }

    private void getNextPage() {
        page++;
        getCommodities();
    }

    private void getCommodities() {
        progressDialogShow(this);
//        setProgressBarVisibility(true);
        new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {
                List<Commodity> get_commodities = ServiceYS.getCommodities(page);
                if (page > 1 && get_commodities != null && get_commodities.size() == 0) {
                    page--;
                    lv_list.setOnLoadMoreListener(null);
                    Toaster.showLong(ActivityCommodities.this, "没有数据了。");
                } else
                    addCommodities(get_commodities);
                return true;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                e.printStackTrace();
                Toaster.showLong(ActivityCommodities.this, "获取商品信息失败");
//                setProgressBarVisibility(false);
            }

            @Override
            public void onSuccess(Boolean relationship) {
                commodities_to_list();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                progressDialogDismiss();
                lv_list.onLoadMoreComplete();
//                setProgressBarVisibility(false);
            }

//            @Override
//            public boolean cancel(boolean mayInterruptIfRunning) {
//                return super.cancel()
//                return task.cancel(mayInterruptIfRunning);
//            }
        }.execute();
    }


    private void addCommodities(List<Commodity> get_commodities) {
        initProblems();
        commodities.addAll(get_commodities);
    }

    public void onListItemClick(LoadMoreListView l, View v, int position, long id) {
        Commodity problem = ((Commodity) l.getItemAtPosition(position));
//        startActivity(new Intent(getActivity(), ActivityProblem.class).putExtra(PROBLEM, problem));
    }

    private void commodities_to_list() {
        if(adapter == null)
        {
            adapter = new AdapterCommodities(
                    getLayoutInflater(), commodities,
                    avatars);
            lv_list.setAdapter(adapter);
        }
        else
        {
            adapter.setItems(commodities);
            adapter.notifyDataSetChanged();
        }
    }

}
