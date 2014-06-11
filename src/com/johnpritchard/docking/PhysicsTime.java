/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.os.SystemClock;

/**
 * 
 */
public abstract class PhysicsTime {

    /**
     * ms
     */
    protected volatile long value;


    public PhysicsTime(long value){
        super();
        if (0 < value){
            this.value = value;
        }
    }
    public PhysicsTime(){
        super();
    }


    public final boolean active(){
        return (0L != value);
    }
    public final int seconds(){
        return (int)(value/1000L);
    }
    public final float secondsf(){
        return ((float)value/1000.0f);
    }
}
