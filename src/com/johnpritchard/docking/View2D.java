/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ConfigurationInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.util.DisplayMetrics;

/**
 * The Android Activity defines a view when it is created, and calls
 * the {@link #onCreate}, {@link #onResume} and {@link #onPause}
 * methods defined here from its respective lifecycle events.
 * 
 * The view employs a collection of pages which are navigated via the
 * "script" and "pageTo" process implemented via {@link ViewAnimator}.
 * 
 * One {@link ViewPage page} is displayed by {@link View2D} at the
 * exclusion of all other pages, during which time it represents the
 * input and output function of the {@link View2D}.
 * 
 * The {@link Page} enum is employed to represent pages for navigation
 * operations, and the {@link Input} enum is employed to represent
 * associations for navigation operations.
 * 
 * The page I/O model provides equivalence for internal and external
 * access to the operation of the interaction machine.
 * 
 * <h3>Interactive Display (I/O)</h3>
 * 
 * Input and (visual) state change events are processed by the {@link
 * ViewAnimator} (via the "script" methods) with repaints.  The
 * animator interprets UI and script input for the input model
 * (defined here) that includes touch and DPAD devices.
 * 
 * @see ViewPage
 */
public final class View2D
    extends android.view.SurfaceView
    implements View
{
    public final static String TAG = ObjectLog.TAG;


    private final SurfaceHolder holder;

    private SharedPreferences preferences;

    private volatile ViewPage2D page;

    private int bg = Color.WHITE;

    private boolean plumb = false;

    private int width = -1, height = -1;



    public View2D(Activity context){
        super(context);

        holder = getHolder();
        holder.addCallback(this);

        this.setFocusable(true);// enable key events
    }


    public final boolean is2D(){
        return true;
    }
    public final boolean is3D(){
        return false;
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

        this.pageTo(state.getString("main.page","start"));
    }
    public void onResume(){
        info("onResume");

        ViewAnimation.Start(this);
    }
    public void onPause(SharedPreferences.Editor state){
        info("onPause");

        if (null != this.page){

            state.putString("main.page",this.page.name());

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

        this.repaint();
    }
    public void surfaceDestroyed(SurfaceHolder holder){
        info("surfaceDestroyed");

        this.plumb = false;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        switch(keyCode){

        case KeyEvent.KEYCODE_SOFT_LEFT:
        case KeyEvent.KEYCODE_DPAD_LEFT:
        case KeyEvent.KEYCODE_ALT_LEFT:
        case KeyEvent.KEYCODE_SHIFT_LEFT:
        case KeyEvent.KEYCODE_LEFT_BRACKET:
        case KeyEvent.KEYCODE_CTRL_LEFT:
        case KeyEvent.KEYCODE_META_LEFT:
            return true;

        case KeyEvent.KEYCODE_SOFT_RIGHT:
        case KeyEvent.KEYCODE_DPAD_RIGHT:
        case KeyEvent.KEYCODE_ALT_RIGHT:
        case KeyEvent.KEYCODE_SHIFT_RIGHT:
        case KeyEvent.KEYCODE_RIGHT_BRACKET:
        case KeyEvent.KEYCODE_CTRL_RIGHT:
        case KeyEvent.KEYCODE_META_RIGHT:
            return true;

        case KeyEvent.KEYCODE_DPAD_UP:
        case KeyEvent.KEYCODE_PAGE_UP:
            return true;

        case KeyEvent.KEYCODE_DPAD_DOWN:
        case KeyEvent.KEYCODE_PAGE_DOWN:
            return true;

        case KeyEvent.KEYCODE_DPAD_CENTER:
        case KeyEvent.KEYCODE_ENTER:
            return true;

        default:
            return false;
        }
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){

        switch(keyCode){

        case KeyEvent.KEYCODE_SOFT_LEFT:
        case KeyEvent.KEYCODE_DPAD_LEFT:
        case KeyEvent.KEYCODE_ALT_LEFT:
        case KeyEvent.KEYCODE_SHIFT_LEFT:
        case KeyEvent.KEYCODE_LEFT_BRACKET:
        case KeyEvent.KEYCODE_CTRL_LEFT:
        case KeyEvent.KEYCODE_META_LEFT:

            script(ScriptDir(Input.Left));
            return true;

        case KeyEvent.KEYCODE_SOFT_RIGHT:
        case KeyEvent.KEYCODE_DPAD_RIGHT:
        case KeyEvent.KEYCODE_ALT_RIGHT:
        case KeyEvent.KEYCODE_SHIFT_RIGHT:
        case KeyEvent.KEYCODE_RIGHT_BRACKET:
        case KeyEvent.KEYCODE_CTRL_RIGHT:
        case KeyEvent.KEYCODE_META_RIGHT:

            script(ScriptDir(Input.Right));
            return true;

        case KeyEvent.KEYCODE_DPAD_UP:
        case KeyEvent.KEYCODE_PAGE_UP:

            script(ScriptDir(Input.Up));
            return true;

        case KeyEvent.KEYCODE_DPAD_DOWN:
        case KeyEvent.KEYCODE_PAGE_DOWN:

            script(ScriptDir(Input.Down));
            return true;

        case KeyEvent.KEYCODE_DPAD_CENTER:
        case KeyEvent.KEYCODE_ENTER:

            script(ScriptEnt());
            return true;

        default:
            return false;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){

        script(event);

        return true;
    }
    @Override
    public boolean onTrackballEvent(MotionEvent event){

        script(event);

        return true;
    }
    @Override
    public boolean onGenericMotionEvent(MotionEvent event){

        script(event);

        return true;
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

                this.page = (ViewPage2D)page.page;

                if (this.plumb){

                    this.page.up(this,width,height);
                }
            }
        }
        else {
            this.page = (ViewPage2D)page.page;

            if (this.plumb){

                this.page.up(this,width,height);
            }
        }
    }
    protected void repaint(){

        ViewAnimation.Script(page);
    }
    /**
     * Application access to {@link #pageTo} for animated interaction.
     */
    protected void script(Page page){

        ViewAnimation.Script(page);
    }
    /**
     * General access to animated input.
     */
    protected void script(Input in){

        if (null != in){

            ViewAnimation.Script(page,new Input[]{in});
        }
    }
    protected void script(Input[] in){

        ViewAnimation.Script(page,in);
    }
    protected void script(MotionEvent event){

        ViewAnimation.Script(page,event);
    }
    @Override
    protected void onDraw(Canvas g){

        if (null != g){

            g.drawColor(bg);

            ViewPage2D page = this.page;
            if (null != page){

                page.draw(g);
            }
        }
    }
    protected void verbose(String m){
        Log.i(TAG,(getClass().getName()+' '+m));
    }
    protected void verbose(String m, Throwable t){
        Log.i(TAG,(getClass().getName()+' '+m),t);
    }
    protected void debug(String m){
        Log.d(TAG,(getClass().getName()+' '+m));
    }
    protected void debug(String m, Throwable t){
        Log.d(TAG,(getClass().getName()+' '+m),t);
    }
    protected void info(String m){
        Log.i(TAG,(getClass().getName()+' '+m));
    }
    protected void info(String m, Throwable t){
        Log.i(TAG,(getClass().getName()+' '+m),t);
    }
    protected void warn(String m){
        Log.w(TAG,(getClass().getName()+' '+m));
    }
    protected void warn(String m, Throwable t){
        Log.w(TAG,(getClass().getName()+' '+m),t);
    }
    protected void error(String m){
        Log.e(TAG,(getClass().getName()+' '+m));
    }
    protected void error(String m, Throwable t){
        Log.e(TAG,(getClass().getName()+' '+m),t);
    }
    protected void wtf(String m){
        Log.wtf(TAG,(getClass().getName()+' '+m));
    }
    protected void wtf(String m, Throwable t){
        Log.wtf(TAG,(getClass().getName()+' '+m),t);
    }

    protected final static Input[] ScriptDir(Input dir){

        return new Input[]{Input.Skip,dir,Input.Emphasis,Input.Skip,Input.Deemphasis};
    }
    protected final static Input[] ScriptEnt(){

        return new Input[]{Input.Emphasis,Input.Skip,Input.Deemphasis,Input.Enter};
    }
}
