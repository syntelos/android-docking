/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 *
 */
public enum PhysicsOperator {
    /**
     * Translation in axis
     *
     * Thruster operational values have an integer number of seconds.
     */
    TX0("T10","%03d"),
    TX1("T01","%03d");


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


    @Override
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
    public int limit(long t_source){

        switch(this){
        case TX1:
            return (int)(t_source/100L);
        default:
            return (int)(t_source/1000L);
        }
    }
}
