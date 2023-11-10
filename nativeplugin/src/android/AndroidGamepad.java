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

import android.view.View;
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

     @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 处理返回按键事件
                    return true; // 消费掉按键事件
                }
                return false;
            }
        });
    }

    private boolean isSupportedKey(int keyCode) {
        // Check if the key code is in the list of supported keys
        for (int supportedKey : SUPPORTED_KEYS) {
            if (keyCode == supportedKey) {
                return true;
            }
        }
        return false;
    }

}