/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.hardware.Camera;
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
public class ObjectLog
    extends java.lang.Object
    implements android.view.SurfaceHolder.Callback
{
    public final static String TAG = "Docking";


    protected final ObjectActivity context;

    protected final String className;

    protected final String baseName;


    protected ObjectLog(){
        this(null);
    }
    protected ObjectLog(ObjectActivity context){
        super();

        this.className = this.getClass().getName();
        this.baseName = Basename(className);

        this.context = context;
    }


    public ObjectActivity getContext(){
        return context;
    }
    public void surfaceCreated(SurfaceHolder holder){
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
    }
    public void surfaceDestroyed(SurfaceHolder holder){
    }
    protected void attach(Camera c)
        throws java.io.IOException
    {
        context.attach(c);
    }
    protected Display display(){

        return context.display();
    }
    protected DisplayMetrics displayMetrics(){

        return context.displayMetrics();
    }
    protected SensorManager sensorManager(){

        return context.sensorManager();
    }
    protected Sensor sensor(int sensor){

        return context.sensor(sensor);
    }
    protected boolean sensorRegister(SensorEventListener li, Sensor sensor, int rate){

        return context.sensorRegister(li,sensor,rate);
    }
    protected void sensorUnregister(SensorEventListener li){

        context.sensorUnregister(li);
    }
    protected void verbose(String m){
        Log.i(TAG,(baseName+' '+m));
    }
    protected void verbose(String m, Throwable t){
        Log.i(TAG,(baseName+' '+m),t);
    }
    protected void debug(String m){
        Log.d(TAG,(baseName+' '+m));
    }
    protected void debug(String m, Throwable t){
        Log.d(TAG,(baseName+' '+m),t);
    }
    protected void info(String m){
        Log.i(TAG,(baseName+' '+m));
    }
    protected void info(String m, Throwable t){
        Log.i(TAG,(baseName+' '+m),t);
    }
    protected void warn(String m){
        Log.w(TAG,(baseName+' '+m));
    }
    protected void warn(String m, Throwable t){
        Log.w(TAG,(baseName+' '+m),t);
    }
    protected void error(String m){
        Log.e(TAG,(baseName+' '+m));
    }
    protected void error(String m, Throwable t){
        Log.e(TAG,(baseName+' '+m),t);
    }
    protected void wtf(String m){
        Log.wtf(TAG,(baseName+' '+m));
    }
    protected void wtf(String m, Throwable t){
        Log.wtf(TAG,(baseName+' '+m),t);
    }
    protected final static String Basename(String cn){
        int idx = cn.lastIndexOf('.');
        if (0 < idx)
            return cn.substring(idx+1);
        else
            return cn;
    }
}
