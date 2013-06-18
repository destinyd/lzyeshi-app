
package dd.android.yeshi_trader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.costum.android.widget.LoadMoreListView;
import com.github.kevinsawicki.wishlist.Toaster;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import dd.android.yeshi_trader.R;
import dd.android.yeshi_trader.R.id;
import dd.android.yeshi_trader.core.Group;
import dd.android.yeshi_trader.core.ServiceYS;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.ArrayList;
import java.util.List;

import static dd.android.yeshi_trader.core.Constants.Extra.GROUP;

/**
 * Activity to authenticate the ABUser against an API (example API on Parse.com)
 */
public class FragmentTraderGroups extends
        RoboSherlockFragment {

    @InjectView(id.lv_list)
    private LoadMoreListView lv_list;

    List<Group> groups = new ArrayList<Group>();
    int page = 1;
    static final private int GET_CODE = 0;

    private RoboAsyncTask<Boolean> groupsTask = null;

    AdapterGroups adapter = null;

    static final int CODE_GROUP = 32;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setHasOptionsMenu(true);
        adapter = new AdapterGroups(
                getActivity().getLayoutInflater(), groups);
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
        get_groups();
    }

    private void getNextPage() {
        page++;
        get_groups();
    }

    public void onListItemClick(LoadMoreListView l, View v, int position, long id) {
        Group group = ((Group) l.getItemAtPosition(position));
        startActivityForResult(new Intent(getActivity(), ActivityGroupCommodities.class).putExtra(GROUP, group),CODE_GROUP);
    }

    @Override
    public void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        get_groups();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.trader_groups, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case id.menu_add_group:
                startActivityForResult(new Intent(getActivity(), ActivityFormGroup.class), GET_CODE);
                return true;
            case id.menu_refresh:
                refresh();
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
        if (requestCode == GET_CODE) {
            if (resultCode == getActivity().RESULT_CANCELED) {
            } else {
                if ("success".equals(data.getAction()))
                {
                    refresh();
                }
            }
        }
    }

    private void refresh() {
        page = 1;
        get_groups();
    }

    protected void get_groups() {
        if(groupsTask != null)
            return;
        groups = new ArrayList<Group>();

        getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);

        groupsTask = new RoboAsyncTask<Boolean>(getActivity()) {
            public Boolean call() throws Exception {
                List<Group> get_groups = ServiceYS.getTraderGroups(page);
                if (page > 1 && get_groups != null && get_groups.size() == 0) {
                    page--;
                    lv_list.setOnLoadMoreListener(null);
                    return false;
                } else
                {
                    if(page == 1)
                        groups = get_groups;
                    else
                        groups.addAll(get_groups);
                }
                return true;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                e.printStackTrace();
                Toaster.showLong(getActivity(), "获取商品分组失败。");
                getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                groupsTask = null;
//                setProgressBarVisibility(false);
            }

            @Override
            public void onSuccess(Boolean success) {
                if(success)
                    groups_to_list();
                else
                    Toaster.showLong(getActivity(), "没有数据了。");
            }

            @Override
            protected void onFinally() throws RuntimeException {
                getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                lv_list.onLoadMoreComplete();
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

    private void groups_to_list() {
        adapter.setItems(groups);
        adapter.notifyDataSetChanged();
    }

}
