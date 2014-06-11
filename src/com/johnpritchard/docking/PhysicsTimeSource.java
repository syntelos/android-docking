/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.os.SystemClock;

/**
 * 
 */
public final class PhysicsTimeSource {

    /**
     * ms
     */
    protected long value;


    public PhysicsTimeSource(long value){
        super();
        if (0 < value){
            this.value = value;
        }
    }


    public int seconds(){
        return (int)(value/1000);
    }
    public float secondsf(){
        return ((float)value/1000.0f);
    }
}
