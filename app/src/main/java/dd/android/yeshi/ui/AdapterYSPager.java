

package dd.android.yeshi.ui;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.github.kevinsawicki.wishlist.Toaster;
import dd.android.yeshi.R;
import dd.android.yeshi.core.Group;
import dd.android.yeshi.core.ServiceYS;
import roboguice.util.RoboAsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static dd.android.yeshi.core.Constants.Extra.GROUP;

public class AdapterYSPager extends FragmentPagerAdapter {

    private final Resources resources;

    List<Group> groups;

    /**
     * Create pager adapter
     *
     * @param resources
     * @param fragmentManager
     */
    public AdapterYSPager(List<Group> p_groups, Resources resources, FragmentManager fragmentManager) {
        super(fragmentManager);
        groups = p_groups;
        this.resources = resources;


    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Fragment getItem(int position) {
        if(groups.get(position) != null){
            Group group = groups.get(position);
            FragmentCommodities fragmentCommodities = new FragmentCommodities(
//                    groups.get(position)
            );
            Bundle bundle = new Bundle();
            bundle.putSerializable(GROUP,group);
            fragmentCommodities.setArguments(bundle);
            return fragmentCommodities;
        }
        else
            return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(groups.get(position) != null)
            return groups.get(position).getName();
        return null;
    }

}
