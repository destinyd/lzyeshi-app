package dd.android.yeshi.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import com.saulpower.fayeclient.FayeClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.prefs.Preferences;

/**
 * Created with IntelliJ IDEA.
 * User: dd
 * Date: 13-4-18
 * Time: 下午1:01
 * To change this template use File | Settings | File Templates.
 */
public class WebSocketService extends IntentService implements FayeClient.FayeListener {

    public final String TAG = this.getClass().getSimpleName();

    FayeClient mClient;
    Handler handler;// =new Handler();

    public WebSocketService() {
        super("WebSocketService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // SSL bug in pre-Gingerbread devices makes websockets currently unusable
        if (android.os.Build.VERSION.SDK_INT <= 8) return;

        Log.e(TAG, "Starting Web Socket Server");

        handler = new Handler() ;//{
//            //            public void handleMessage(String msg) {
////                Log.e(TAG, "handler Server" + msg);
////            }
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
////                bar.incrementProgressBy(5);
//                Log.e(TAG, "handler Server" + String.valueOf(msg.what));
//            }
//        };

        try {

//            String baseUrl = Preferences.getString(Preferences.KEY_FAYE_HOST, DebugActivity.PROD_FAYE_HOST);
            String baseUrl = "192.168.1.4";

//            URI uri = URI.create(String.format("wss://%s:443/events", baseUrl));
            URI uri = URI.create(String.format("ws://%s:9292/faye", baseUrl));
//            String channel = String.format("/%s/**", User.getCurrentUser().getUserId());
            String channel = "/room/11/messages/new";

            JSONObject ext = new JSONObject();
//            ext.put("authToken", User.getCurrentUser().getAuthorizationToken());
//            ext.put("authToken", "anything");
            ext.put("authToken", "anything");

            Log.e(TAG, "FayeClient Server");
            mClient = new FayeClient(handler, uri, channel);
            Log.e(TAG, "setFayeListener Server");
            mClient.setFayeListener(this);
            Log.e(TAG, "connectToServer Server");
            mClient.connectToServer(ext);

        } catch (Exception ex) {
            Log.e(TAG, "Exception Server");
            ex.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e(TAG, "onStart Server");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand Server");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void connectedToServer() {
        Log.e(TAG, "Connected to Server");
    }

    @Override
    public void disconnectedFromServer() {
        Log.e(TAG, "Disonnected to Server");
    }

    @Override
    public void subscribedToChannel(String subscription) {
        Log.e(TAG, String.format("Subscribed to channel %s on Faye Server", subscription));
    }

    @Override
    public void subscriptionFailedWithError(String error) {
        Log.e(TAG, String.format("Subscription failed with error: %s Server", error));
    }

    @Override
    public void messageReceived(JSONObject jsonObject) {
        Log.e(TAG, String.format("Received message %s Server", jsonObject.toString()));
    }


}