
package dd.android.yeshi.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.costum.android.widget.LoadMoreListView;
import com.github.kevinsawicki.wishlist.Toaster;
import com.google.inject.Inject;
import dd.android.yeshi.R;
import dd.android.yeshi.core.Commodity;
import dd.android.yeshi.core.Group;
import dd.android.yeshi.core.PictureImageLoader;
import dd.android.yeshi.core.ServiceYS;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.ArrayList;
import java.util.List;

import static dd.android.yeshi.core.Constants.Extra.GROUP;

/**
 * Activity to authenticate the ABUser against an API (example API on Parse.com)
 */
public class FragmentCommodities extends
        FragmentYS {
    @InjectView(R.id.lv_list)
    private LoadMoreListView lv_list;
//    @InjectExtra(GROUP)
    protected Group group;
    @Inject
    private PictureImageLoader avatars;

    List<Commodity> commodities = null;

    int page = 1, pass_page = 0;

    @Override
    public void setArguments(Bundle args) {
        group = (Group)args.getSerializable(GROUP);
//        super.setArguments(args);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_commodities, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
    }
    private void initProblems() {
        if (commodities == null)
            commodities = new ArrayList<Commodity>();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
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

    private void getNextPage() {
        page++;
        getCommodities();
    }

    private void getCommodities() {
        progressDialogShow(getActivity());
        getActivity().setProgressBarVisibility(true);
        new RoboAsyncTask<Boolean>(getActivity()) {
            public Boolean call() throws Exception {
                List<Commodity> get_commodities = ServiceYS.getCommodities("515a7cf351e4a15a0000006e", page);
                if (page > 1 && get_commodities != null && get_commodities.size() == 0) {
                    page--;
                } else
                    addCommodities(get_commodities);
                return true;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                e.printStackTrace();
                Toaster.showLong(getActivity(), "获取商品信息失败");
                getActivity().setProgressBarVisibility(false);
            }

            @Override
            public void onSuccess(Boolean relationship) {
                initListData(commodities);
            }

            @Override
            protected void onFinally() throws RuntimeException {
                progressDialogDismiss();
                getActivity().setProgressBarVisibility(false);
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

    private void initListData(List<Commodity> requests) {
        AdapterCommodities adapter = new AdapterCommodities(
                getActivity().getLayoutInflater(), requests,
                avatars);
        lv_list.setAdapter(adapter);
    }

}
