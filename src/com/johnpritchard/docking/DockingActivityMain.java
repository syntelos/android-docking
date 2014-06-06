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
import android.view.Window;
import android.view.WindowManager;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 
 */
public class DockingActivityMain
    extends ObjectActivity
{

    private View2D view;

    private SharedPreferences preferences;


    @Override
    public void onCreate(Bundle state)
    {
        Docking.MainActivate(this);

        super.onCreate(state);
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        this.preferences = this.getSharedPreferences("llg.properties",MODE_PRIVATE);

        this.view = new View2D(this);

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
        info("onResume");

        this.view.onResume();
    }
    protected SharedPreferences preferences(){

        return this.preferences;
    }
}
