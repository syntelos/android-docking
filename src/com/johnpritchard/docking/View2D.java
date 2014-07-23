/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ConfigurationInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.InputDevice;
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

    private final static long InputFilterGeneric = 100L;

    private final static long InputFilterGesture = 150L;


    protected final String objectIdentity, logPrefix;

    private final GestureDetector touch;

    private final SurfaceHolder holder;

    private SharedPreferences preferences;

    protected volatile Page pageId;

    private volatile ViewPage2D page;

    private int bg = Color.WHITE;

    private boolean plumb = false;

    protected int width = -1, height = -1;

    private long inputFilter = 0L;



    public View2D(ObjectActivity context){
        super(context);

        objectIdentity = String.valueOf(System.identityHashCode(this));

        logPrefix = "View2D#"+objectIdentity+' ';

        touch = new GestureDetector(context,this);

        holder = getHolder();
        holder.addCallback(this);
        holder.addCallback(context);

        this.setFocusable(true);// enable key events
    }


    public final boolean is2D(){
        return true;
    }
    public final boolean is3D(){
        return false;
    }
    public final Page currentPage(){
        ViewPage2D page = this.page;
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
    }
    public void onResume(){

        pageTo(Page.start);

        if (plumb){

            ViewAnimation.Start(this);
        }
    }
    public void onPause(SharedPreferences.Editor state){

        ViewAnimation.Stop(this);

        if (null != pageId){
            state.putString("page",pageId.name());
        }
        else {
            state.putString("page","start");
        }

        if (null != this.page){

            this.page.down(state);
        }
        this.plumb = false;
    }
    public void surfaceCreated(SurfaceHolder holder){

        this.plumb = false;
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){

        this.width = w;
        this.height = h;

        this.plumb = true;

        if (null != this.page){

            this.page.up(this,width,height);
        }

        ViewAnimation.Start(this);

    }
    public void surfaceDestroyed(SurfaceHolder holder){

        ViewAnimation.Stop(this);

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

        if (plumb){

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
        else {
            return false;
        }
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){

        if (plumb){

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
        else {
            return false;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){

        if (plumb){

            if (this.pageId.simpleInput){

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

        return true;
    }
    public boolean onDoubleTapEvent(MotionEvent e){

        return true;
    }
    @Override
    public boolean onTrackballEvent(MotionEvent event){

        if (plumb){

            script(generic(event));

            return true;
        }
        else {
            return false;
        }
    }
    @Override
    public boolean onGenericMotionEvent(MotionEvent event){

        if (plumb){

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

                Docking.StartActivity3D();
            }
        }
    }
    @Override
    public String toString(){
        return logPrefix;
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

            final ViewPage2D page = this.page;

            ViewAnimation.Script(page,new InputScript[]{in});
        }
    }
    @Override
    public void script(InputScript[] in){

        final ViewPage2D page = this.page;

        ViewAnimation.Script(page,in);
    }
    protected void script(char key){

        final ViewPage2D page = this.page;

        ViewAnimation.Script(page,new InputScript[]{new InputScript.Key(key)});
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

                        if (null != xy && null != page){

                            return add(null,xy[0],xy[1],Float.MAX_VALUE,page.current);
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
    private InputScript[] add(InputScript[] list, float x, float y, float distance, ViewPage2DComponent current){

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
        Log.i(TAG,(logPrefix+((null != page)?(page.name()):("<*>"))+' '+m));
    }
    protected void verbose(String m, Throwable t){
        Log.i(TAG,(logPrefix+((null != page)?(page.name()):("<*>"))+' '+m),t);
    }
    protected void debug(String m){
        Log.d(TAG,(logPrefix+((null != page)?(page.name()):("<*>"))+' '+m));
    }
    protected void debug(String m, Throwable t){
        Log.d(TAG,(logPrefix+((null != page)?(page.name()):("<*>"))+' '+m),t);
    }
    protected void info(String m){
        Log.i(TAG,(logPrefix+((null != page)?(page.name()):("<*>"))+' '+m));
    }
    protected void info(String m, Throwable t){
        Log.i(TAG,(logPrefix+((null != page)?(page.name()):("<*>"))+' '+m),t);
    }
    protected void warn(String m){
        Log.w(TAG,(logPrefix+((null != page)?(page.name()):("<*>"))+' '+m));
    }
    protected void warn(String m, Throwable t){
        Log.w(TAG,(logPrefix+((null != page)?(page.name()):("<*>"))+' '+m),t);
    }
    protected void error(String m){
        Log.e(TAG,(logPrefix+((null != page)?(page.name()):("<*>"))+' '+m));
    }
    protected void error(String m, Throwable t){
        Log.e(TAG,(logPrefix+((null != page)?(page.name()):("<*>"))+' '+m),t);
    }
    protected void wtf(String m){
        Log.wtf(TAG,(logPrefix+((null != page)?(page.name()):("<*>"))+' '+m));
    }
    protected void wtf(String m, Throwable t){
        Log.wtf(TAG,(logPrefix+((null != page)?(page.name()):("<*>"))+' '+m),t);
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
