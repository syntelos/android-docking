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
import android.view.GestureDetector;
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
    implements View, 
               GestureDetector.OnGestureListener, 
               GestureDetector.OnDoubleTapListener
{
    public final static String TAG = ObjectLog.TAG;


    private final GestureDetector touch;

    private final SurfaceHolder holder;

    private SharedPreferences preferences;

    protected volatile Page pageId;

    private volatile ViewPage2D page;

    private int bg = Color.WHITE;

    private boolean plumb = false;

    private int width = -1, height = -1;



    public View2D(Activity context){
        super(context);

        touch = new GestureDetector(context,this);

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
        //info("onCreate");

        this.preferences = state;
        /*
         * Prevent continuations across app state boundaries while
         * onPause(N) can follow onCreate(N+1) [in actual time]
         * 
         * onCreate(N)
         * // ? onPause(N)
         * onCreate(N+1)
         * // ? onPause(N)
         */
        ViewAnimation.Stop();

        DockingPhysics.Stop();
    }
    public void onResume(){
        //info("onResume");

        ViewAnimation.Start(this);

        pageTo(Page.valueOf(preferences.getString("page","start")));

        DockingGeometryPort.Init();

        DockingGeometryStarfield.Init();
    }
    public void onPause(SharedPreferences.Editor state){
        //info("onPause");

        if (null != this.pageId){

            state.putString("page",this.pageId.name());

            if (null != this.page){

                this.page.down(state);
            }
        }

        this.plumb = false;

        ViewAnimation.Stop();
    }
    public void surfaceCreated(SurfaceHolder holder){
        //info("surfaceCreated");

        this.plumb = false;
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
        //info("surfaceChanged");

        this.width = w;
        this.height = h;

        this.plumb = true;

        if (null != this.page){

            this.page.up(this,width,height);
        }
        this.repaint();
    }
    public void surfaceDestroyed(SurfaceHolder holder){
        //info("surfaceDestroyed");

        this.plumb = false;
    }
    /**
     * Works on Sony-TV, but not MB860
     * 
     * The back button or input script should always have the same
     * effect as on devices where the back button operates directly on
     * the activity stack (without passing through the View key event
     * process).

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {

        if (KeyEvent.KEYCODE_BACK == keyCode){
            switch(event.getAction()){

            case KeyEvent.ACTION_DOWN:

                return (Page.start != this.pageId);

            case KeyEvent.ACTION_UP:

                if (Page.start != this.pageId){

                    script(Input.Back);
                    return true;
                }
                else {
                    return false;
                }
            default:
                break;
            }
        }
        return false;
    }
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        switch(keyCode){

        case KeyEvent.KEYCODE_BACK:
            return false;

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

        case KeyEvent.KEYCODE_BACK:
            return false;

        case KeyEvent.KEYCODE_SOFT_LEFT:
        case KeyEvent.KEYCODE_DPAD_LEFT:
        case KeyEvent.KEYCODE_ALT_LEFT:
        case KeyEvent.KEYCODE_SHIFT_LEFT:
        case KeyEvent.KEYCODE_LEFT_BRACKET:
        case KeyEvent.KEYCODE_CTRL_LEFT:
        case KeyEvent.KEYCODE_META_LEFT:

            if (this.pageId.simpleInput){

                script(Input.Left);
            }
            else {
                script(View.Script.Direction(Input.Left));
            }
            return true;

        case KeyEvent.KEYCODE_SOFT_RIGHT:
        case KeyEvent.KEYCODE_DPAD_RIGHT:
        case KeyEvent.KEYCODE_ALT_RIGHT:
        case KeyEvent.KEYCODE_SHIFT_RIGHT:
        case KeyEvent.KEYCODE_RIGHT_BRACKET:
        case KeyEvent.KEYCODE_CTRL_RIGHT:
        case KeyEvent.KEYCODE_META_RIGHT:

            if (this.pageId.simpleInput){

                script(Input.Right);
            }
            else {
                script(View.Script.Direction(Input.Right));
            }
            return true;

        case KeyEvent.KEYCODE_DPAD_UP:
        case KeyEvent.KEYCODE_PAGE_UP:

            script(View.Script.Direction(Input.Up));
            return true;

        case KeyEvent.KEYCODE_DPAD_DOWN:
        case KeyEvent.KEYCODE_PAGE_DOWN:

            if (this.pageId.simpleInput){

                script(Input.Down);
            }
            else {
                script(View.Script.Direction(Input.Down));
            }
            return true;

        case KeyEvent.KEYCODE_DPAD_CENTER:
        case KeyEvent.KEYCODE_ENTER:

            if (this.pageId.simpleInput){

                script(Input.Enter);
            }
            else {
                script(View.Script.Enter());
            }
            return true;

        default:

            if ((!this.pageId.simpleInput) && event.isPrintingKey()){

                script((char)event.getUnicodeChar());

                return true;
            }
            else {
                return false;
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){

        if (this.pageId.simpleInput){

            touch.onTouchEvent(event);
        }
        else {

            script(event);
        }
        return true;
    }
    /**
     * @see android.view.GestureDetector$OnGestureListener
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //info("onSingleTapUp");

        return false;
    }
    public void onLongPress(MotionEvent e) {
        //info("onLongPress {Enter}");

        script(Input.Enter);
    }
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float dx, float dy)
    {
        //info("onScroll");

        /*
         * Relative coordinate space for gestures
         */
        if (Math.abs(dx) > Math.abs(dy)){

            if (0.0f > dx){

                script(Input.Left);
            }
            else {
                script(Input.Right);
            }
        }
        else if (0.0f < dy){

            script(Input.Up);
        }
        else {
            script(Input.Down);
        }
        return true;
    }
    public boolean onFling(MotionEvent e1, MotionEvent e2,
                           float dx, float dy)
    {
        //info("onFling");

        /*
         * Relative coordinate space for gestures
         */
        if (Math.abs(dx) > Math.abs(dy)){

            if (0.0f > dx){

                script(Input.Left);
            }
            else {
                script(Input.Right);
            }
        }
        else if (0.0f < dy){

            script(Input.Up);
        }
        else {
            script(Input.Down);
        }
        return true;
    }
    public void onShowPress(MotionEvent e){
        //info("onShowPress");

    }
    public boolean onDown(MotionEvent e){
        //info("onDown");

        return false;
    }
    /**
     * @see android.view.GestureDetector$OnDoubleTapListener
     */
    public boolean onSingleTapConfirmed(MotionEvent e){
        //info("onSingleTapConfirmed {Enter}");

        script(Input.Enter);

        return true;
    }
    public boolean onDoubleTap(MotionEvent e){
        //info("onDoubleTap {Enter}");

        if (Page.about == this.pageId){

            Docking.Post2D(new DockingPostStartModel());
        }
        return true;
    }
    public boolean onDoubleTapEvent(MotionEvent e){
        //info("onDoubleTapEvent");

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
     * Called from {@link ViewAnimator}
     * @see #script
     */
    public void pageTo(Page page){

        //info("pageTo "+page);

        if (null == page){

            return;
        }
        else if (null != this.page){

            if (page.page != this.page){

                this.page.down();
                try {
                    this.pageId = page;

                    this.page = (ViewPage2D)page.page;

                    if (this.plumb){

                        this.page.up(this,width,height);
                    }
                }
                catch (ClassCastException exc){

                    this.page = null;

                    //warn("switching to 3D for page: "+page);

                    Docking.StartActivity3D();
                }
            }
        }
        else {
            try {
                this.pageId = page;

                this.page = (ViewPage2D)page.page;

                if (this.plumb){

                    this.page.up(this,width,height);
                }
            }
            catch (ClassCastException exc){

                this.page = null;

                //warn("switching to 3D for page: "+page);

                Docking.StartActivity3D();
            }
        }
    }
    /**
     * Repaint (2D only)
     */
    protected void repaint(){

        ViewAnimation.Script();
    }
    /**
     * Application access to {@link #pageTo} for animated interaction.
     */
    @Override
    public void script(Page page){

        ViewAnimation.Script(page);
    }
    /**
     * General access to animated input.
     */
    @Override
    public void script(InputScript in){

        if (null != in){

            ViewAnimation.Script(page,new InputScript[]{in});
        }
    }
    @Override
    public void script(InputScript[] in){

        ViewAnimation.Script(page,in);
    }
    protected void script(MotionEvent event){

        ViewAnimation.Script(page,event);
    }
    protected void script(char key){

        ViewAnimation.Script(page,key);
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
        Log.i(TAG,("View2D "+m));
    }
    protected void verbose(String m, Throwable t){
        Log.i(TAG,("View2D "+m),t);
    }
    protected void debug(String m){
        Log.d(TAG,("View2D "+m));
    }
    protected void debug(String m, Throwable t){
        Log.d(TAG,("View2D "+m),t);
    }
    protected void info(String m){
        Log.i(TAG,("View2D "+m));
    }
    protected void info(String m, Throwable t){
        Log.i(TAG,("View2D "+m),t);
    }
    protected void warn(String m){
        Log.w(TAG,("View2D "+m));
    }
    protected void warn(String m, Throwable t){
        Log.w(TAG,("View2D "+m),t);
    }
    protected void error(String m){
        Log.e(TAG,("View2D "+m));
    }
    protected void error(String m, Throwable t){
        Log.e(TAG,("View2D "+m),t);
    }
    protected void wtf(String m){
        Log.wtf(TAG,("View2D "+m));
    }
    protected void wtf(String m, Throwable t){
        Log.wtf(TAG,("View2D "+m),t);
    }
}
