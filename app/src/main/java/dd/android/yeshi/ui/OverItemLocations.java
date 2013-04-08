package dd.android.yeshi.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.github.kevinsawicki.wishlist.Toaster;
import dd.android.yeshi.core.Location;
import dd.android.yeshi.core.ServiceYS;
import roboguice.util.RoboAsyncTask;

import java.util.ArrayList;
import java.util.List;

public class OverItemLocations extends ItemizedOverlay<OverlayItem> {
    private List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
    private Context mContext;
//
//    private double mLat1 = 39.90923;//39.9022; // point1纬度
//    private double mLon1 = 116.397428;//116.3822; // point1经度
//
//    private double mLat2 = 39.9022;
//    private double mLon2 = 116.3922;
//
//    private double mLat3 = 39.917723;
//    private double mLon3 = 116.3722;

    public OverItemLocations(Drawable marker,List<Location> locations){//}, Context context) {
        super(marker);

//        this.mContext = context;

// 用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)
//        GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 * 1E6));
//        GeoPoint p2 = new GeoPoint((int) (mLat2 * 1E6), (int) (mLon2 * 1E6));
//        GeoPoint p3 = new GeoPoint((int) (mLat3 * 1E6), (int) (mLon3 * 1E6));
//
//        GeoList.add(new OverlayItem(p1, "P1", "point1"));
//        GeoList.add(new OverlayItem(p2, "P2", "point2"));
//        GeoList.add(new OverlayItem(p3, "P3", "point3"));
        for(Location location : locations)
        {
            GeoPoint pt = new GeoPoint((int)(location.getLat() * 1E6),(int)(location.getLng() * 1E6));
            GeoList.add(new OverlayItem(pt, "摊位位置", "商家前5次成功定位地点"));
        }
        populate(); //createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
    }

    @Override
    protected OverlayItem createItem(int i) {
        return GeoList.get(i);
    }

    @Override
    public int size() {
        return GeoList.size();
    }

    @Override
// 处理当点击事件
    protected boolean onTap(int i) {
//        Toaster.showLong(this.mContext, GeoList.get(i).getSnippet(),
//                Toast.LENGTH_SHORT).show();
        return true;
    }

}
