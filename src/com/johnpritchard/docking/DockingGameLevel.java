/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.SharedPreferences;
import android.util.Log;

/**
 * 
 */
public enum DockingGameLevel {
    /*
     * State vectors for application (SV meta)
     */
    G (  0,    0.0,   0.0,  0.0,    0.0,  0.0, 0.0,   0),
    H (  0,    0.0,   0.0,  0.0,    1.0,  0.0, 0.0,   0),
    M (  0,    0.0,   0.0,  0.0,   10.0,  0.0, 0.0,   0),
    /*
     * State vectors for game play
     * 
     *      Mass      Force      Range  Velocity -  Budget
     *  LN   kg      N      N      m      m/s   m/s/s  s
     */
    L1(  1, 1000.0, 100.0, 10.0,   250.0,  5.0, 0.0,  50),
    L2(  2, 1000.0, 100.0, 10.0,  1000.0, 10.0, 0.0, 100),
    L3(  3, 1000.0, 100.0, 10.0,  4000.0, 20.0, 0.0, 200),
    L4(  4, 1000.0, 100.0, 10.0, 16000.0, 40.0, 0.0, 400),
    L5(  5, 1000.0, 100.0, 10.0, 64000.0, 80.0, 0.0, 800);


    public volatile static DockingGameLevel Current = L1;

    public static void OnResume(SharedPreferences preferences){

        Current = DockingGameLevel.valueOf(preferences.getString("game.level","L1"));
    }
    public static void OnPause(SharedPreferences.Editor state){

        state.putString("game.level",Current.name());
    }
    public static void Review(double score){

        if (4.0 == score){

            Current = Current.next();
        }
    }


    /**
     * Game level number is not zero.  Non-game number is zero.
     */
    public final int number;
    /**
     * kg
     */
    public final double mass_static;
    /**
     * N = kg m/s/s
     */
    public final double force_thruster_0;

    public final double force_thruster_1;
    /**
     * m/s/s
     */
    public final double accel_thruster_0;

    public final double accel_thruster_1;
    /**
     * mul by cost =
     * div by 1.0
     */
    public final double cost_thruster_0;
    /**
     * mul by cost =
     * div by 10.0
     */
    public final double cost_thruster_1;

    public final float range_x;

    public final float velocity_x;

    public final float acceleration_x;

    public final long time_source;


    private DockingGameLevel(int number, double m, double f0, double f1, double r, double v, double a, long t){
        this.number = number;
        this.mass_static = m;
        this.force_thruster_0 = f0;
        this.force_thruster_1 = f1;
        this.accel_thruster_0 = (this.force_thruster_0 / this.mass_static);
        this.accel_thruster_1 = (this.force_thruster_1 / this.mass_static);
        this.cost_thruster_0 = (this.accel_thruster_0 * 10.0);
        this.cost_thruster_1 = (this.accel_thruster_1 * 10.0);
        this.range_x = (float)r;
        this.velocity_x = (float)v;
        this.acceleration_x = (float)a;
        this.time_source = t*1000;
    }


    public DockingGameLevel next(){
        switch(this){
        case L1:
            return L2;
        case L2:
            return L3;
        case L3:
            return L4;
        case L4:
            return L5;
        default:
            return L1;
        }
    }

    protected final static String TAG = ObjectLog.TAG;

    protected static void Verbose(String m){
        Log.i(TAG,"DockingGameLevel "+m);
    }
    protected static void Verbose(String m, Throwable t){
        Log.i(TAG,"DockingGameLevel "+m,t);
    }
    protected static void Debug(String m){
        Log.d(TAG,"DockingGameLevel "+m);
    }
    protected static void Debug(String m, Throwable t){
        Log.d(TAG,"DockingGameLevel "+m,t);
    }
    protected static void Info(String m){
        Log.i(TAG,"DockingGameLevel "+m);
    }
    protected static void Info(String m, Throwable t){
        Log.i(TAG,"DockingGameLevel "+m,t);
    }
    protected static void Warn(String m){
        Log.w(TAG,"DockingGameLevel "+m);
    }
    protected static void Warn(String m, Throwable t){
        Log.w(TAG,"DockingGameLevel "+m,t);
    }
    protected static void Error(String m){
        Log.e(TAG,"DockingGameLevel "+m);
    }
    protected static void Error(String m, Throwable t){
        Log.e(TAG,"DockingGameLevel "+m,t);
    }
    protected static void WTF(String m){
        Log.wtf(TAG,"DockingGameLevel "+m);
    }
    protected static void WTF(String m, Throwable t){
        Log.wtf(TAG,"DockingGameLevel "+m,t);
    }
}
