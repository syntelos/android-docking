/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.util.Log;

/**
 * Convert pointer input to serial input with animation.
 * 
 * Track and visualize pointer input in a way that's compatible with
 * native serial input.
 * 
 * <h3>Interactive Display (I/O)</h3>
 * 
 * Input and (visual) state change events are processed with repaints.
 * 
 * The animator manages input and output at frequencies intended to
 * provide a fairly consistent user experience over input and host
 * devices.
 * 
 */
public final class ViewAnimation
    extends java.lang.Thread
{
    public final static String TAG = ObjectLog.TAG;


    protected final static long TouchInputFilter = 300L;

    protected final static long OutputFilter = 20L;

    private final static int SKIP_NOT = 0;
    private final static int SKIP_SKIP = 1;
    private final static int SKIP_OP = 2;


    private final static Object StaticMonitor = ViewAnimation.class;


    private volatile static ViewAnimation Instance;


    /**
     * Used by {@link View}
     */
    protected static void Start(View view){

        synchronized(StaticMonitor){
            if (null == Instance){

                Instance = new ViewAnimation(view);
                Instance.start();
            }
            else {
                Warn("start: not running");
            }
        }
    }
    /**
     * Used by {@link View}
     */
    protected static void Stop(){

        synchronized(StaticMonitor){
            if (null != Instance){
                try {
                    Instance.shutdown();
                }
                finally {
                    Instance = null;
                }
            }
            else {
                Warn("stop: not shutdown");
            }
        }
    }
    /**
     * Used by {@link View}
     */
    protected static void Script(Page page){

        if (null != Instance){

            Instance.script(page);
        }
        else {
            Warn("script: dropped page-to");
        }
    }
    /**
     * Used by {@link View2D} for repaint
     */
    protected static void Script(){

        if (null != Instance){

            Instance.script();
        }
        else {
            Warn("script: dropped page-to");
        }
    }
    /**
     * Used by {@link View}
     */
    protected static void Script(ViewPage page, InputScript[] in){

        if (null != Instance){

            Instance.script(page,in);
        }
        else {
            Warn("script: dropped input");
        }
    }
    /**
     * Used by {@link View}
     */
    protected static void Script(ViewPage page, MotionEvent event){

        if (null != Instance){

            Instance.script(page,event);
        }
        else {
            Warn("script: dropped motion");
        }
    }
    /**
     * Used by {@link View}
     */
    protected static void Script(ViewPage page, char key){

        if (null != Instance){

            Instance.script(page,key);
        }
        else {
            Warn("script: dropped key");
        }
    }

    /**
     * Animation event used by {@link ViewAnimation}
     */
    private final static class Script {

        private Script head;

        private Page pageTo;

        private ViewPage page;

        private MotionEvent motion;

        private InputScript[] input;

        private Script next;


        private Script(Script head){
            super();

            this.head = head;

            if (null != head){

                head.append(this);
            }
        }
        private Script(Script head, Page pageTo){
            this(head);

            this.pageTo = pageTo;
        }
        private Script(Script head, ViewPage page, MotionEvent motion){
            this(head);

            this.page = page;
            this.motion = motion;
        }
        private Script(Script head, ViewPage page, char key){
            this(head);

            this.page = page;
            this.input = new InputScript[]{new InputScript.Key(key)};
        }
        private Script(Script head, ViewPage page, InputScript[] input){
            this(head);

            this.page = page;
            this.input = input;
        }


        private Script head(){
            if (null == this.head)
                return this;
            else
                return this.head;
        }
        private Script append(Script tail){
            if (null != this.next){
                return this.next.append(tail);
            }
            else {
                this.next = tail;
                return this;
            }
        }
        private Script pop(){
            Script re = next;
            {
                this.head = null;
                this.next = null;
            }
            return re;
        }
    }


    private final View view;

    private final SurfaceHolder holder;

    private final boolean is2D, is3D;

    private final Object monitor = new Object();

    private volatile Script queue;

    private volatile boolean running = true;

    private boolean recover2D = false;


    private ViewAnimation(View view){
        super("View/Animation");
        if (null != view){
            this.view = view;
            this.holder = view.getHolder();
            this.is2D = view.is2D();
            this.is3D = view.is3D();
        }
        else {
            throw new IllegalArgumentException();
        }
    }


    private void script(Page pageTo){

        this.queue = (new Script(this.queue,pageTo)).head();

        synchronized(this.monitor){

            this.monitor.notify();
        }
    }
    /**
     * Repaint (2D only)
     */
    private void script(){

        this.queue = (new Script(this.queue)).head();

        synchronized(this.monitor){

            this.monitor.notify();
        }
    }
    private void script(ViewPage page, MotionEvent motion){

        this.queue = (new Script(this.queue,page,motion)).head();

        synchronized(this.monitor){

            this.monitor.notify();
        }
    }
    private void script(ViewPage page, char key){

        this.queue = (new Script(this.queue,page,key)).head();

        synchronized(this.monitor){

            this.monitor.notify();
        }
    }
    private void script(ViewPage page, InputScript[] input){

        this.queue = (new Script(this.queue,page,input)).head();

        synchronized(this.monitor){

            this.monitor.notify();
        }
    }
    private void shutdown(){

        this.running = false;

        synchronized(this.monitor){

            this.monitor.notify();
        }
    }
    public void run(){
        try {
            info("running");

            long touchInputFilter = 0L;

            int skip = 0;

            while (running){

                if (recover2D){

                    sleep(OutputFilter);

                    this.paint();
                }
                else {
                    Script sequence = this.queue;
                    this.queue = null;

                    if (null == sequence){

                        info("waiting");

                        synchronized(this.monitor){

                            this.monitor.wait();
                        }
                    }
                    else {
                        InputScript in = null;

                        while (null != sequence){

                            if (null != sequence.pageTo){

                                info("pageTo");
                                /*
                                 * pageTo
                                 */
                                view.pageTo(sequence.pageTo);
                            }
                            else if (null != sequence.page){
                                /*
                                 * motion and input
                                 */
                                InputScript[] script = null;

                                if (null != sequence.motion){
                                    /*
                                     * touch input filtering
                                     */
                                    if (touchInputFilter < SystemClock.uptimeMillis()){

                                        info("include motion");

                                        script = sequence.page.script(sequence.motion);
                                    }
                                    else {
                                        warn("exclude motion");
                                    }
                                }
                                else if (null != sequence.input){
                                    /*
                                     * touch input filtering
                                     */
                                    if (touchInputFilter < SystemClock.uptimeMillis()){

                                        info("include input");

                                        script = sequence.input;
                                    }
                                    else {
                                        warn("exclude input");
                                    }
                                }


                                if (null != script){

                                    final ViewPage page = sequence.page;

                                    final int count = script.length;

                                    info("exec script "+count);

                                    for (int cc = 0; cc < count; cc++){

                                        if (0 != cc){
                                            switch (skip){
                                            case SKIP_NOT:
                                                /*
                                                 * Tail paint and sleep
                                                 */
                                                this.paint();

                                                sleep(OutputFilter);
                                                break;
                                            case SKIP_SKIP:
                                                skip = SKIP_OP;
                                                break;
                                            default:
                                                skip = SKIP_NOT;
                                                break;
                                            }
                                        }

                                        in = script[cc];

                                        info("input "+in.name()+" to "+page.name());

                                        page.input(in);

                                        if (Input.Skip == in){
                                            skip = SKIP_SKIP;
                                        }
                                    }

                                    touchInputFilter = SystemClock.uptimeMillis()+TouchInputFilter;
                                }
                                else {
                                    warn("ignore script <null>");
                                }
                            }

                            sequence = sequence.pop();

                            /*
                             * Tail paint and sleep
                             */
                            this.paint();

                            if (null != sequence){

                                sleep(OutputFilter);
                            }
                        }
                    }
                }
            }
        }
        catch (InterruptedException inx){
            return;
        }
        finally {
            info("returning");
        }
    }
    private void paint(){

        if (is2D){

            try {
                Canvas g = this.holder.lockCanvas();

                ((View2D)this.view).onDraw(g);

                this.holder.unlockCanvasAndPost(g);

                recover2D = false;
            }
            catch (RuntimeException exc){

                recover2D = true;

                warn("paint",exc);
            }
        }
    }
    protected void verbose(String m){
        Log.i(TAG,"View/Animation "+m);
    }
    protected void verbose(String m, Throwable t){
        Log.i(TAG,"View/Animation "+m,t);
    }
    protected void debug(String m){
        Log.d(TAG,"View/Animation "+m);
    }
    protected void debug(String m, Throwable t){
        Log.d(TAG,"View/Animation "+m,t);
    }
    protected void info(String m){
        Log.i(TAG,"View/Animation "+m);
    }
    protected void info(String m, Throwable t){
        Log.i(TAG,"View/Animation "+m,t);
    }
    protected void warn(String m){
        Log.w(TAG,"View/Animation "+m);
    }
    protected void warn(String m, Throwable t){
        Log.w(TAG,"View/Animation "+m,t);
    }
    protected void error(String m){
        Log.e(TAG,"View/Animation "+m);
    }
    protected void error(String m, Throwable t){
        Log.e(TAG,"View/Animation "+m,t);
    }
    protected void wtf(String m){
        Log.wtf(TAG,"View/Animation "+m);
    }
    protected void wtf(String m, Throwable t){
        Log.wtf(TAG,"View/Animation "+m,t);
    }
    protected static void Verbose(String m){
        Log.i(TAG,"View/Animation "+m);
    }
    protected static void Verbose(String m, Throwable t){
        Log.i(TAG,"View/Animation "+m,t);
    }
    protected static void Debug(String m){
        Log.d(TAG,"View/Animation "+m);
    }
    protected static void Debug(String m, Throwable t){
        Log.d(TAG,"View/Animation "+m,t);
    }
    protected static void Info(String m){
        Log.i(TAG,"View/Animation "+m);
    }
    protected static void Info(String m, Throwable t){
        Log.i(TAG,"View/Animation "+m,t);
    }
    protected static void Warn(String m){
        Log.w(TAG,"View/Animation "+m);
    }
    protected static void Warn(String m, Throwable t){
        Log.w(TAG,"View/Animation "+m,t);
    }
    protected static void Error(String m){
        Log.e(TAG,"View/Animation "+m);
    }
    protected static void Error(String m, Throwable t){
        Log.e(TAG,"View/Animation "+m,t);
    }
    protected static void WTF(String m){
        Log.wtf(TAG,"View/Animation "+m);
    }
    protected static void WTF(String m, Throwable t){
        Log.wtf(TAG,"View/Animation "+m,t);
    }
}
