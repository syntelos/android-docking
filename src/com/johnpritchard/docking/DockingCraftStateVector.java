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

    private final static String SV_R_X = "sv.r_x";
    private final static String SV_V_X = "sv.v_x";
    private final static String SV_A_X = "sv.a_x";
    private final static String SV_T_SOURCE = "sv.t";
    private final static String SV_T_SINK_XP = "sv.t_xp";
    private final static String SV_T_SINK_XM = "sv.t_xm";

    public final static long SECONDS = 1000L;

    public final static float KILOMETERS = 1000.0f;


    private final static long SV_T_SOURCE_INIT(){

        return 999L*SECONDS;
    }
    private final static float SV_R_X_INIT(){

        return 10.0f*KILOMETERS;
    }
    private final static float SV_V_X_INIT(){
        return 10.0f;
    }
    private final static float SV_A_X_INIT(){
        return 0.0f;
    }

    /**
     * m
     */
    public double range_x;
    /**
     * m/s
     */
    public double velocity_x;
    /**
     * m/s/s
     */
    public double acceleration_x;
    /**
     * millis
     */
    public long time_last;

    public PhysicsTimeSource time_source;
    public PhysicsTimeSink time_xp;
    public PhysicsTimeSink time_xm;

    private long copy;


    protected DockingCraftStateVector(){
        super();
    }


    public void add(PhysicsScript prog){

        switch(prog.operator){
        case TX:
            switch(prog.dof){
            case XP:
                time_xp.add(prog);
                break;
            case XM:
                time_xm.add(prog);
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

        time_xp = new PhysicsTimeSink(time_source,state.getLong(SV_T_SINK_XP,0L));

        time_xm = new PhysicsTimeSink(time_source,state.getLong(SV_T_SINK_XM,0L));

    }
    protected synchronized void close(SharedPreferences.Editor state){
        float r_x = (float)range_x;
        float v_x = (float)velocity_x;
        float a_x = (float)acceleration_x;

        state.putFloat(SV_R_X,r_x);
        state.putFloat(SV_V_X,v_x);
        state.putFloat(SV_A_X,a_x);

        state.putLong(SV_T_SOURCE,time_source.value);
        state.putLong(SV_T_SINK_XP,time_xp.value);
        state.putLong(SV_T_SINK_XM,time_xm.value);
    }
    protected synchronized void update(long copy){

        long last = this.time_last;

        long time = SystemClock.uptimeMillis();

        if (0 != last){

            double dt = ((double)(time-last))/1000.0;

            if (this.time_xp.active()){

                double da = dt*DockingCraftModel.acceleration_under_thrust;

                acceleration_x += da;
            }
            if (this.time_xm.active()){

                double da = dt*DockingCraftModel.acceleration_under_thrust;

                acceleration_x -= da;
            }

            double dv = dt*acceleration_x;

            if (0.0 != Z(dv)){

                velocity_x += dv;

                if (0.0 == Z(velocity_x)){

                    velocity_x = 0.0;
                }
                else {

                    double dr = dt*velocity_x;

                    range_x -= dr;

                    if (0.0 == Z(range_x)){

                        range_x = 0.0;
                    }
                }
            }
        }
        this.time_last = time;
        this.time_xp.update(time);
        this.time_xm.update(time);

        if (time > (this.copy+copy)){

            this.copy = time;

            DockingPageGameAbstract.Format(this);
        }
        else {
            DockingPageGameAbstract.Range((float)this.range_x);
        }
    }
}
