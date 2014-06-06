/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.SharedPreferences;
import android.view.SurfaceHolder;

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

    private View3D view;

    private SharedPreferences preferences;

    private volatile ViewPage3D page;

    private boolean plumb = false;

    private int width = -1, height = -1;


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

        this.pageTo(state.getString("view.page","game"));
    }
    public synchronized void onResume(){
    }
    public synchronized void onPause(SharedPreferences.Editor state){

        if (null != this.page){

            state.putString("view.page",this.page.name());

            this.page.down(state);
        }

        this.plumb = false;
    }
    public synchronized void surfaceCreated(SurfaceHolder holder){

        this.plumb = false;
    }
    public synchronized void surfaceChanged(SurfaceHolder holder, int format, int w, int h){

        this.width = w;
        this.height = h;

        this.plumb = true;

        this.page.up(view,width,height);

    }
    public synchronized void surfaceDestroyed(SurfaceHolder holder){
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
    public synchronized void pageTo(Page page){

        if (null == page){

            return;
        }
        else if (null != this.page){

            if (page.page != this.page){

                this.page.down();

                this.page = (ViewPage3D)page.page;

                if (this.plumb){

                    this.page.up(view,width,height);
                }
            }
        }
        else {
            this.page = (ViewPage3D)page.page;

            if (this.plumb){

                this.page.up(view,width,height);
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

                page.up(view,width,height);

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

            page.up(view,width,height);

            plumb = true;
        }
    }
    /**
     * Renderer
     */
    public void onSurfaceCreated(GL10 gl, EGLConfig config){
    }
}
