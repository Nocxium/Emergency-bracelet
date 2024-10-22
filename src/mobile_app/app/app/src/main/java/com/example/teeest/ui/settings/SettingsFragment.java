package com.example.teeest.ui.settings;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.teeest.R;

public class SettingsFragment extends Fragment {

    ViewGroup viewgroup;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        viewgroup = (ViewGroup) inflater.inflate(R.layout.fragment_settings, container, false);

        return viewgroup;
    }

}