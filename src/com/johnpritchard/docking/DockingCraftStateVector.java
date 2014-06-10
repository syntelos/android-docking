/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.SharedPreferences;
import android.os.SystemClock;

/**
 * Principle simulation components and their integration.
 */
public final class DockingCraftStateVector
    extends Epsilon
    implements Cloneable
{

    private final static String SV_V_X = "sv.v_x";
    private final static String SV_A_X = "sv.a_x";

    private final static float SV_V_X_INIT(){
        return 1.0f;
    }
    private final static float SV_A_X_INIT(){
        return 0.0f;
    }

    /**
     * millis
     */
    public long time;
    /**
     * m/s
     */
    public double velocity_x;
    /**
     * m/s/s
     */
    public double acceleration_x;


    protected DockingCraftStateVector(){
        super();
    }


    protected synchronized void open(SharedPreferences state){

        velocity_x = state.getFloat(SV_V_X,SV_V_X_INIT());

        acceleration_x = state.getFloat(SV_A_X,SV_A_X_INIT());

        time = SystemClock.uptimeMillis();
    }
    protected synchronized void close(SharedPreferences.Editor state){
        float v_x = (float)velocity_x;
        float a_x = (float)acceleration_x;

        state.putFloat(SV_V_X,v_x);
        state.putFloat(SV_A_X,a_x);
    }
    protected synchronized void update(double pt){

        double pa = pt*acceleration_x;

        if (0.0 != Z(pa)){

            velocity_x += pa;

            if (0.0 == Z(velocity_x)){

                velocity_x = 0.0;
            }
        }
        time = SystemClock.uptimeMillis();
    }
    public synchronized DockingCraftStateVector clone(){
        try {
            return (DockingCraftStateVector)super.clone();
        }
        catch (CloneNotSupportedException exc){
            throw new InternalError();
        }
    }
    public synchronized boolean copy(DockingCraftStateVector source, long dt){

        if (SystemClock.uptimeMillis() > (this.time+dt)){

            this.time = source.time;
            this.velocity_x = source.velocity_x;
            this.acceleration_x = source.acceleration_x;

            return true;
        }
        else {
            return false;
        }
    }

}
