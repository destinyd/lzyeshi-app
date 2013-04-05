package dd.android.yeshi.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import dd.android.yeshi.R;

/**
 * @author Adil Soomro
 *
 */
public class CameraActivity extends Activity {
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_settings);
    }
    
    public void takePhotoCamera(View v)
    {
    	Toast.makeText(this, "ohhhhh, That is swearly cooool..\nHow did you do that?? O_o ", Toast.LENGTH_LONG).show();
    }
    public void pickFromGallery(View v) {
		Toast.makeText(this, "That was really awesom, isn't that?", Toast.LENGTH_LONG).show();
	}
}