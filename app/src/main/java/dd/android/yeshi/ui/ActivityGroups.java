
package dd.android.yeshi.ui;

import android.support.v4.view.ViewPager;
import com.actionbarsherlock.view.Window;
import com.github.kevinsawicki.wishlist.Toaster;
import com.viewpagerindicator.TitlePageIndicator;
import dd.android.yeshi.R;
import dd.android.yeshi.R.id;
import android.os.Bundle;
import dd.android.yeshi.core.Group;
import dd.android.yeshi.core.ServiceYS;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to authenticate the ABUser against an API (example API on Parse.com)
 */
public class ActivityGroups extends
        ActivityYS {

    @InjectView(id.tpi_header)
    private TitlePageIndicator indicator;
    @InjectView(id.vp_pages)
    private ViewPager pager;

    List<Group> groups = new ArrayList<Group>();
    int page = 1,pass_page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.act_groups);

        getGroups();


    }

    protected void getGroups(){
        groups = new ArrayList<Group>();

//        setProgressBarVisibility(true);

        new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {
                List<Group> get_groups = ServiceYS.getGroups(page);
                if(page > 1 && get_groups != null && get_groups.size() == 0)
                {
                    page--;
                }
                else
                    groups.addAll(get_groups);
                return true;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                e.printStackTrace();
                Toaster.showLong(ActivityGroups.this, "获取商品分组失败。");
//                setProgressBarVisibility(false);
            }

            @Override
            public void onSuccess(Boolean relationship) {
                pager.setAdapter(new AdapterYSPager(groups,getResources(), getSupportFragmentManager()));

                indicator.setViewPager(pager);
            }

            @Override
            protected void onFinally() throws RuntimeException {
//                activityGroups.progressDialogDismiss();
//                setProgressBarVisibility(false);
            }

//            @Override
//            public boolean cancel(boolean mayInterruptIfRunning) {
//                return super.cancel()
//                return task.cancel(mayInterruptIfRunning);
//            }
        }.execute();
    }

}
