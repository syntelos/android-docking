/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;

/**
 * 
 */
public class DockingActivity3D
    extends ObjectActivity
{

    View3D view;


    @Override
    public void onCreate(Bundle state)
    {
        Docking.Activate3D(this);

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

        DockingGameLevel.OnPause(preferences);

        preferences.commit();
    }
    @Override
    protected void onResume(){
        super.onResume();

        DockingGameLevel.OnResume(this.preferences);

        this.view.onResume();
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){

        view.script(new InputScript[]{
                new InputScript.Database(InputScript.Database.Op.Init,this)
            });
    }
}
