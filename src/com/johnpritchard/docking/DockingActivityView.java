/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 
 */
public class DockingActivityView
    extends ObjectActivity
{

    View3D view;


    @Override
    public void onCreate(Bundle state)
    {
        Docking.ViewActivate(this);

        super.onCreate(state);
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        this.preferences = this.getSharedPreferences("docking.properties",MODE_PRIVATE);

        this.view = new View3D(this);

        this.setContentView(this.view);

        this.view.onCreate(this.preferences);
    }
    @Override
    protected void onPause(){
        super.onPause();

        SharedPreferences.Editor preferences = this.preferences.edit();

        this.view.onPause(preferences);

        preferences.commit();
    }
    @Override
    protected void onResume(){
        super.onResume();

        this.view.onResume();
    }
}
