/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.SharedPreferences;
import android.view.SurfaceHolder;
import android.opengl.GLES10;

import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Because GL rendering occurs in a thread distinct from the Main/UI
 * thread, the renderer requires its own object and must employ
 * inter-thread communication techniques for user input.
 */
public final class View3DRenderer
    extends ObjectLog
    implements android.opengl.GLSurfaceView.Renderer,
               android.view.SurfaceHolder.Callback,
               View3DScreenShot
{

    private View3D view;

    private SharedPreferences preferences;

    protected volatile Page pageId;

    protected volatile ViewPage3D page;

    private boolean plumb = false;

    protected int width = -1, height = -1;

    private boolean stale = true;

    private volatile boolean screenshot = false;
    private volatile int screenshotFormat = 0;
    private volatile int screenshotType = 0;
    private volatile ByteBuffer screenshotBuffer = null;
    private final Object screenshotMonitor = new Object();


    public View3DRenderer(View3D view){
        super();
        this.view = view;
    }


    public SharedPreferences preferences(){

        return this.preferences;
    }
    /**
     * Occurs before surface created
     */
    public synchronized void onCreate(SharedPreferences state){

        this.preferences = state;
    }
    public void onResume(){

        pageTo(Page.valueOf(preferences.getString("page","gameview")));
    }
    public synchronized void onPause(SharedPreferences.Editor state){

        if (null != this.pageId){
            /*
             * {Input.Back} -> {Page.start}
             * 
             * This rule permits the back button to have the desired
             * effect on devices where the back button operates on the
             * activity stack without passing through the View key
             * event process.
             */
            state.putString("page","start");

            if (null != this.page){

                this.page.down(state);
            }
        }

        this.plumb = false;
    }
    public synchronized void surfaceCreated(SurfaceHolder holder){

        this.plumb = false;
        this.screenshot = false;
    }
    public synchronized void surfaceChanged(SurfaceHolder holder, int format, int w, int h){

        this.width = w;
        this.height = h;

        this.plumb = true;
        this.screenshot = false;

        if (null != this.page){

            this.page.up(view,width,height);
        }
    }
    public synchronized void surfaceDestroyed(SurfaceHolder holder){
        info("surfaceDestroyed");

        this.plumb = false;
        this.screenshot = false;
    }
    /**
     * Called from {@link ViewAnimator}
     * @see #script
     */
    public synchronized void pageTo(Page page){

        if (screenshot){

            return;
        }
        else {
            info("pageTo "+page);

            if (null == page){

                return;
            }
            else if (null != this.page){

                if (page.page != this.page){

                    this.page.down();
                    try {
                        this.pageId = page;

                        this.page = (ViewPage3D)page.page;

                        if (this.plumb){

                            this.page.up(view,width,height);
                        }
                    }
                    catch (ClassCastException exc){

                        this.page = null;

                        warn("switching to 2D for page: "+page);

                        Docking.StartActivity2D();
                    }
                }
            }
            else {
                try {
                    this.pageId = page;

                    this.page = (ViewPage3D)page.page;

                    if (this.plumb){

                        this.page.up(view,width,height);
                    }
                }
                catch (ClassCastException exc){

                    this.page = null;

                    warn("switching to 2D for page: "+page);

                    Docking.StartActivity2D();
                }
            }
        }
    }
    /**
     * @see View3DScreenShot
     */
    public boolean screenshot(int format, int type, ByteBuffer buffer)
        throws InterruptedException
    {

        this.screenshotFormat = format;
        this.screenshotType = type;
        this.screenshotBuffer = buffer;
        this.screenshot = true;

        synchronized(screenshotMonitor){

            screenshotMonitor.wait(1000L);
        }

        return (!screenshot);
    }
    /**
     * Renderer
     */
    public void onDrawFrame(GL10 gl){
        if (screenshot){
            try {
                GLES10.glReadPixels(0,0,width,height,screenshotFormat,screenshotType,screenshotBuffer);
            }
            finally {
                this.screenshot = false;
            }
            synchronized(screenshotMonitor){

                screenshotMonitor.notifyAll();
            }
        }
        else if (null != page){

            if (plumb){

                if (stale){
                    stale = false;

                    page.init(gl);
                }
                page.draw(gl);
            }
            else {

                page.up(view,width,height);

                plumb = true;
            }
        }
    }
    /**
     * Renderer
     */
    public void onSurfaceChanged(GL10 gl, int width, int height){

        // this.width = width;
        // this.height = height;

        // if (null != page){

        //     page.down();

        //     plumb = false;

        //     page.up(view,width,height);

        //     plumb = true;
        // }
    }
    /**
     * Renderer
     */
    public void onSurfaceCreated(GL10 gl, EGLConfig config){
    }
}
