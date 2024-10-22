package com.example.teeest;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.otto.Subscribe;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity{

    ImageButton imageButton;
    NavController navController;

    Intent intent;

    // Adapter that stores and transfers data to RecycleView in home fragment.
    public MyAdapter adapt;

    List<String> dates = new ArrayList<String>();
    List<String> devices = new ArrayList<String>();


    private void showNotification(String msg, String title, Boolean autoCancel) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "emergency");

        builder.setContentTitle(title);
        builder.setContentText(msg);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(autoCancel);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1, builder.build());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Navigation
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.navigation_home);


        //Adapter that stores and passes the data to the RecyclerView element
        adapt = new MyAdapter(this, dates, devices);
        //

        //Notification set-up using NotificationManager if target device has android 8.0 or higher
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chnl = new NotificationChannel("emergency", "emergencyNotif",
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(chnl);
        }
        //
        BusProvider.getInstance().register(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        intent = new Intent(this, MqttConnectionManagerService.class);
        startService(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        intent = new Intent(this, MqttConnectionManagerService.class);
        startService(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        startForegroundService(intent);
    }

    @Subscribe public void onMessageReceived(MessageReceivedEvent event){
        adapt.AddNewItem(event.date, event.msg);
        showNotification("Someone may be in danger!", "ATTENTION!", false);
        BusProvider.getInstance().post(new UpdateRecyclerEvent());
    }


}