/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.llg;

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
    public final static String TAG = "ULSF";


    protected ObjectActivity context;


    protected ObjectLog(){
        super();
    }
    protected ObjectLog(ObjectActivity context){
        super();

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
