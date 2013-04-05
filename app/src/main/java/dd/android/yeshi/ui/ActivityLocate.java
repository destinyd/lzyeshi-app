package dd.android.yeshi.ui;

import dd.android.yeshi.FCApplication;
import dd.android.yeshi.R;
import dd.android.yeshi.core.Problem;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.baidu.location.*;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.search.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
//import com.umeng.analytics.MobclickAgent;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

import static dd.android.yeshi.core.Constants.Extra.PROBLEM;

public class ActivityLocate extends ActivityYS {
    @InjectExtra(PROBLEM)
    protected Problem problem;
    @InjectView(R.id.bmapView)
    protected MapView mMapView;

    private MapController mMapController = null;

    public MKMapViewListener mMapListener = null;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    MyLocationOverlay myLocationOverlay = null;
    LocationData locData = null;

    MKSearch mSearch = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_address);

        mMapController = mMapView.getController();

        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);


        mSearch = new MKSearch();
        mSearch.init(FCApplication.getInstance().mBMapManager, new MKSearchListener() {
            @Override
            public void onGetPoiDetailSearchResult(int type, int error) {
            }

            public void onGetAddrResult(MKAddrInfo res, int error) {
                if (error != 0) {
                    String str = String.format("错误号：%d", error);
                    Toast.makeText(ActivityLocate.this, str, Toast.LENGTH_LONG).show();
                    return;
                }
                mMapView.getController().animateTo(res.geoPt);

//                String strInfo = String.format("onGetAddrResult 纬度：%f 经度：%f\r\n", res.geoPt.getLatitudeE6()/1e6, res.geoPt.getLongitudeE6()/1e6);
//
//                Toast.makeText(ActivityLocate.this, strInfo, Toast.LENGTH_LONG).show();
//                Drawable marker = getResources().getDrawable(R.drawable.icon_markf);  //得到需要标在地图上的资源
//                marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());   //为maker定义位置和边界
                mMapView.getOverlays().clear();
//                mMapView.getOverlays().add(new OverItemT(marker, ActivityLocate.this, res.geoPt, res.strAddr));
                PoiOverlayFC poiOverlay = new PoiOverlayFC(ActivityLocate.this,mMapView);
                if(res.poiList != null){
                    poiOverlay.setData(res.poiList);
                    mMapView.getOverlays().add(poiOverlay);
                    mMapView.refresh();
                    poiOverlay.animateTo();
                }
            }
            public void onGetPoiResult(MKPoiResult res, int type, int error) {
                if (error != 0 || res == null) {
                    Toast.makeText(ActivityLocate.this, "onGetPoiResult 解析失败", Toast.LENGTH_LONG).show();
                    return;
                }
                if (res != null && res.getCurrentNumPois() > 0) {
                    GeoPoint ptGeo = res.getAllPoi().get(0).pt;
                    // 移动地图到该点：
                    mMapView.getController().animateTo(ptGeo);

                    String strInfo = String.format("纬度：%f 经度：%f\r\n", ptGeo.getLatitudeE6() / 1e6, ptGeo.getLongitudeE6() / 1e6);
                    strInfo += "\r\n附近有：";
                    for (int i = 0; i < res.getAllPoi().size(); i++) {
                        strInfo += (res.getAllPoi().get(i).name + ";");
                    }
                    Toast.makeText(ActivityLocate.this, strInfo, Toast.LENGTH_LONG).show();
                }
            }
            public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
            }
            public void onGetTransitRouteResult(MKTransitRouteResult res, int error) {
            }
            public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error) {
            }
            public void onGetBusDetailResult(MKBusLineResult result, int iError) {
            }
            @Override
            public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
            }

        });

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");     //设置坐标类型
        option.setScanSpan(30000);//5s for test
        option.setAddrType("all");
        option.setPriority(LocationClientOption.GpsFirst);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);

        mMapView.displayZoomControls(true);
//        mMapListener = new MKMapViewListener() {
//
//            @Override
//            public void onMapMoveFinish() {
//            }
//
//            @Override
//            public void onClickMapPoi(MapPoi mapPoiInfo) {
//            }
//        };
//        mMapView.regMapViewListener(FCApplication.getInstance().mBMapManager, mMapListener);
        myLocationOverlay = new MyLocationOverlay(mMapView);
//        set_mylocation(new LocationData());
        problem_to_view();

    }

    private void problem_to_view() {
        GeoPoint ptCenter = new GeoPoint((int)(problem.getLat() * 1E6),(int)(problem.getLng() * 1E6));
        mSearch.reverseGeocode(ptCenter);
//        PoiOverlayFC poiOverlay = new PoiOverlayFC(this, mMapView);
//        ArrayList<MKPoiInfo> pois = new ArrayList<MKPoiInfo>();
//        MKPoiInfo mkPoiInfo = new MKPoiInfo();
//        mkPoiInfo.address = problem.getAddress();
//        mkPoiInfo.pt = new GeoPoint((int)(problem.getLat() * 1E6),(int)(problem.getLng() * 1E6));
//        mkPoiInfo.name = problem.getAddress();
//        pois.add(mkPoiInfo);
//        poiOverlay.setData(pois);
//        mMapView.getOverlays().clear();
//        mMapView.getOverlays().add(poiOverlay);
        set_mylocation(locData);
        mMapView.refresh();
//        poiOverlay.animateTo();
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
            if (poiLocation == null) {
                return;
            }
        }
    }

    public class NotifyLister extends BDNotifyListener {
        public void onNotify(BDLocation mlocation, float distance) {
        }
    }

    protected void set_mylocation(LocationData p_locData){
//        locData = new LocationData();
//        if(locData.latitude !=0.0 && locData.longitude !=0.0){
        if(p_locData != null && p_locData.latitude != 0.0 && p_locData.longitude != 0.0 && p_locData.latitude != 4.9E-324 && p_locData.longitude != 4.9E-324){
            if(locData == null){
                locData = p_locData;
                myLocationOverlay.setData(locData);
                mMapView.getOverlays().add(myLocationOverlay);
                myLocationOverlay.enableCompass();
                mMapView.refresh();
                mMapController.animateTo(new GeoPoint((int) (locData.latitude * 1e6), (int) (locData.longitude * 1e6)));//, mHandler.obtainMessage(1));
            }
            else{
                Log.e("new my location", String.format(" %s, %s", p_locData.latitude, p_locData.longitude));
                locData = p_locData;
                myLocationOverlay.setData(locData);
                mMapView.refresh();
                mMapController.animateTo(new GeoPoint((int) (locData.latitude * 1e6), (int) (locData.longitude * 1e6)));//, mHandler.obtainMessage(1));
            }
        }
    }

}

