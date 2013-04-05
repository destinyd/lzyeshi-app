package dd.android.yeshi.ui;

import android.app.Activity;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.search.MKPoiInfo;
import com.github.kevinsawicki.wishlist.Toaster;

public class PoiOverlayFC extends PoiOverlay {

    Activity activity;

    public PoiOverlayFC(Activity activity, MapView mapView) {
        super(activity, mapView);
        this.activity = activity;
    }

    @Override
    protected boolean onTap(int i) {
        MKPoiInfo info = getPoi(i);
        Toaster.showLong(
                this.activity,
                info.name
        );
        return true;
    }
}
