/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.os.SystemClock;

/**
 * Time dilation for application programming.
 */
public final class DockingGameClock {

    public enum Mode {
        Normal,
        Half,
        Quarter,
        Eighth;
    }

    public final static Mode mode = Mode.Half;

    public static long uptimeMillis(){

        long time = SystemClock.uptimeMillis();

        switch(mode){
        case Half:
            return (time >> 1);
        case Quarter:
            return (time >> 2);
        case Eighth:
            return (time >> 3);

        default:
            return time;
        }
    }


    private DockingGameClock(){
        super();
    }

}
