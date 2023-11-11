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


import android.view.LayoutInflater;
import android.view.ViewGroup;

import android.view.View;
import android.view.KeyEvent;

import android.widget.TextView;

public class AndroidGamepad extends Fragment
{
    private static final String TAG = AndroidGamepad.class.getSimpleName();

    private static Context context;

    private static final int[] SUPPORTED_KEYS = 
    {
        KeyEvent.KEYCODE_BUTTON_A,
        KeyEvent.KEYCODE_BUTTON_B,
        // Add more keys as needed
    };   

    public static AndroidGamepad getGamepad(Context context)
    {
        return new AndroidGamepad(context);
    }

    private AndroidGamepad(final Context context)
    {
        Log.d(TAG, "Register Android Input Plugin");
        this.context = context;
        final FragmentManager fragmentManager = ((Activity)context).getFragmentManager();
		if (fragmentManager.findFragmentByTag(TAG) == null) 
        {
			fragmentManager.beginTransaction().add(this, TAG).commit();
            Log.d(TAG, "Fragment Gamepad Added");
		}
    }


    @Override
	public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         // Create a new TextView programmatically
        TextView textView = new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setText("Hello, Fragment!");

        textView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) 
            {
                Log.d(TAG,"OnKey should Work");
                //if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) 
                if (event.getAction() == KeyEvent.ACTION_DOWN) 
                {
                    // 处理返回按键事件
                    //return true; // 消费掉按键事件
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_BUTTON_A:
                            // Process button A press
                            break;
                        case KeyEvent.KEYCODE_DPAD_LEFT:
                            Log.d("GAMEPAD", "You Press the DPad Left");
                            break;
                        case KeyEvent.KEYCODE_DPAD_RIGHT:
                            Log.d("GAMEPAD", "You Press the DPad Right");
                            // Add cases for other buttons as needed
                        case KeyEvent.KEYCODE_DPAD_UP:
                            Log.d("GAMEPAD", "You Press the DPad Up");
                            break;
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            Log.d("GAMEPAD", "You Press the DPad Down");
                            break;
                        }
                }
                return false;
            }
        });
        Log.d(TAG, "A Crazy View was created" );
        // Set focusable to true to ensure the TextView can receive key events
        textView.setFocusable(true);
        textView.setFocusableInTouchMode(true);

        // Return the created view (textView) for the fragment
        return textView;
    }

    //  @Override
    // public void onViewCreated(View view, Bundle savedInstanceState) {
    //     super.onViewCreated(view, savedInstanceState);
    //     view.setFocusableInTouchMode(true);
    //     view.requestFocus();
    //     view.setOnKeyListener(new View.OnKeyListener() {
    //         @Override
    //         public boolean onKey(View v, int keyCode, KeyEvent event) 
    //         {
    //             Log.d(TAG,"OnKey should Work");
    //             //if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) 
    //             if (event.getAction() == KeyEvent.ACTION_DOWN) 
    //             {
    //                 // 处理返回按键事件
    //                 //return true; // 消费掉按键事件
    //                 switch (keyCode)
    //                 {
    //                     case KeyEvent.KEYCODE_BUTTON_A:
    //                         // Process button A press
    //                         break;
    //                     case KeyEvent.KEYCODE_DPAD_LEFT:
    //                         Log.d("GAMEPAD", "You Press the DPad Left");
    //                         break;
    //                     case KeyEvent.KEYCODE_DPAD_RIGHT:
    //                         Log.d("GAMEPAD", "You Press the DPad Right");
    //                         // Add cases for other buttons as needed
    //                     case KeyEvent.KEYCODE_DPAD_UP:
    //                         Log.d("GAMEPAD", "You Press the DPad Up");
    //                         break;
    //                     case KeyEvent.KEYCODE_DPAD_DOWN:
    //                         Log.d("GAMEPAD", "You Press the DPad Down");
    //                         break;
    //                     }
    //             }
    //             return false;
    //         }
    //     });
    //     Log.d(TAG,"The Key Listener Should be register");
    // }

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