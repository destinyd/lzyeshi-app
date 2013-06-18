
package dd.android.yeshi_trader.service;

import android.util.Log;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import dd.android.yeshi_trader.R;
import dd.android.yeshi_trader.core.*;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import dd.android.yeshi_trader.ui.ActivityMain;
import roboguice.service.RoboService;
import roboguice.util.RoboAsyncTask;

public class LocateService extends RoboService {

    //      @Inject
//      protected String authToken;
//    @Inject
//    protected ZhaohaiServiceProvider serviceProvider;
//    @InjectExtra(APIKEY)

//    private ProblemStatus ps;

    //获取消息线程
    private MessageThread messageThread = null;

    //点击查看
    private Intent messageIntent = null;
    private PendingIntent messagePendingIntent = null;

    //通知栏消息
    private Notification messageNotification = null;
    private NotificationManager messageNotificatioManager = null;

    public LocationClient mLocClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    BDLocation nowLocation;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();    //To change body of overridden methods use File | Settings | File Templates.

        init();
        //开启线程
        messageThread = new MessageThread();
        messageThread.isRunning = true;
        messageThread.start();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        PropertiesController.readConfiguration();
        if (Settings.getFactory().isOpen) {
            messageNotificatioManager.notify(R.string.app_name, messageNotification);
        } else {
            messageNotificatioManager.cancel(R.string.app_name);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public class MessageBinder extends Binder {
        public LocateService getService() {
            return LocateService.this;
        }
    }

    @Override
    public void onDestroy() {
        System.exit(0);
        //或者，二选一，推荐使用System.exit(0)，这样进程退出的更干净
//        //messageThread.isRunning = false;
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * 从服务器端获取消息
     */
    class MessageThread extends Thread {
        //运行状态，下一步骤有大用
        public boolean isRunning = true;

        public void run() {
            while (isRunning) {
                try {
                    PropertiesController.readConfiguration();
                    if (Settings.getFactory().isOpen) {
                        //get locate
                        if (mLocClient != null)
                        {
                            mLocClient.start();
                            mLocClient.requestLocation();
                        }
                        else
                            Log.d("LocSDK3", "locClient is null or not started");
                        try {
                            Thread.sleep(Constants.Delay.LOCATION_SLEEP);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(Constants.Delay.SERVICE_SLEEP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void init() {
        PropertiesController.readConfiguration();
        //初始化
        messageNotification = new Notification();
        messageNotification.icon = R.drawable.ic_launcher;
        messageNotification.tickerText = "正在摆摊";
//        int style_notifi = 0;
//        if (Settings.f().isSoundNotifi)
//            style_notifi = style_notifi | Notification.DEFAULT_SOUND;
//        if (Settings.f().isShockNotifi)
//            style_notifi = style_notifi | Notification.DEFAULT_VIBRATE;
//        if (Settings.f().isLightNotifi)
//            style_notifi = style_notifi | Notification.DEFAULT_LIGHTS;
//        messageNotification.defaults = style_notifi;
        messageNotification.flags = Notification.FLAG_ONGOING_EVENT;
        messageNotificatioManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        messageIntent = new Intent(LocateService.this, ActivityMain.class);
        messagePendingIntent = PendingIntent.getActivity(LocateService.this, 0, messageIntent, 0);
        //更新通知栏
        messageNotification.setLatestEventInfo(
                LocateService.this,
                "正在摆摊",
                "我们将您的位置公开，给您带来更多的客户", messagePendingIntent);


        mLocClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocClient.registerLocationListener(myListener);    //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setAddrType("all");//返回的定位结果包含地址信息
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(0);//设置发起定位请求的间隔时间为5000ms
        option.disableCache(true);//禁止启用缓存定位
//        option.setPoiNumber(5);	//最多返回POI个数
//        option.setPoiDistance(1000); //poi查询距离
//        option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息

        mLocClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            mLocClient.stop();
            if (location == null)
                return;
            nowLocation = location;
            if(nowLocation.getLatitude() == 4.9E-324 && nowLocation.getLongitude() == 4.9E-324){
                Toast.makeText(LocateService.this,"未能成功定位，请打开gps并连接网络。",Toast.LENGTH_LONG);
                return;
            }
            new RoboAsyncTask<Boolean>(LocateService.this) {
                public Boolean call() throws Exception {
                    postLocation(nowLocation);
                    return true;
                }

                @Override
                protected void onException(Exception e) throws RuntimeException {
                    e.printStackTrace();
//                    Toaster.showLong(LocateService.this, "获取商品分组失败。");
//                setProgressBarVisibility(false);
                }
//
//                @Override
//                public void onSuccess(Boolean relationship) {
////                    pager.setAdapter(new AdapterYSPager(groups,getResources(), getSupportFragmentManager()));
////
////                    indicator.setViewPager(pager);
//                }
//
////            @Override
////            public boolean cancel(boolean mayInterruptIfRunning) {
////                return super.cancel()
////                return task.cancel(mayInterruptIfRunning);
////            }
            }.execute();
//            StringBuffer sb = new StringBuffer(256);
//            sb.append("time : ");
//            sb.append(location.getTime());
//            sb.append("\nerror code : ");
//            sb.append(location.getLocType());
//            sb.append("\nlatitude : ");
//            sb.append(location.getLatitude());
//            sb.append("\nlontitude : ");
//            sb.append(location.getLongitude());
//            sb.append("\nradius : ");
//            sb.append(location.getRadius());
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {
//                sb.append("\nspeed : ");
//                sb.append(location.getSpeed());
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//            }

//            logMsg(sb.toString());
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
//            StringBuffer sb = new StringBuffer(256);
//            sb.append("Poi time : ");
//            sb.append(poiLocation.getTime());
//            sb.append("\nerror code : ");
//            sb.append(poiLocation.getLocType());
//            sb.append("\nlatitude : ");
//            sb.append(poiLocation.getLatitude());
//            sb.append("\nlontitude : ");
//            sb.append(poiLocation.getLongitude());
//            sb.append("\nradius : ");
//            sb.append(poiLocation.getRadius());
//            if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
//                sb.append("\naddr : ");
//                sb.append(poiLocation.getAddrStr());
//            }
//            if (poiLocation.hasPoi()) {
//                sb.append("\nPoi:");
//                sb.append(poiLocation.getPoi());
//            } else {
//                sb.append("noPoi information");
//            }
//            logMsg(sb.toString());
        }
    }

    private void postLocation(BDLocation location) {
//        try {
//            ServiceYS.postLocation(location);
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
    }

}