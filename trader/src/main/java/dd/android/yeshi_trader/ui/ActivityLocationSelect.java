package dd.android.yeshi_trader.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.github.kevinsawicki.wishlist.Toaster;
import dd.android.yeshi_trader.R;
import dd.android.yeshi_trader.ApplicationYS;
import dd.android.yeshi_trader.core.*;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

//import com.umeng.analytics.MobclickAgent;
import static dd.android.yeshi_trader.core.Constants.Other.*;

public class ActivityLocationSelect extends ActivityYS {
    @InjectView(R.id.bmapView)
    protected MapView mMapView;

    final String TAG = "ActivityLocationSelect";

//    static MapView mMapView = null;

    private MapController mMapController = null;

    public MKMapViewListener mMapListener = null;
    FrameLayout mMapViewContainer = null;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
//    public NotifyLister mNotifyer = null;

    MyLocationOverlay myLocationOverlay = null;
    int index = 0;
    LocationData locData = null;

//    Location location = new Location();

    OverlayItemSelectLocation OverlayItemSelect = null;

//    Handler mHandler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
////            Toast.makeText(ActNewActivityBaiduMap.this, "msg:" +msg.what, Toast.LENGTH_SHORT).show();
//        }
//
//        ;
//    };

//    PoiOverlay poi_overlay;
//    ZhaohaiPoiOverlay poi_overlay;// = new ZhaohaiPoiOverlay(this, mMapView, mSearch);

//    TimerTask task = null;// new TimerTask() {
//    //        public void run() {
////            SuggestionSearchButtonProcess(actv_q);//每次需要执行的代码放到这里面。
////        }
////    };
//    java.util.Timer timer = new java.util.Timer(true);


    Drawable marker;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_locations);

//        mMapView = (MapView)findViewById(R.id.bmapView);

        mMapController = mMapView.getController();

        initMapView();

        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);

        //位置提醒相关代码
//        mNotifyer = new NotifyLister();
//        mNotifyer.SetNotifyLocation(42.03249652949337, 113.3129895882556, 3000, "bd09ll");//4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)
//        mLocClient.registerNotify(mNotifyer);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");     //设置坐标类型
//        option.setScanSpan(60000);//5s for test
        option.setScanSpan(5000);//5s for test
        option.setAddrType("all");
        option.setPriority(LocationClientOption.GpsFirst);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mMapView.getController().setZoom(15);
        mMapView.getController().enableClick(true);

        mMapView.displayZoomControls(true);


        marker = this.getResources().getDrawable(R.drawable.icon_marka);
        OverlayItemSelect = new OverlayItemSelectLocation(marker, this, mMapView);
        mMapView.getOverlays().add(OverlayItemSelect);

        mMapListener = new MKMapViewListener() {

            @Override
            public void onMapMoveFinish() {
                Log.e(TAG, "onMapMoveFinish");
                set_select();
                mMapView.refresh();
            }


            //地图点击
            @Override
            public void onClickMapPoi(MapPoi mapPoiInfo) {
            }

            @Override
            public void onGetCurrentMap(Bitmap bitmap) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onMapAnimationFinish() {
                Log.e(TAG, "onMapAnimationFinish");
                set_select();
                mMapView.refresh();
            }
        };
        mMapView.regMapViewListener(ApplicationYS.getInstance().mBMapManager, mMapListener);
        set_select();
        myLocationOverlay = new MyLocationOverlay(mMapView);
        set_mylocation(new LocationData());
////        if(locData.latitude !=0.0 && locData.longitude !=0.0){
//        myLocationOverlay.setData(locData);
//        mMapView.getOverlays().add(myLocationOverlay);
//        myLocationOverlay.enableCompass();
//        mMapView.refresh();

//        poi_overlay = new PoiOverlay(this, mMapView);
//        poi_overlay = new ZhaohaiPoiOverlay(this, mMapView, mSearch);
//
//        mMapView.getOverlays().add(poi_overlay);
//        AdapterView.OnItemSelectedListener selected_listener = new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                //To change body of implemented methods use File | Settings | File Templates.
//                String full_address = adapterView.getItemAtPosition(i).toString();
////                mSearch.poiSearchInCity("柳州",full_address);
////                mSearch.poiDetailSearch(full_address);
//                mSearch.poiSearchInCity("柳州", "100");
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        };
        Toast.makeText(this, "移动地图选择所在地点，点击图中心图标选定。" ,Toast.LENGTH_LONG).show();

    }

    private void set_select() {
        OverlayItemSelect.removeAll();
        OverlayItem oi = new OverlayItem(mMapView.getMapCenter(),"再次点击以此作为您的摊位坐标","");
        OverlayItemSelect.addItem(oi);
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
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
////
//    public void testUpdateClick() {
//        mLocClient.requestLocation();
//    }

    private void initMapView() {
        mMapView.setLongClickable(true);
        mMapView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        mMapController.setCenter(new GeoPoint(LAT,LNG));
        //mMapController.setMapClickEnable(true);
        //mMapView.setSatellite(false);
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
            mLocClient.stop();
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }

    protected void set_mylocation(LocationData p_locData){
//        locData = new LocationData();
//        if(locData.latitude !=0.0 && locData.longitude !=0.0){
        if(p_locData != null && p_locData.latitude != 0.0 && p_locData.longitude != 0.0 && p_locData.latitude != 4.9E-324 && p_locData.longitude != 4.9E-324){
            if(locData == null){
                locData = p_locData;
                myLocationOverlay.setData(locData);
                myLocationOverlay.enableCompass();
                mMapView.getOverlays().add(myLocationOverlay);
                mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
            }
            else{
                locData = p_locData;
                myLocationOverlay.setData(locData);
            }
            mMapView.refresh();
            mMapController.animateTo(new GeoPoint((int) (locData.latitude * 1e6), (int) (locData.longitude * 1e6)));//, mHandler.obtainMessage(1));
        }
    }
//
//    void post_location(Location location){
//        progressDialogShow();
//        new RoboAsyncTask<Boolean>(this) {
//            public Boolean call() throws Exception {
//                Location p_location = ServiceYS.postLocation(location);
//                if (p_location != null) {
//                    location = p_location;
//                    return true;
//                }
//                return false;
//            }
//
//            @Override
//            protected void onException(Exception e) throws RuntimeException {
//                e.printStackTrace();
//                Toaster.showLong(ActivityLocationSelect.this, "提交地点失败。");
//                progressDialogDismiss();
////                setProgressBarVisibility(false);
//            }
//
//            @Override
//            public void onSuccess(Boolean relationship) {
//            }
//
//            @Override
//            protected void onFinally() throws RuntimeException {
//                progressDialogDismiss();
//            }
//        }.execute();
//    }

}


