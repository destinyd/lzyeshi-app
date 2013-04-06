
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
        addTab("Home", R.drawable.tab_home, ActivityGroups.class);
        addTab("衣服", R.drawable.tab_search, ActivitySettings.class);
        addTab("Fake", R.drawable.tab_search, ActivitySettings.class);
        addTab("Home", R.drawable.tab_home, ActivitySettings.class);
        addTab("Search", R.drawable.tab_search, ActivitySettings.class);
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
