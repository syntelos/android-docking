/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.os.SystemClock;
import android.util.Log;

/**
 * Game play engine.
 */
public final class DockingPhysics
    extends java.lang.Thread
{
    private final static String TAG = ObjectLog.TAG;

    private final static Object StaticMonitor = ViewAnimation.class;

    protected volatile static DockingPhysics Instance;

    public static void Start(){
        synchronized(StaticMonitor){
            if (null == Instance){

                Instance = new DockingPhysics();

                Instance._start();
            }
        }
    }
    public static void Stop(){
        synchronized(StaticMonitor){
            DockingPhysics instance = Instance;
            if (null != instance){
                try {
                    instance._stop();
                }
                finally {
                    Instance = null;
                }
            }
        }
    }
    public static void Script(PhysicsScript in){
        DockingPhysics instance = Instance;
        if (null != instance){

            instance.script(in);
        }
    }

    private final static long TINC = 20L;

    private final static long FCOPY = 500L;



    private final Object monitor = new Object();

    private volatile PhysicsScript queue;

    private volatile boolean running = true;


    private DockingPhysics(){
        super("Phys/Animation");
    }


    private void _start(){

        this.running = true;

        super.start();
    }
    private void _stop(){

        this.running = false;

        this.queue = null;

        synchronized(this.monitor){

            this.monitor.notify();
        }
    }
    private void script(PhysicsScript in){

        this.queue = in.push(this.queue);
    }
    public void run(){
        try {
            //info("running");

            sleep(1500L);

            final DockingCraftStateVector sv = DockingCraftStateVector.Instance;

            while (running){
                {
                    PhysicsScript prog = this.queue;
                    this.queue = null;

                    while (null != prog){

                        sv.add(prog);

                        prog = prog.pop();
                    }
                }

                if (!sv.update(FCOPY)){

                    Docking.Post3D(new DockingPostFinishPhysics());

                    return;
                }
                else {
                    synchronized(this.monitor){
                        this.monitor.wait(TINC);
                    }
                }
            }
        }
        catch (InterruptedException inx){
            return;
        }
        finally {
            //info("returning");

            //Instance = null;
        }
    }
    protected void verbose(String m){
        Log.i(TAG,"Phys/Animation "+m);
    }
    protected void verbose(String m, Throwable t){
        Log.i(TAG,"Phys/Animation "+m,t);
    }
    protected void debug(String m){
        Log.d(TAG,"Phys/Animation "+m);
    }
    protected void debug(String m, Throwable t){
        Log.d(TAG,"Phys/Animation "+m,t);
    }
    protected void info(String m){
        Log.i(TAG,"Phys/Animation "+m);
    }
    protected void info(String m, Throwable t){
        Log.i(TAG,"Phys/Animation "+m,t);
    }
    protected void warn(String m){
        Log.w(TAG,"Phys/Animation "+m);
    }
    protected void warn(String m, Throwable t){
        Log.w(TAG,"Phys/Animation "+m,t);
    }
    protected void error(String m){
        Log.e(TAG,"Phys/Animation "+m);
    }
    protected void error(String m, Throwable t){
        Log.e(TAG,"Phys/Animation "+m,t);
    }
    protected void wtf(String m){
        Log.wtf(TAG,"Phys/Animation "+m);
    }
    protected void wtf(String m, Throwable t){
        Log.wtf(TAG,"Phys/Animation "+m,t);
    }
    protected static void Verbose(String m){
        Log.i(TAG,"Phys/Animation "+m);
    }
    protected static void Verbose(String m, Throwable t){
        Log.i(TAG,"Phys/Animation "+m,t);
    }
    protected static void Debug(String m){
        Log.d(TAG,"Phys/Animation "+m);
    }
    protected static void Debug(String m, Throwable t){
        Log.d(TAG,"Phys/Animation "+m,t);
    }
    protected static void Info(String m){
        Log.i(TAG,"Phys/Animation "+m);
    }
    protected static void Info(String m, Throwable t){
        Log.i(TAG,"Phys/Animation "+m,t);
    }
    protected static void Warn(String m){
        Log.w(TAG,"Phys/Animation "+m);
    }
    protected static void Warn(String m, Throwable t){
        Log.w(TAG,"Phys/Animation "+m,t);
    }
    protected static void Error(String m){
        Log.e(TAG,"Phys/Animation "+m);
    }
    protected static void Error(String m, Throwable t){
        Log.e(TAG,"Phys/Animation "+m,t);
    }
    protected static void WTF(String m){
        Log.wtf(TAG,"Phys/Animation "+m);
    }
    protected static void WTF(String m, Throwable t){
        Log.wtf(TAG,"Phys/Animation "+m,t);
    }
}
