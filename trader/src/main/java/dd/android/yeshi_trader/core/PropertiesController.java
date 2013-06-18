package dd.android.yeshi_trader.core;

import dd.android.common.PropertiesUtil;

import static dd.android.yeshi_trader.core.Constants.Setting.*;


public class PropertiesController {
	public static void writeConfiguration() {
        PropertiesUtil.writeConfiguration(SDCARD_PATH, FILE_NAME, Settings.getFactory());
	}

	public static void readConfiguration() {
        Settings obj = PropertiesUtil.readConfiguration(SDCARD_PATH, FILE_NAME, Settings.class);
        if(obj != null)
            Settings.setFactory(obj);
        else
            Settings.getFactory();
	}
}
