/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.SharedPreferences;
import android.os.SystemClock;

import static com.johnpritchard.docking.DockingCraftModel.*;

/**
 * Principle simulation components and their integration.
 */
public final class DockingCraftStateVector
    extends Epsilon
{

    private final static String SV_R_X = "sv.r_x";
    private final static String SV_V_X = "sv.v_x";
    private final static String SV_A_X = "sv.a_x";
    private final static String SV_T_SOURCE = "sv.t";
    private final static String SV_T_SINK_XP0 = "sv.t_xp0";
    private final static String SV_T_SINK_XM0 = "sv.t_xm0";
    private final static String SV_T_SINK_XP1 = "sv.t_xp1";
    private final static String SV_T_SINK_XM1 = "sv.t_xm1";

    public final static long I_SECONDS = 1000L;

    public final static float I_KILOMETERS = 1000.0f;


    private final static float SV_R_X_INIT(){

        return 1.0f*I_KILOMETERS;
    }
    private final static float SV_V_X_INIT(){
        return 10.0f;
    }
    private final static float SV_A_X_INIT(){
        return 0.0f;
    }
    private final static long SV_T_SOURCE_INIT(){

        return 99L*I_SECONDS;
    }
    private final static long SV_T_SINK_0_INIT(){
        return 0L;
    }
    private final static long SV_T_SINK_1_INIT(){
        return 0L;
    }

    public final static DockingCraftStateVector Instance =
        new DockingCraftStateVector();

    /**
     * m
     */
    public float range_x;
    /**
     * m/s
     */
    public float velocity_x;
    /**
     * m/s/s
     */
    public float acceleration_x;
    /**
     * millis
     */
    public long time_last;

    public PhysicsTimeSource time_source;
    public PhysicsTimeSink time_xp0;
    public PhysicsTimeSink time_xm0;
    public PhysicsTimeSink time_xp1;
    public PhysicsTimeSink time_xm1;

    private long copy;


    private DockingCraftStateVector(){
        super();
    }


    public boolean running(){
        return (0.001f < range_x);
    }
    public boolean done(){
        return (0.001f >= range_x);
    }
    public boolean crash(){
        return (0.001f >= range_x && 0.010f < velocity_x);
    }
    public boolean dock(){
        return (0.001f >= range_x && 0.010f >= velocity_x);
    }
    public void add(PhysicsScript prog){

        switch(prog.operator){
        case TX0:
            switch(prog.dof){
            case XP:
                time_xp0.add(prog);
                break;
            case XM:
                time_xm0.add(prog);
                break;
            default:
                error("prog dof: "+prog.dof);
                break;
            }
            break;
        case TX1:
            switch(prog.dof){
            case XP:
                time_xp1.add(prog);
                break;
            case XM:
                time_xm1.add(prog);
                break;
            default:
                error("prog dof: "+prog.dof);
                break;
            }
            break;
        default:
            error("prog op: "+prog.operator);
            break;
        }
    }
    protected synchronized void open(SharedPreferences state){

        range_x = state.getFloat(SV_R_X,SV_R_X_INIT());

        velocity_x = state.getFloat(SV_V_X,SV_V_X_INIT());

        acceleration_x = state.getFloat(SV_A_X,SV_A_X_INIT());

        time_source = new PhysicsTimeSource(state.getLong(SV_T_SOURCE,SV_T_SOURCE_INIT()));

        time_xp0 = new PhysicsTimeSink(time_source,state.getLong(SV_T_SINK_XP0,SV_T_SINK_0_INIT()));

        time_xm0 = new PhysicsTimeSink(time_source,state.getLong(SV_T_SINK_XM0,SV_T_SINK_0_INIT()));

        time_xp1 = new PhysicsTimeSink(time_source,state.getLong(SV_T_SINK_XP1,SV_T_SINK_1_INIT()));

        time_xm1 = new PhysicsTimeSink(time_source,state.getLong(SV_T_SINK_XM1,SV_T_SINK_1_INIT()));

        copy = 0L;
    }
    protected synchronized void close(SharedPreferences.Editor state){

        if (done()){
            state.putFloat(SV_R_X,SV_R_X_INIT());
            state.putFloat(SV_V_X,SV_V_X_INIT());
            state.putFloat(SV_A_X,SV_A_X_INIT());

            state.putLong(SV_T_SOURCE,SV_T_SOURCE_INIT());
            state.putLong(SV_T_SINK_XP0,SV_T_SINK_0_INIT());
            state.putLong(SV_T_SINK_XM0,SV_T_SINK_0_INIT());
            state.putLong(SV_T_SINK_XP1,SV_T_SINK_1_INIT());
            state.putLong(SV_T_SINK_XM1,SV_T_SINK_1_INIT());
        }
        else {
            state.putFloat(SV_R_X,range_x);
            state.putFloat(SV_V_X,velocity_x);
            state.putFloat(SV_A_X,acceleration_x);

            state.putLong(SV_T_SOURCE,time_source.value);
            state.putLong(SV_T_SINK_XP0,time_xp0.value);
            state.putLong(SV_T_SINK_XM0,time_xm0.value);
            state.putLong(SV_T_SINK_XP1,time_xp1.value);
            state.putLong(SV_T_SINK_XM1,time_xm1.value);
        }
    }
    /**
     * The simulation employs real time (DT = clock - last) rather
     * than synthetic time (DT = TINC) in order to produce a
     * consistent or coherent visualization.
     */
    protected synchronized boolean update(long copy){
        /*
         * An integration that's fun to play with.
         */
        final long last = this.time_last;

        final long time = SystemClock.uptimeMillis();

        if (0 != last){

            final double dt = ((double)(time-last))/1000.0;

            final double da0 = Z(dt*accel_thruster_0);

            final double da1 = Z(dt*accel_thruster_1);

            final boolean xp0 = this.time_xp0.active();

            final boolean xm0 = this.time_xm0.active();

            final boolean xp1 = this.time_xp1.active();

            final boolean xm1 = this.time_xm1.active();

            if (xp0){

                if (xm0){

                    acceleration_x = 0.0f;
                }
                else {

                    acceleration_x = (float)+(da0);

                    velocity_x = Clamp(((double)velocity_x + da0),1000.0);
                }
            }
            else if (xm0){

                acceleration_x = (float)-(da0);

                velocity_x = Clamp(((double)velocity_x - da0),1000.0);
            }
            else {

                acceleration_x = 0.0f;
            }

            if (xp1){

                if (!xm1){

                    acceleration_x = (float)+(da1);

                    velocity_x = Clamp(((double)velocity_x + da1),1000.0);
                }
            }
            else if (xm1){

                acceleration_x = (float)-(da1);

                velocity_x = Clamp(((double)velocity_x - da1),1000.0);
            }



            if (0.0f != velocity_x){

                final double dr = Z(dt*velocity_x);

                range_x = Clamp(((double)range_x - dr),1000.0);
            }

            if (time > (this.copy+copy)){

                this.copy = time;

                DockingPageGameAbstract.Format();
            }
            else {
                DockingPageGameAbstract.Range(this.range_x);
            }
        }
        this.time_last = time;
        this.time_xp0.update(time);
        this.time_xm0.update(time);
        this.time_xp1.update(time);
        this.time_xm1.update(time);

        return running();
    }
}
