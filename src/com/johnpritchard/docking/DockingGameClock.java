/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.os.SystemClock;

/**
 * Time dilation for application programming.
 */
public final class DockingGameClock {
    /**
     * 
     */
    public enum Mode {
        Normal    (  1, 0, 1.0000f, 1000L),
        Two       (  2, 1, 0.5000f,  500L),
        Four      (  4, 2, 0.2500f,  250L),
        Eight     (  8, 3, 0.1250f,  125L),
        Sixteen   ( 16, 4, 0.0625f,   62L);


        public final int number;

        public final int shift;

        public final float fraction;

        public final long period;


        private Mode( int number, int shift, float fraction, long period){
            this.number = number;
            this.shift = shift;
            this.fraction = fraction;
            this.period = period;
        }
    }

    public final static Mode mode = Mode.Sixteen;

    public static long uptimeMillis(){

        return (SystemClock.uptimeMillis() >> mode.shift);
    }
    public static void sleep(long millis)
        throws InterruptedException
    {

        Thread.sleep(millis << mode.shift);
    }
    public static long millis(long millis){

        return (millis << mode.shift);
    }
    public static long millis(long millis, boolean condition){

        if (condition){

            return (millis << mode.shift);
        }
        else {

            return millis;
        }
    }


    private DockingGameClock(){
        super();
    }

}
