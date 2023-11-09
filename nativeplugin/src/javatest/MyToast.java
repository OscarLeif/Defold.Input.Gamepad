package com.defold.toastextension;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import android.util.Log;

public class MyToast 
{
    private static final Handler handler = new Handler(Looper.getMainLooper());

    public static void showToast(Context context, String message) 
    {
        //Log.d("My Tag", "We should display a Toast = "+ message );
        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
         handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("My Tag", "We should display a Toast = " + message);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}