
package dd.android.yeshi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import dd.android.yeshi.R;
import roboguice.activity.RoboTabActivity;

public class ActivityLauncher extends
        RoboTabActivity {
    TabHost tabHost;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);

        tabHost = getTabHost();
        setTabs();
    }
    private void setTabs()
    {
        addTab("最新", R.drawable.tab_home, ActivityCommodities.class);
        addTab("分类", R.drawable.tab_groups, ActivityGroups.class);
//        addTab("购物车", R.drawable.tab_search, ActivitySettings.class);
        addTab("定位", R.drawable.tab_location, ActivitySettings.class);
        addTab("联系", R.drawable.tab_contact, ActivityContact.class);
        addTab("设置", R.drawable.tab_settings, ActivitySettings.class);
    }
    private void addTab(String labelId, int drawableId, Class<?> c)
    {
        Intent intent = new Intent(this, c);
        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(labelId);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);
        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);
    }
    public void openCameraActivity(View b)
    {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }


    private void exit() {
//        MobclickAgent.onKillProcess(ActivityLauncher.this);
        System.exit(0);
    }


}
