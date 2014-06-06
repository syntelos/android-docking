/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.llg;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.util.DisplayMetrics;

/**
 * 
 */
public class ObjectSurfaceView
    extends android.view.SurfaceView
    implements android.view.SurfaceHolder.Callback
{
    protected final static String TAG = ObjectLog.TAG;


    protected SurfaceHolder holder;


    public ObjectSurfaceView(ObjectActivity context){
        super(context);

        holder = getHolder();
    }


    public void surfaceCreated(SurfaceHolder holder){
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
    }
    public void surfaceDestroyed(SurfaceHolder holder){
    }
    protected Display display(){

        return ((ObjectActivity)getContext()).display();
    }
    protected DisplayMetrics displayMetrics(){

        return ((ObjectActivity)getContext()).displayMetrics();
    }
    protected SensorManager sensorManager(){

        return ((ObjectActivity)getContext()).sensorManager();
    }
    protected Sensor sensor(int sensor){

        return ((ObjectActivity)getContext()).sensor(sensor);
    }
    protected boolean sensorRegister(SensorEventListener li, Sensor sensor, int rate){

        return ((ObjectActivity)getContext()).sensorRegister(li,sensor,rate);
    }
    protected void sensorUnregister(SensorEventListener li){

        ((ObjectActivity)getContext()).sensorUnregister(li);
    }
    protected void verbose(String m){
        Log.i(TAG,m);
    }
    protected void verbose(String m, Throwable t){
        Log.i(TAG,m,t);
    }
    protected void debug(String m){
        Log.d(TAG,m);
    }
    protected void debug(String m, Throwable t){
        Log.d(TAG,m,t);
    }
    protected void info(String m){
        Log.i(TAG,m);
    }
    protected void info(String m, Throwable t){
        Log.i(TAG,m,t);
    }
    protected void warn(String m){
        Log.w(TAG,m);
    }
    protected void warn(String m, Throwable t){
        Log.w(TAG,m,t);
    }
    protected void error(String m){
        Log.e(TAG,m);
    }
    protected void error(String m, Throwable t){
        Log.e(TAG,m,t);
    }
    protected void wtf(String m){
        Log.wtf(TAG,m);
    }
    protected void wtf(String m, Throwable t){
        Log.wtf(TAG,m,t);
    }
}
