/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * Thruster operational values have an integer number of seconds.
 */
public enum PhysicsOperator {
    /**
     * Translation in axis
     */
    TX("T","%03d"),
    TY("T","%03d"),
    TZ("T","%03d"),
    /**
     * Rotation in axis
     */
    RX("R","%03d"),
    RY("R","%03d"),
    RZ("R","%03d");


    public final String label;

    public final int llen;

    public final String format;

    public final int flen;


    private PhysicsOperator(String label, String format){
        this.label = label;
        this.llen = label.length();
        this.format = format;
        this.flen = format.length();
    }


    public String toString(){
        return label;
    }
    public float seconds(long ms){

        return ((float)ms/1000f);
    }
    public long milliseconds(float seconds){
        if (1000f > seconds){

            if (0f < seconds){

                return (long)(seconds*1000f); // (floor)
            }
            else {

                return 0;
            }
        }
        else {
            return 999;
        }
    }
}
