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
import android.view.InputDevice;
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

    private final static long InputFilterGeneric = 100L;

    private final static long InputFilterGesture = 150L;


    protected final String objectIdentity, logPrefix;

    private final GestureDetector touch;

    private final SurfaceHolder holder;

    protected final View3DRenderer renderer;

    private SharedPreferences preferences;

    private long inputFilter = 0L;



    public View3D(ObjectActivity context){
        super(context);

        objectIdentity = String.valueOf(System.identityHashCode(this));

        logPrefix = "View3D#"+objectIdentity+' ';

        touch = new GestureDetector(context,this);

        holder = getHolder();

        renderer = new View3DRenderer(this);

        setRenderer(renderer);

        holder.addCallback(renderer);
        holder.addCallback(context);

        holder.setKeepScreenOn(true);

        this.setFocusable(true);// enable key events
    }


    public final boolean is2D(){
        return false;
    }
    public final boolean is3D(){
        return true;
    }
    public final Page currentPage(){
        ViewPage3D page = renderer.page;
        if (null != page)
            return page.value();
        else
            return null;
    }
    public SharedPreferences preferences(){

        return this.preferences;
    }
    /**
     * Occurs before surface created
     */
    public void onCreate(SharedPreferences state){

        this.preferences = state;

        this.renderer.onCreate(state);
    }
    public void onResume(){

        this.renderer.onResume();
    }
    public void onPause(SharedPreferences.Editor state){

        this.holder.setKeepScreenOn(false);

        this.renderer.onPause(state);
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

        if (renderer.plumb){

            if (renderer.pageId.simpleInput){

                touch.onTouchEvent(event);
            }
            else {

                script(generic(event));
            }
            return true;
        }
        else {

            return false;
        }
    }
    /**
     * @see android.view.GestureDetector$OnGestureListener
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        return false;
    }
    public void onLongPress(MotionEvent e) {

        final long eventTime = e.getEventTime();

        if (inputFilter < eventTime){

            inputFilter = (eventTime + InputFilterGesture);

            script(Input.Enter);
        }
    }
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float dx, float dy)
    {
        script(gesture(e2.getEventTime(),dx,dy));

        return true;
    }
    public boolean onFling(MotionEvent e1, MotionEvent e2,
                           float dx, float dy)
    {
        script(gesture(e2.getEventTime(),dx,dy));

        return true;
    }
    public void onShowPress(MotionEvent e){
    }
    public boolean onDown(MotionEvent e){
        return false;
    }
    /**
     * @see android.view.GestureDetector$OnDoubleTapListener
     */
    public boolean onSingleTapConfirmed(MotionEvent e){

        final long eventTime = e.getEventTime();

        if (inputFilter < eventTime){

            inputFilter = (eventTime + InputFilterGesture);

            script(Input.Enter);
        }
        return true;
    }
    public boolean onDoubleTap(MotionEvent e){

        Docking.ScreenShot3D(true);

        return true;
    }
    public boolean onDoubleTapEvent(MotionEvent e){

        return false;
    }
    @Override
    public boolean onTrackballEvent(MotionEvent event){

        if (renderer.plumb){

            script(generic(event));

            return true;
        }
        else {
            return false;
        }
    }
    @Override
    public boolean onGenericMotionEvent(MotionEvent event){

        if (renderer.plumb){

            script(generic(event));

            return true;
        }
        else {
            return false;
        }
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

            final ViewPage3D page = renderer.currentPage();

            ViewAnimation.Script(page,new InputScript[]{in});
        }
    }
    @Override
    public void script(InputScript[] in){

        final ViewPage3D page = renderer.currentPage();

        ViewAnimation.Script(page,in);
    }
    protected void script(char key){

        final ViewPage3D page = renderer.currentPage();

        ViewAnimation.Script(page,new InputScript[]{new InputScript.Key(key)});
    }
    protected Display display(){

        return ((ObjectActivity)getContext()).display();
    }
    protected DisplayMetrics displayMetrics(){

        return ((ObjectActivity)getContext()).displayMetrics();
    }
    @Override
    public String toString(){
        return logPrefix;
    }

    private final InputScript[] gesture(long eventTime, float dx, float dy){

        if (inputFilter < eventTime){

            inputFilter = (eventTime + InputFilterGesture);

            /*
             * Relative coordinate space for gestures
             */
            if (Math.abs(dx) > Math.abs(dy)){

                if (0.0f < dx){

                    return new InputScript[]{Input.Left};
                }
                else {
                    return new InputScript[]{Input.Right};
                }
            }
            else if (0.0f < dy){

                return new InputScript[]{Input.Up};
            }
            else {
                return new InputScript[]{Input.Down};
            }
        }
        return null;
    }
    /**
     * Called from {@link ViewAnimation} to convert pointer activity
     * to navigation activity for subsequent delivery to the input
     * method.
     */
    private final InputScript[] generic(MotionEvent event){

        final long eventTime = event.getEventTime();

        if (inputFilter < eventTime){

            inputFilter = (eventTime + InputFilterGeneric);

            if (0 != (event.getSource() & InputDevice.SOURCE_CLASS_POINTER)){
                /*
                 *  Absolute coordinate space
                 */
                switch(event.getActionMasked()){
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_MOVE:
                    {
                        final float[] xy = Convert(event);

                        if (null != xy && null != renderer.page){

                            return add(null,xy[0],xy[1],Float.MAX_VALUE,renderer.page.current);
                        }
                        else {
                            return null;
                        }
                    }
                default:
                    break;
                }
            }
            else {
                /*
                 * Relative coordinate space
                 */
                final int px = event.getActionIndex();

                final float dx = event.getX(px);
                final float dy = event.getY(px);

                if (0.0f != dx || 0.0f != dy){

                    if (Math.abs(dx) > Math.abs(dy)){

                        if (0.0f < dx){

                            return new InputScript[]{Input.Left};
                        }
                        else {
                            return new InputScript[]{Input.Right};
                        }
                    }
                    else if (0.0f > dy){

                        return new InputScript[]{Input.Up};
                    }
                    else {
                        return new InputScript[]{Input.Down};
                    }
                }
            }
        }
        return null;
    }
    /**
     * Append to list while "distance" is decreasing or direction is "enter".
     */
    private InputScript[] add(InputScript[] list, float x, float y, float distance, ViewPage3DComponent current){

        if (null != current){

            final Input dir = current.direction(x,y);

            if (null == dir){

                return list;
            }
            else if (Input.Enter == dir){
                /*
                 * Separate ENTER from selection process
                 */
                if (null == list){

                    return View.Script.Enter();
                }
                else {
                    return list;
                }
            }
            else {
                final float dis = current.distance(x,y);

                if (dis < distance){
                    /*
                     * Visual code generation to not repeat {Deemphasis}
                     */
                    final InputScript[] add = View.Script.Direction(dir);

                    if (null == list){

                        list = add;
                    }
                    else {
                        list = Input.Add(list,add);
                    }

                    return add(list,x,y,dis,current.getCardinal(dir));
                }
            }
        }
        return list;
    }

    protected void verbose(String m){
        Log.i(TAG,(logPrefix+m));
    }
    protected void verbose(String m, Throwable t){
        Log.i(TAG,(logPrefix+m),t);
    }
    protected void debug(String m){
        Log.d(TAG,(logPrefix+m));
    }
    protected void debug(String m, Throwable t){
        Log.d(TAG,(logPrefix+m),t);
    }
    protected void info(String m){
        Log.i(TAG,(logPrefix+m));
    }
    protected void info(String m, Throwable t){
        Log.i(TAG,(logPrefix+m),t);
    }
    protected void warn(String m){
        Log.w(TAG,(logPrefix+m));
    }
    protected void warn(String m, Throwable t){
        Log.w(TAG,(logPrefix+m),t);
    }
    protected void error(String m){
        Log.e(TAG,(logPrefix+m));
    }
    protected void error(String m, Throwable t){
        Log.e(TAG,(logPrefix+m),t);
    }
    protected void wtf(String m){
        Log.wtf(TAG,(logPrefix+m));
    }
    protected void wtf(String m, Throwable t){
        Log.wtf(TAG,(logPrefix+m),t);
    }

    protected static float[] Convert(MotionEvent event){
        if (1 == event.getPointerCount()){

            float[] re = new float[2];
            {
                re[0] = event.getX(0);
                re[1] = event.getY(0);
            }
            return re;
        }
        else {
            return null;
        }
    }
}
