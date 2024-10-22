package com.example.teeest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RequiresApi(api = Build.VERSION_CODES.O)
public class MqttConnectionManagerService extends Service {

    public MqttAndroidClient client;
    public MqttConnectOptions options;
    private final IBinder mBinder = new LocalBinder();

    private static final String TAG = "MyTag";
    private ConnectivityManager mConnMan;

    String clientId = MqttClient.generateClientId();
    String serverUrl = "tcp://192.168.224.1:1883";
    String password = "s";
    String username = "s";
    String topic = "is222zf/feeds/lights/issending/";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    int timestampInt = 0;


    public class LocalBinder extends Binder {
        MqttConnectionManagerService getService(){
            return MqttConnectionManagerService.this;
        }
    }

    public class MqttServiceStartReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            connect(client, options);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "foreground",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }

        options = createMqttConnectOptions(username, password);
        client = createMqttAndroidClient(serverUrl, this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new MqttServiceStartReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mConnMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStart");
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "foreground",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    private MqttConnectOptions createMqttConnectOptions(String username, String password) {
        MqttConnectOptions connection_options = new MqttConnectOptions();
        connection_options.setCleanSession(true);
        connection_options.setUserName(username);
        connection_options.setPassword(password.toCharArray());
        connection_options.setAutomaticReconnect(true);

        return connection_options;
    }

    private MqttAndroidClient createMqttAndroidClient(String serverUrl, Context ct) {
        client = new MqttAndroidClient(ct, serverUrl, clientId);
        return client;
    }

    public void connect(final MqttAndroidClient client, MqttConnectOptions options) {

        try {
            if (!client.isConnected()) {
                IMqttToken token = client.connect(options);
                //on successful connection, publish or subscribe as usual
                token.setActionCallback(connectActionListener);
                client.setCallback(callbackExtended);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    IMqttActionListener connectActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.d(TAG, "Connected");
            sub(topic, 0, (MqttAndroidClient) asyncActionToken.getClient());
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.d(TAG, "Not connected");
        }
    };


    MqttCallbackExtended callbackExtended = new MqttCallbackExtended() {
        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            Log.d(TAG, "Successfully reconnected");
            sub(topic, 0, client);
        }

        @Override
        public void connectionLost(Throwable cause) {
            Log.d(TAG, "Lost connection, reconnecting");
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void messageArrived(String topic, @NonNull MqttMessage message) throws Exception {
            int diff = (int) SystemClock.elapsedRealtime() - timestampInt;
            Log.d(TAG, Integer.toString(diff));

            if(diff > 250){
                String msg = new String(message.getPayload());
                Log.d(TAG, msg);

                //Posting an event that tells home fragment to update it's RecycleView
                MessageReceivedEvent messageReceivedEvent= new MessageReceivedEvent();
                LocalDateTime date = LocalDateTime.now();
                messageReceivedEvent.msg = msg;
                messageReceivedEvent.date = date.format(formatter);
                BusProvider.getInstance().post(messageReceivedEvent);

                timestampInt = (int) SystemClock.elapsedRealtime();
            }else{
                Log.d(TAG,  "Duplicate");
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };


    private void sub(String topic, int qos, MqttAndroidClient client){
        try{
            client.subscribe(topic, qos);
            Log.d(TAG, "Subscribed");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}