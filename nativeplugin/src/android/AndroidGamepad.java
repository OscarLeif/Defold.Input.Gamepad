package com.defold.android.gamepad;

import android.content.Context;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import android.util.Log;

import android.view.KeyEvent;

public class AndroidGamepad extends Fragment
{
    private static final String TAG = AndroidGamepad.class.getSimpleName();

    private static final int[] SUPPORTED_KEYS = {
        KeyEvent.KEYCODE_BUTTON_A,
        KeyEvent.KEYCODE_BUTTON_B,
        // Add more keys as needed
    };

    private static Context context;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

}