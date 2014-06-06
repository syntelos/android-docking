/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.llg;

import android.app.ActivityManager;
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
    implements View,
               android.opengl.GLSurfaceView.Renderer
{
    protected final static String TAG = ObjectLog.TAG;


    private SurfaceHolder holder;

    private SharedPreferences preferences;

    private volatile ViewPage3D page;

    private boolean plumb = false;

    private int width = -1, height = -1;


    public View3D(ObjectActivity context){
        super(context);

        holder = getHolder();

        setRenderer(this);

        holder.addCallback(context);

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

        this.pageTo(state.getString("view.page","game"));
    }
    public void onResume(){
        info("onResume");

        ViewAnimation.Start(this);
    }
    public void onPause(SharedPreferences.Editor state){
        info("onPause");

        if (null != this.page){

            state.putString("view.page",this.page.name());

            this.page.down(state);
        }

        this.plumb = false;

        ViewAnimation.Stop();
    }
    public void surfaceCreated(SurfaceHolder holder){
        info("surfaceCreated");

        this.plumb = false;
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
        info("surfaceChanged");

        this.width = w;
        this.height = h;

        this.plumb = true;

        this.page.up(this,width,height);

    }
    public void surfaceDestroyed(SurfaceHolder holder){
        info("surfaceDestroyed");

        this.plumb = false;
    }
    /**
     * Called from {@link #onCreate} followed by {@link #repaint}.
     * @see #script
     */
    private void pageTo(String name){
        try {
            this.pageTo(Page.valueOf(name));
        }
        catch (RuntimeException exc){

            this.pageTo(Page.start);
        }
    }
    /**
     * Called from {@link ViewAnimator}
     * @see #script
     */
    public void pageTo(Page page){

        if (null == page){

            return;
        }
        else if (null != this.page){

            if (page.page != this.page){

                this.page.down();

                this.page = (ViewPage3D)page.page;

                if (this.plumb){

                    this.page.up(this,width,height);
                }
            }
        }
        else {
            this.page = (ViewPage3D)page.page;

            if (this.plumb){

                this.page.up(this,width,height);
            }
        }
    }
    /**
     * Renderer
     */
    public void onDrawFrame(GL10 gl){
        if (null != page){

            if (plumb){

                page.draw(gl);
            }
            else {

                page.up(this,width,height);

                plumb = true;
            }
        }
    }
    /**
     * Renderer
     */
    public void onSurfaceChanged(GL10 gl, int width, int height){

        this.width = width;
        this.height = height;

        if (null != page){

            page.down();

            plumb = false;

            page.up(this,width,height);

            plumb = true;
        }
    }
    /**
     * Renderer
     */
    public void onSurfaceCreated(GL10 gl, EGLConfig config){
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
