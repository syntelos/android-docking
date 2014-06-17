/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ConfigurationInfo;
import static android.opengl.GLES11.*;
import static android.opengl.GLES11Ext.*;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
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
               GestureDetector.OnGestureListener, 
               GestureDetector.OnDoubleTapListener
{
    protected final static String TAG = ObjectLog.TAG;


    private final GestureDetector touch;

    private final SurfaceHolder holder;

    protected final View3DRenderer renderer;

    private SharedPreferences preferences;



    public View3D(ObjectActivity context){
        super(context);

        touch = new GestureDetector(context,this);

        holder = getHolder();

        renderer = new View3DRenderer(this);

        setRenderer(renderer);

        holder.addCallback(renderer);

        holder.setKeepScreenOn(true);

        this.setFocusable(true);// enable key events
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

        //DockingMovie.Stop();

        this.renderer.onCreate(state);
    }
    public void onResume(){
        //info("onResume");

        ViewAnimation.Start(this);

        this.renderer.onResume();
    }
    public void onPause(SharedPreferences.Editor state){
        //info("onPause");

        this.holder.setKeepScreenOn(false);

        this.renderer.onPause(state);

        ViewAnimation.Stop();

        //DockingMovie.Stop();
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
                return true;

            case KeyEvent.ACTION_UP:

                script(Input.Back);
                return true;

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

            script(Input.Left);
            return true;

        case KeyEvent.KEYCODE_SOFT_RIGHT:
        case KeyEvent.KEYCODE_DPAD_RIGHT:
        case KeyEvent.KEYCODE_ALT_RIGHT:
        case KeyEvent.KEYCODE_SHIFT_RIGHT:
        case KeyEvent.KEYCODE_RIGHT_BRACKET:
        case KeyEvent.KEYCODE_CTRL_RIGHT:
        case KeyEvent.KEYCODE_META_RIGHT:

            script(Input.Right);
            return true;

        case KeyEvent.KEYCODE_DPAD_UP:
        case KeyEvent.KEYCODE_PAGE_UP:

            script(Input.Up);
            return true;

        case KeyEvent.KEYCODE_DPAD_DOWN:
        case KeyEvent.KEYCODE_PAGE_DOWN:

            script(Input.Down);
            return true;

        case KeyEvent.KEYCODE_DPAD_CENTER:
        case KeyEvent.KEYCODE_ENTER:

            script(Input.Enter);
            return true;

        default:
            if ((!this.renderer.pageId.simpleInput) && event.isPrintingKey()){

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

        touch.onTouchEvent(event);

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
         * Relative coordinate space [LANDSCAPE]
         */
        if (Math.abs(dx) > Math.abs(dy)){

            if (0.0f < dx){

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
         * Relative coordinate space [LANDSCAPE]
         */
        if (Math.abs(dx) > Math.abs(dy)){

            if (0.0f < dx){

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

        Docking.ScreenShot3D();

        return true;
    }
    public boolean onDoubleTapEvent(MotionEvent e){
        return false;
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

        this.renderer.pageTo(page);
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

            ViewAnimation.Script(renderer.page,new InputScript[]{in});
        }
    }
    @Override
    public void script(InputScript[] in){

        ViewAnimation.Script(renderer.page,in);
    }
    protected void script(MotionEvent event){

        ViewAnimation.Script(renderer.page,event);
    }
    protected void script(char key){

        ViewAnimation.Script(renderer.page,key);
    }
    protected Display display(){

        return ((ObjectActivity)getContext()).display();
    }
    protected DisplayMetrics displayMetrics(){

        return ((ObjectActivity)getContext()).displayMetrics();
    }

    protected void verbose(String m){
        Log.i(TAG,("View3D "+m));
    }
    protected void verbose(String m, Throwable t){
        Log.i(TAG,("View3D "+m),t);
    }
    protected void debug(String m){
        Log.d(TAG,("View3D "+m));
    }
    protected void debug(String m, Throwable t){
        Log.d(TAG,("View3D "+m),t);
    }
    protected void info(String m){
        Log.i(TAG,("View3D "+m));
    }
    protected void info(String m, Throwable t){
        Log.i(TAG,("View3D "+m),t);
    }
    protected void warn(String m){
        Log.w(TAG,("View3D "+m));
    }
    protected void warn(String m, Throwable t){
        Log.w(TAG,("View3D "+m),t);
    }
    protected void error(String m){
        Log.e(TAG,("View3D "+m));
    }
    protected void error(String m, Throwable t){
        Log.e(TAG,("View3D "+m),t);
    }
    protected void wtf(String m){
        Log.wtf(TAG,("View3D "+m));
    }
    protected void wtf(String m, Throwable t){
        Log.wtf(TAG,("View3D "+m),t);
    }
}
