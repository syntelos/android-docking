/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingMovie
    extends java.lang.Thread
{
    public final static boolean Available = false;

    private final static String TAG = ObjectLog.TAG;

    private final static Object StaticMonitor = DockingMovie.class;


    protected volatile static DockingMovie Instance;

    public static void Start(){
        if (Available){
            synchronized(StaticMonitor){
                if (null == Instance){

                    Instance = new DockingMovie();

                    Instance._start();
                }
            }
        }
    }
    public static void Stop(){
        if (Available){
            synchronized(StaticMonitor){
                DockingMovie instance = Instance;
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
    }


    private final static long TINC = 2000L;


    private final Object monitor = new Object();

    private volatile boolean running = true;


    private DockingMovie(){
        super("ScreenShot/Animation");
    }


    private void _start(){

        this.running = true;

        super.start();
    }
    private void _stop(){

        this.running = false;

        synchronized(this.monitor){

            this.monitor.notify();
        }
    }
    public void run(){
        try {
            //info("running");

            while (running){

                Docking.ScreenShot3D();

                synchronized(this.monitor){
                    this.monitor.wait(TINC);
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
}
