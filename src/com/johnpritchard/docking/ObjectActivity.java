/*
 * Copyright (C) 2014, John Pritchard
 */
package com.johnpritchard.docking;

import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.util.DisplayMetrics;

/**
 * 
 */
public class ObjectActivity
    extends android.app.Activity
    implements SurfaceHolder.Callback
{
    protected final static String TAG = ObjectLog.TAG;


    public ObjectActivity(){
        super();
    }


    /** 
     */
    public void surfaceCreated(SurfaceHolder holder){
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
    }
    public void surfaceDestroyed(SurfaceHolder holder){
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.i(TAG,"onCreate");
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void onPause() {
        Log.i(TAG,"onPause");
        super.onPause();
    }
    @Override
    protected void onResume() {
        Log.i(TAG,"onResume");
        super.onResume();
    }
    @Override
    protected void onStart() {
        Log.i(TAG,"onStart");
        super.onStart();
    }
    @Override
    protected void onStop() {
        Log.i(TAG,"onStop");
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        Log.i(TAG,"onDestroy");
        super.onDestroy();
    }
    protected void attach(Camera c)
        throws java.io.IOException
    {
    }
    protected Display display(){

        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);

        return wm.getDefaultDisplay();
    }
    protected DisplayMetrics displayMetrics(){

        Display display = display();

        DisplayMetrics metrics = new DisplayMetrics();

        display.getMetrics(metrics);

        return metrics;
    }
    protected SensorManager sensorManager(){

        return (SensorManager)getSystemService(SENSOR_SERVICE);
    }
    protected Sensor sensor(int sensor){

        return sensorManager().getDefaultSensor(sensor);
    }
    protected boolean sensorRegister(SensorEventListener li, Sensor sensor, int rate){

        return sensorManager().registerListener(li,sensor,rate);
    }
    protected void sensorUnregister(SensorEventListener li){

        sensorManager().unregisterListener(li);
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
