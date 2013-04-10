package dd.android.yeshi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.actionbarsherlock.view.MenuItem;
import dd.android.yeshi.R;


public class ActivityContact extends ActivityYS {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_contact);

//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void handleLocation(View view){
//        startActivity(new Intent(this,ActivityLocations.class));
        ActivityLauncher.getFactory().changeTab(2);
    }
}
