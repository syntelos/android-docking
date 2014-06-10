/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ConfigurationInfo;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import static android.opengl.GLES11.*;
import static android.opengl.GLES11Ext.*;
import android.util.Log;
import android.view.Display;
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
    implements View
{
    protected final static String TAG = ObjectLog.TAG;


    private SharedPreferences preferences;

    private SurfaceHolder holder;

    private View3DRenderer renderer;

    private View3DInputConnection input_connection;


    public View3D(ObjectActivity context){
        super(context);

        holder = getHolder();

        renderer = new View3DRenderer(this);

        setRenderer(renderer);

        holder.addCallback(renderer);

        holder.setKeepScreenOn(true);

        setFocusable(true); // keyboard
        setFocusableInTouchMode(true); // keyboard
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
    protected synchronized void physicsUpdate(){

        renderer.physicsUpdate();
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

        DockingPhysics.Start(this.preferences);
    }
    public void onPause(SharedPreferences.Editor state){
        info("onPause");

        DockingPhysics.Stop(state);

        this.renderer.onPause(state);

        ViewAnimation.Stop();
    }
    /**
     * Works on Sony-TV, but not MB860
     */
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        switch(keyCode){

        case KeyEvent.KEYCODE_BACK:
            return true;

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
            script(Input.Back);
            return true;

        case KeyEvent.KEYCODE_SOFT_LEFT:
        case KeyEvent.KEYCODE_DPAD_LEFT:
        case KeyEvent.KEYCODE_ALT_LEFT:
        case KeyEvent.KEYCODE_SHIFT_LEFT:
        case KeyEvent.KEYCODE_LEFT_BRACKET:
        case KeyEvent.KEYCODE_CTRL_LEFT:
        case KeyEvent.KEYCODE_META_LEFT:

            script(View.Script.Direction(Input.Left));
            return true;

        case KeyEvent.KEYCODE_SOFT_RIGHT:
        case KeyEvent.KEYCODE_DPAD_RIGHT:
        case KeyEvent.KEYCODE_ALT_RIGHT:
        case KeyEvent.KEYCODE_SHIFT_RIGHT:
        case KeyEvent.KEYCODE_RIGHT_BRACKET:
        case KeyEvent.KEYCODE_CTRL_RIGHT:
        case KeyEvent.KEYCODE_META_RIGHT:

            script(View.Script.Direction(Input.Right));
            return true;

        case KeyEvent.KEYCODE_DPAD_UP:
        case KeyEvent.KEYCODE_PAGE_UP:

            script(View.Script.Direction(Input.Up));
            return true;

        case KeyEvent.KEYCODE_DPAD_DOWN:
        case KeyEvent.KEYCODE_PAGE_DOWN:

            script(View.Script.Direction(Input.Down));
            return true;

        case KeyEvent.KEYCODE_DPAD_CENTER:
        case KeyEvent.KEYCODE_ENTER:

            script(View.Script.Enter());
            return true;

        default:
            if (event.isPrintingKey()){

                script((char)event.getUnicodeChar());
            }
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
     * Support for soft keyboard
     */
    public boolean onCheckIsTextEditor() {

        info("onCheckIsTextEditor");

        return true;
    }
    /**
     * Support for soft keyboard
     */
    public InputConnection onCreateInputConnection(EditorInfo attrs) {

        info("onCreateInputConnection");

        if (null == this.input_connection){
            this.input_connection = new View3DInputConnection(this);
        }

        //attrs.imeOptions = EditorInfo.IME_ACTION_DONE;

        return this.input_connection;
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
}
