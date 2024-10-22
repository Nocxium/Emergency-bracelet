package com.example.teeest.ui.home;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teeest.BusProvider;
import com.example.teeest.MainActivity;
import com.example.teeest.MyAdapter;
import com.example.teeest.R;
import com.example.teeest.UpdateRecyclerEvent;

//using a library provided by Otto. Source code - https://github.com/square/otto
import com.squareup.otto.Subscribe;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    MyAdapter adapt;
    public RecyclerView recyclerView;
    public MainActivity activity;
    ViewGroup viewgroup;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        viewgroup = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        return viewgroup;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Retrieving MainActivity instance, with all it's public variables and methods.
        activity = (MainActivity) getActivity();

        //Finding RecycleView that's resides on home fragment.
        recyclerView = view.findViewById(R.id.recyclerView2);

        //Accessing adapter that stores information from MainActivity instance.
        adapt = activity.adapt;

        //Assigning adapter from MainActivity to home fragment's RecycleView.
        recyclerView.setAdapter(adapt);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Class that handles swipes on RecycleView elements
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                adapt.DeleteItem(position);
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();

        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        BusProvider.getInstance().unregister(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Subscribe public void onRecyclerUpdate(UpdateRecyclerEvent event){
        recyclerView = viewgroup.findViewById(R.id.recyclerView2);
        adapt = activity.adapt;
        recyclerView.setAdapter(adapt);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}