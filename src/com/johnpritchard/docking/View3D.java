/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ConfigurationInfo;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import static android.opengl.GLES11.*;
import static android.opengl.GLES11Ext.*;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.util.DisplayMetrics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * The GL suface view requires Activity onPause and onResume calls
 * from the user.  The GL surface view (super class) implements the
 * surface holder callback which it employs for initializing the GL
 * context.
 */
public final class View3D
    extends android.opengl.GLSurfaceView
    implements View
{
    protected final static String TAG = ObjectLog.TAG;


    private SharedPreferences preferences;

    private SurfaceHolder holder;

    private View3DRenderer renderer;


    public View3D(ObjectActivity context){
        super(context);

        holder = getHolder();

        renderer = new View3DRenderer(this);

        setRenderer(renderer);

        holder.addCallback(renderer);

        holder.setKeepScreenOn(true);
    }


    public final boolean is2D(){
        return false;
    }
    public final boolean is3D(){
        return true;
    }
    public SharedPreferences preferences(){

        return this.preferences;
    }
    /**
     * Occurs before surface created
     */
    public void onCreate(SharedPreferences state){
        info("onCreate");

        this.preferences = state;

        this.renderer.onCreate(state);
    }
    public void onResume(){
        info("onResume");

        ViewAnimation.Start(this);

        this.renderer.onResume();
    }
    public void onPause(SharedPreferences.Editor state){
        info("onPause");

        this.renderer.onPause(state);

        ViewAnimation.Stop();
    }
    /**
     * Called from {@link ViewAnimator}
     * @see #script
     */
    public void pageTo(Page page){

        this.renderer.pageTo(page);
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
