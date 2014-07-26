/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.SharedPreferences;
import android.view.SurfaceHolder;
import static android.opengl.GLES10.*;

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
               android.view.SurfaceHolder.Callback
{

    private final View3D view;

    private SharedPreferences preferences;

    protected volatile Page pageId;

    protected volatile ViewPage3D page;

    protected volatile boolean plumb = false;

    protected int width = -1, height = -1;

    private boolean stale = true;

    private volatile boolean screenshot_ext = false;
    private volatile int screenshot_extFormat = 0;
    private volatile int screenshot_extType = 0;
    private volatile ByteBuffer screenshot_extBuffer = null;
    private final Object screenshot_extMonitor = new Object();

    // private volatile View3DScreenShot screenshot_movie = null;


    public View3DRenderer(View3D view){
        super(view.logPrefix);
        if (null != view){
            this.view = view;
        }
        else {
            throw new IllegalArgumentException();
        }
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

        pageTo(Page.valueOf(preferences.getString("page","gamehistory")));

        if (plumb){

            ViewAnimation.Start(view);
        }
    }
    public synchronized void onPause(SharedPreferences.Editor state){

        ViewAnimation.Stop(view);

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
        this.screenshot_ext = false;
    }
    public synchronized void surfaceChanged(SurfaceHolder holder, int format, int w, int h){

        this.width = w;
        this.height = h;

        this.plumb = true;
        this.screenshot_ext = false;

        if (null != this.page){

            this.page.up(view,width,height);
        }

        ViewAnimation.Start(view);

        // try {
        //     screenshot_movie = new View3DScreenShot(view);
        // }
        // catch (Exception exc){

        //     error("movie",exc);
        // }
    }
    public synchronized void surfaceDestroyed(SurfaceHolder holder){

        ViewAnimation.Stop(view);

        this.plumb = false;
        this.screenshot_ext = false;
    }
    /**
     * Called from {@link ViewAnimator}
     * @see #script
     */
    public synchronized void pageTo(Page page){

        if (screenshot_ext){

            return;
        }
        else {

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

                    Docking.StartActivity2D();
                }
            }
        }
    }
    /**
     * @see View3DScreenShot
     * @see DockingPostScreenShot
     */
    public boolean screenshot_ext(int format, int type, ByteBuffer buffer)
        throws InterruptedException
    {

        this.screenshot_extFormat = format;
        this.screenshot_extType = type;
        this.screenshot_extBuffer = buffer;
        this.screenshot_ext = true;

        synchronized(screenshot_extMonitor){

            screenshot_extMonitor.wait(1000L);
        }

        return (!screenshot_ext);
    }
    /**
     * Renderer
     */
    public void onDrawFrame(GL10 gl){
        if (screenshot_ext){
            try {
                glReadPixels(0,0,width,height,screenshot_extFormat,screenshot_extType,screenshot_extBuffer);
            }
            finally {
                this.screenshot_ext = false;
            }
            synchronized(screenshot_extMonitor){

                screenshot_extMonitor.notifyAll();
            }
        }
        else {

            // if (null != screenshot_movie){

            //     screenshot_movie.frame();
            // }

            if (plumb){

                if (stale){
                    stale = false;

                    page.init(gl);
                }
                page.draw(gl);
            }
            /*
             * else (activity exiting)
             */
        }
    }
    /**
     * Renderer
     */
    public void onSurfaceChanged(GL10 gl, int width, int height){
    }
    /**
     * Renderer
     */
    public void onSurfaceCreated(GL10 gl, EGLConfig config){
    }
    @Override
    public String toString(){
        return logPrefix;
    }
    protected synchronized ViewPage3D currentPage(){

        ViewPage3D page = this.page;

        if (null == page)
            throw new IllegalStateException(this.logPrefix+this.pageId.name());
        else {
            return page;
        }
    }
}
