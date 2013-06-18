
package dd.android.yeshi_trader.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import com.actionbarsherlock.view.Window;
import com.github.kevinsawicki.wishlist.Toaster;
import dd.android.yeshi_trader.R;
import dd.android.yeshi_trader.core.PropertiesController;
import dd.android.yeshi_trader.core.Settings;

import java.util.ArrayList;

public class ActivityMain extends
        ActivityTabYS {
//    @InjectView(android.R.id.tabhost)
    TabHost mTabHost;
//    @InjectView(android.R.id.tabs)
    TabWidget mTabWidget;

    ViewPager mViewPager;
    TabsAdapter mTabsAdapter;

    static public ActivityMain factory = null;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.main);
        factory = this;

        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabWidget = (TabWidget)findViewById(android.R.id.tabs);

        mViewPager = (ViewPager)findViewById(R.id.pager);

        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager, mTabWidget);

        mTabsAdapter.addTab(mTabHost.newTabSpec("面板"),
                FragmentDashboard.class, null, "面板", R.drawable.tab_home);
        mTabsAdapter.addTab(mTabHost.newTabSpec("帐目"),
                FragmentAccounts.class, null, "帐目", R.drawable.tab_accounting);
        mTabsAdapter.addTab(mTabHost.newTabSpec("商品分组"),
                FragmentTraderGroups.class, null, "商品", R.drawable.tab_groups);
        mTabsAdapter.addTab(mTabHost.newTabSpec("设置"),
                FragmentSettings.class, null, "设置", R.drawable.tab_settings);

        if (bundle != null) {
            mTabHost.setCurrentTabByTag(bundle.getString("tab"));
        }

    }

    private void exit() {
//        MobclickAgent.onKillProcess(ActivityMain.this);
        System.exit(0);
    }

    @Override
    protected void onDestroy() {
        factory = null;
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

    public static class TabsAdapter extends FragmentPagerAdapter
            implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final Activity mActivity;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final TabWidget mTabWidget;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;

            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager, TabWidget tabWidget) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mActivity = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
            mTabWidget = tabWidget;
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args, String labelId, int drawableId) {
            String tag = tabSpec.getTag();
            tabSpec.setContent(new DummyTabFactory(mContext));

            drawTab(tabSpec, labelId, drawableId);

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        private void drawTab(TabHost.TabSpec tabSpec, String labelId, int drawableId) {
            View tabIndicator = LayoutInflater.from(mContext).inflate(R.layout.tab_indicator, mTabWidget, false);
            TextView title = (TextView) tabIndicator.findViewById(R.id.title);
            title.setText(labelId);
            ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
            icon.setImageResource(drawableId);
            tabSpec.setIndicator(tabIndicator);
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }

        @Override
        public void onTabChanged(String tabId) {
            int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
            String tag = mTabs.get(position).tag;
            mActivity.setTitle(tag);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            // Unfortunately when TabHost changes the current tab, it kindly
            // also takes care of putting focus on it when not in touch mode.
            // The jerk.
            // This hack tries to prevent this from pulling focus out of our
            // ViewPager.
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
