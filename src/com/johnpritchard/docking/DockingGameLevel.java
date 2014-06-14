/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.SharedPreferences;
/**
 * 
 */
public enum DockingGameLevel {
    L1( 1000.0, 100.0, 10.0,  500.0,  5.0, 0.0,  50),
    L2( 1000.0, 100.0, 10.0, 1000.0, 10.0, 0.0, 100);


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


    private DockingGameLevel(double m, double f0, double f1, double r, double v, double a, long t){

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


    public DockingGameLevel prev(){

        return L1;
    }
    public DockingGameLevel next(){

        return L2;
    }
}
