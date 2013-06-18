package dd.android.yeshi_trader.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.saulpower.fayeclient.FayeClient;
import dd.android.yeshi_trader.R;
import dd.android.yeshi_trader.core.FayeNotificationChatMessage;
import dd.android.yeshi_trader.core.PropertiesController;
import dd.android.yeshi_trader.core.Settings;
import dd.android.yeshi_trader.ui.ActivityChatMessages;
import org.json.JSONObject;

import java.net.URI;

import static dd.android.yeshi_trader.core.Constants.Extra.*;
import static dd.android.yeshi_trader.core.Constants.Extra.NAME;
import static dd.android.yeshi_trader.core.Constants.Faye.*;
import static dd.android.yeshi_trader.core.Constants.Faye.WS_FAYE;
import static dd.android.yeshi_trader.core.Constants.Other.*;

/**
 * Created with IntelliJ IDEA.
 * User: dd
 * Date: 13-4-18
 * Time: 下午1:01
 * To change this template use File | Settings | File Templates.
 */
public class NotificationFayeService extends IntentService implements FayeClient.FayeListener {

    public final String TAG = this.getClass().getSimpleName();

    public final String FORMAT_NOTIFICATION_CHANNEL = "/n/%s";

    FayeClient mClient;
    Handler handler;// =new Handler();

    //通知栏消息
    private Notification messageNotification = null;
    private NotificationManager messageNotificatioManager = null;
    //点击查看
    private Intent messageIntent = null;
    private PendingIntent messagePendingIntent = null;

    String name = null;

    public NotificationFayeService() {
        super("NotificationFayeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // SSL bug in pre-Gingerbread devices makes websockets currently unusable
        if (android.os.Build.VERSION.SDK_INT <= 8) return;

        PropertiesController.readConfiguration();

        Log.d(TAG, "onHandleIntent");

        if(Settings.getFactory().getName() != null)
            name = Settings.getFactory().getName();
        if(name == null) return;

        Log.d(TAG, "Starting Web Socket Server");

        handler = new Handler() ;

        try {

            URI uri = URI.create(WS_FAYE);
            String channel = String.format(FORMAT_NOTIFICATION_CHANNEL,name);

            JSONObject ext = new JSONObject();
//            ext.put("authToken", "anything");

            Log.d(TAG, "FayeClient Server channel " + channel);
            mClient = new FayeClient(handler, uri, channel);
            Log.d(TAG, "setFayeListener Server");
            mClient.setFayeListener(this);
            Log.d(TAG, "connectToServer Server");
            mClient.connectToServer(ext);

        } catch (Exception ex) {
            Log.e(TAG, "Exception Server");
            ex.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        init_notification();
    }

    private void init_notification() {
        messageNotification = new Notification();
        messageNotification.icon = R.drawable.ic_launcher;
        messageNotification.tickerText = "您有新的消息";
        int style_notifi = 0;
        if (Settings.getFactory().isSoundNotifi)
            style_notifi = style_notifi | Notification.DEFAULT_SOUND;
        if (Settings.getFactory().isShockNotifi)
            style_notifi = style_notifi | Notification.DEFAULT_VIBRATE;
        if (Settings.getFactory().isLightNotifi)
            style_notifi = style_notifi | Notification.DEFAULT_LIGHTS;
        messageNotification.defaults = style_notifi;
        messageNotificatioManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart Server");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand Server");
        if(intent.getStringExtra(NAME) != null)
            name = intent.getStringExtra(NAME);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void connectedToServer() {
        Log.d(TAG, "Connected to Server");
    }

    @Override
    public void disconnectedFromServer() {
        Log.d(TAG, "Disonnected to Server");
    }

    @Override
    public void subscribedToChannel(String subscription) {
        Log.d(TAG, String.format("Subscribed to channel %s on Faye Server", subscription));
    }

    @Override
    public void subscriptionFailedWithError(String error) {
        Log.d(TAG, String.format("Subscription failed with error: %s Server", error));
    }

    @Override
    public void messageReceived(JSONObject jsonObject) {
        try{
            Log.d(TAG, String.format("Received message %s Server", jsonObject.toString()));
            FayeNotificationChatMessage fncm = JSON.parseObject(jsonObject.toString(), FayeNotificationChatMessage.class);
            messageIntent = new Intent(this, ActivityChatMessages.class);
            messagePendingIntent = PendingIntent.getActivity(this, 0, messageIntent, 0);
            //更新通知栏
            messageNotification.setLatestEventInfo(
                    this,
                    "您有新的消息",
                    String.format("%s 给您发了条短信息,点击查看", fncm.getName()), messagePendingIntent);
            messageNotificatioManager.notify(R.string.app_name, messageNotification);
        }
        catch (Exception e){
            Log.e(TAG, e.getLocalizedMessage());
        }
    }




}