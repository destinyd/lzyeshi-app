package dd.android.yeshi_trader.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import dd.android.yeshi_trader.core.PropertiesController;
import dd.android.yeshi_trader.core.Settings;

/**
 * Created with IntelliJ IDEA.
 * User: dd
 * Date: 13-5-30
 * Time: 下午2:38
 * To change this template use File | Settings | File Templates.
 */
public class ActivityLauncher extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        PropertiesController.readConfiguration();
        if (Settings.getFactory().isLogin())
            startActivity(new Intent(this, ActivityMain.class));
        else
            startActivity(new Intent(this, ActivityLogin.class));
        finish();
    }
}
