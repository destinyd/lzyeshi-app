package dd.android.yeshi.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import com.baidu.location.*;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.github.kevinsawicki.wishlist.Toaster;
import dd.android.yeshi.R;
import dd.android.yeshi.core.Commodity;
import dd.android.yeshi.core.Location;
import dd.android.yeshi.core.ServiceYS;
import dd.android.yeshi.core.Trader;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.ArrayList;
import java.util.List;

import static dd.android.yeshi.core.Constants.Extra.TRADER;

public class ActivityTraderLocations extends ActivityYS {
    @InjectView(R.id.bmapView)
    protected MapView mMapView;

    private MapController mMapController = null;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    MyLocationOverlay myLocationOverlay = null;
    LocationData locData = null;

    List<Location> locations = new ArrayList<Location>();

    @InjectExtra(TRADER)
    protected Trader trader;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_locations);

        mMapController = mMapView.getController();

        mMapView.getController().setZoom(16);

        mMapView.displayZoomControls(true);

        bindLocClient();


        get_locations();

    }

    private void bindLocClient() {
        new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {
                return true;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
            }

            @Override
            public void onSuccess(Boolean relationship) {
                mLocClient = new LocationClient(ActivityTraderLocations.this);
                mLocClient.registerLocationListener(myListener);

                LocationClientOption option = new LocationClientOption();
                option.setOpenGps(true);//打开gps
                option.setCoorType("bd09ll");     //设置坐标类型
                option.setScanSpan(30000);//5s for test
                option.setAddrType("all");
                option.setPriority(LocationClientOption.GpsFirst);
                mLocClient.setLocOption(option);
                mLocClient.start();
                myLocationOverlay = new MyLocationOverlay(mMapView);
            }
        }.execute();

    }


    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMapView.onRestoreInstanceState(savedInstanceState);
    }


    /**
     * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;

            LocationData tmp_loc_data = new LocationData();

            tmp_loc_data.latitude = location.getLatitude();
            tmp_loc_data.longitude = location.getLongitude();
            tmp_loc_data.direction = 2.0f;
            tmp_loc_data.accuracy = location.getRadius();
            tmp_loc_data.direction = location.getDerect();

            Log.d("loctest", String.format("before: lat: %f lon: %f", location.getLatitude(), location.getLongitude()));

            set_mylocation(tmp_loc_data);
        }

        public void onReceivePoi(BDLocation poiLocation) {
//            if (poiLocation == null) {
//                return;
//            }
        }
    }

    protected void set_mylocation(LocationData p_locData){
        if(p_locData != null && p_locData.latitude != 0.0 && p_locData.longitude != 0.0 && p_locData.latitude != 4.9E-324 && p_locData.longitude != 4.9E-324){
            if(locData == null){
                locData = p_locData;
                myLocationOverlay.setData(locData);
                mMapView.getOverlays().add(myLocationOverlay);
                myLocationOverlay.enableCompass();
                mMapView.refresh();
            }
            else{
                Log.e("new my location", String.format(" %s, %s", p_locData.latitude, p_locData.longitude));
                locData = p_locData;
                myLocationOverlay.setData(locData);
                mMapView.refresh();
            }
        }
    }

    void  get_locations(){
        progressDialogShow();
        new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {
                List<Location> get_locations = ServiceYS.getTraderLocations(trader.get_id());
                if(get_locations != null && get_locations.size() > 0){
                    locations.addAll(get_locations);
                }
                return true;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                e.printStackTrace();
                Toaster.showLong(ActivityTraderLocations.this, "获取商家定位失败。");
            }

            @Override
            public void onSuccess(Boolean relationship) {
                locations_to_view();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                progressDialogDismiss();
            }

        }.execute();
    }

    private void locations_to_view() {
        Drawable marker = getResources().getDrawable(R.drawable.icon_marka);
        mMapView.getOverlays().add(new OverItemLocations(marker, locations)); //添加ItemizedOverlay实例到mMapView
        mMapView.refresh();//刷新地图
        if(locations != null && locations.size() > 0){
            Location location = locations.get(0);
            GeoPoint pt = new GeoPoint((int)(location.getLat() * 1E6),(int)(location.getLng() * 1E6));
            mMapController.animateTo(pt);//, mHandler.obtainMessage(1));
        }
    }

}

