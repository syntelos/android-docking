/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
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

    protected final static long OutputFilter = 20L;

    private final static int SKIP_NOT = 0;
    private final static int SKIP_SKIP = 1;
    private final static int SKIP_OP = 2;

    private final static Object StaticMonitor = ViewAnimation.class;

    /**
     * 
     */
    public final static class Shutdown
        extends RuntimeException
    {
        public Shutdown(){
            super();
        }
    }


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
            else if (null != Instance && view != Instance.view){
                try {
                    Instance.shutdown();
                }
                finally {
                    Instance = new ViewAnimation(view);
                    Instance.start();
                }
            }
            else {
                //Warn("<Start>");
            }
        }
    }
    /**
     * Used by {@link View}
     */
    protected static void Stop(View view){

        synchronized(StaticMonitor){
            if (null != Instance && view == Instance.view){
                try {
                    Instance.shutdown();
                }
                finally {
                    Instance = null;
                }
            }
            else {
                //Warn("<Stop>");
            }
        }
    }
    private static void Exit(ViewAnimation from){
        synchronized(StaticMonitor){
            ViewAnimation instance = Instance;
            if (null != instance && from == instance){

                Instance = null;
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
            //Warn("script: dropped page-to");
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
            //Warn("script: dropped page-to");
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
            //Warn("script: dropped input");
        }
    }


    /**
     * Animation event used by {@link ViewAnimation}
     */
    private final static class Script {

        private Script head;

        private Page pageTo;

        private ViewPage page;

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

    private volatile boolean running;

    private boolean recover2D;


    private ViewAnimation(View view){
        super("View/Animation");
        setPriority(8);
        if (null != view){
            this.view = view;
            this.holder = view.getHolder();
            this.is2D = view.is2D();
            this.is3D = view.is3D();
            this.queue = null;
            this.running = true;
            this.recover2D = false;
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

            final long outputFilter = DockingGameClock.millis(OutputFilter,is3D);

            int skip = 0;

            this.paint();

            while (running){

                if (recover2D){

                    sleep(outputFilter);

                    this.paint();
                }
                else {
                    Script sequence = this.queue;
                    this.queue = null;

                    if (null == sequence){

                        synchronized(this.monitor){

                            this.monitor.wait();
                        }
                    }
                    else {

                        while (null != sequence){

                            if (null != sequence.pageTo){

                                info("pageTo "+sequence.pageTo);

                                /*
                                 * pageTo
                                 */
                                view.pageTo(sequence.pageTo);
                            }
                            else if (null != sequence.page && view.currentPage() == sequence.page.value()){
                                /*
                                 * input script
                                 */
                                InputScript[] script = sequence.input;

                                if (null != script){

                                    final ViewPage page = sequence.page;

                                    final int count = script.length;

                                    for (int cc = 0; cc < count; cc++){

                                        if (0 != cc){
                                            switch (skip){
                                            case SKIP_NOT:
                                                /*
                                                 * Tail paint and sleep
                                                 */
                                                this.paint();

                                                sleep(outputFilter);
                                                break;
                                            case SKIP_SKIP:
                                                skip = SKIP_OP;
                                                break;
                                            default:
                                                skip = SKIP_NOT;
                                                break;
                                            }
                                        }

                                        InputScript in = script[cc];

                                        if (in.isEval()){

                                            try {
                                                Page pageTo = ((InputScript.Eval)in).eval();

                                                if (null != pageTo){

                                                    info("eval "+in+" returned "+pageTo);

                                                    view.pageTo(pageTo);
                                                }
                                                else {
                                                    info("eval "+in+" returned <null>");
                                                }
                                            }
                                            catch (Shutdown exi){

                                                info("shutdown via eval "+in,exi);

                                                return;
                                            }
                                            catch (Exception exc){

                                                error("error in eval "+in,exc);
                                            }
                                        }
                                        else {
                                            try {
                                                info("input "+in+" to "+page);

                                                page.input(in);
                                            }
                                            catch (Shutdown exi){

                                                info("shutdown via input "+in+" to "+page,exi);

                                                return;
                                            }
                                            catch (Exception exc){

                                                error("error in input "+in+" to "+page,exc);
                                            }
                                        }

                                        if (in.isSkipping()){
                                            skip = SKIP_SKIP;
                                        }
                                    }
                                }
                            }
                            else if (null != sequence.page){

                                final InputScript[] script = sequence.input;

                                if (null != script){
                                    final String prefix = "drop script page "+sequence.page.name()+' ';

                                    for (InputScript in: script){
                                        warn(prefix+in);
                                    }
                                }
                            }
                            else {

                                final InputScript[] script = sequence.input;

                                if (null != script){
                                    final String prefix = "drop script <*> ";

                                    for (InputScript in: script){
                                        warn(prefix+in);
                                    }
                                }
                            }

                            sequence = sequence.pop();

                            /*
                             * Tail paint and sleep
                             */
                            this.paint();

                            if (null != sequence){

                                sleep(outputFilter);
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

            Exit(this);
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
        Page page = view.currentPage();
        Log.i(TAG,"View/Animation "+((null != page)?(page.name()):("<*>"))+' '+m);
    }
    protected void verbose(String m, Throwable t){
        Page page = view.currentPage();
        Log.i(TAG,"View/Animation "+((null != page)?(page.name()):("<*>"))+' '+m,t);
    }
    protected void debug(String m){
        Page page = view.currentPage();
        Log.d(TAG,"View/Animation "+((null != page)?(page.name()):("<*>"))+' '+m);
    }
    protected void debug(String m, Throwable t){
        Page page = view.currentPage();
        Log.d(TAG,"View/Animation "+((null != page)?(page.name()):("<*>"))+' '+m,t);
    }
    protected void info(String m){
        Page page = view.currentPage();
        Log.i(TAG,"View/Animation "+((null != page)?(page.name()):("<*>"))+' '+m);
    }
    protected void info(String m, Throwable t){
        Page page = view.currentPage();
        Log.i(TAG,"View/Animation "+((null != page)?(page.name()):("<*>"))+' '+m,t);
    }
    protected void warn(String m){
        Page page = view.currentPage();
        Log.w(TAG,"View/Animation "+((null != page)?(page.name()):("<*>"))+' '+m);
    }
    protected void warn(String m, Throwable t){
        Page page = view.currentPage();
        Log.w(TAG,"View/Animation "+((null != page)?(page.name()):("<*>"))+' '+m,t);
    }
    protected void error(String m){
        Page page = view.currentPage();
        Log.e(TAG,"View/Animation "+((null != page)?(page.name()):("<*>"))+' '+m);
    }
    protected void error(String m, Throwable t){
        Page page = view.currentPage();
        Log.e(TAG,"View/Animation "+((null != page)?(page.name()):("<*>"))+' '+m,t);
    }
    protected void wtf(String m){
        Page page = view.currentPage();
        Log.wtf(TAG,"View/Animation "+((null != page)?(page.name()):("<*>"))+' '+m);
    }
    protected void wtf(String m, Throwable t){
        Page page = view.currentPage();
        Log.wtf(TAG,"View/Animation "+((null != page)?(page.name()):("<*>"))+' '+m,t);
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
