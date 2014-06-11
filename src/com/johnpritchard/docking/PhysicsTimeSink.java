/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public final class PhysicsTimeSink {

    protected final PhysicsTimeSource source;
    /**
     * ms
     */
    protected long value;

    protected long last;


    public PhysicsTimeSink(PhysicsTimeSource source){
        super();
        if (null != source){

            this.source = source;
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    public PhysicsTimeSink(PhysicsTimeSource source, long value){
        this(source);
        if (0 < value){
            this.value = value;
        }
    }


    public void add(PhysicsScript prog){
        final PhysicsOperator op = prog.operator;

        value = op.milliseconds(op.seconds(value) + prog.seconds());
    }
    public int seconds(){
        return (int)(value/1000);
    }
    public float secondsf(){
        return ((float)value/1000.0f);
    }
    public boolean active(){
        return (0 != value);
    }
    public void update(long time){

        if (0L != last && 0 < value){

            final long dt = Math.min((time-last),Math.min(value,source.value));

            if (0L != dt){

                this.value -= dt;

                this.source.value -= dt;
            }
            else {
                this.value = 0L;
            }
        }
        last = time;
    }
}
