/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.SystemClock;

import java.util.Date;

/**
 * Principle simulation components and their integration.
 */
public final class DockingCraftStateVector
    extends Epsilon
{

    public final static DockingCraftStateVector Instance =
        new DockingCraftStateVector();


    public DockingGameLevel level;
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
    public long time_start;
    public long time_last;
    public long time_clock;
    public long time_source;
    public long time_xp0;
    public long time_xm0;
    public long time_xp1;
    public long time_xm1;

    /*
     * infrastructure
     */
    private long copy;

    private long cursor = -1;

    protected String label, identifier;

    protected Date created, completed;

    protected float score;


    private DockingCraftStateVector(){
        super();
    }


    public boolean complete(){
        return (null != completed);
    }
    public boolean incomplete(){
        return (null != created && null == completed);
    }
    public boolean running(){

        return (!crash()) && (!dock()) && (!stall());
    }
    public boolean done(){

        return (crash() || dock() || stall());
    }
    public boolean free(){
        return (0.001f < range_x);
    }
    public boolean crash(){
        return (0.011f > range_x && 0.010f < velocity_x);
    }
    public boolean dock(){
        return (0.011f > range_x && 0.011f > velocity_x);
    }
    public boolean stall(){
        return (0.001f > velocity_x && 0L >= time_source);
    }
    public synchronized void add(PhysicsScript prog){

        final PhysicsOperator op = prog.operator;

        switch(prog.operator){
        case TX0:
            switch(prog.dof){
            case XP:
                time_xp0 += prog.millis();
                break;
            case XM:
                time_xm0 += prog.millis();
                break;
            default:
                error("prog dof: "+prog.dof);
                break;
            }
            break;
        case TX1:
            switch(prog.dof){
            case XP:
                time_xp1 += prog.millis();
                break;
            case XM:
                time_xm1 += prog.millis();
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
    /**
     * The simulation employs real time (DT = clock - last) rather
     * than synthetic time (DT = TINC) in order to produce a
     * consistent or coherent visualization.
     */
    protected synchronized boolean update(long copy){
        /*
         * Integration procedure
         */
        final long last = this.time_last;

        final long time = SystemClock.uptimeMillis();

        if (0 != last){

            final long t_delta = (time-last);

            final boolean xp0 = (0L != this.time_xp0);

            final boolean xm0 = (0L != this.time_xm0);

            final boolean xp1 = (0L != this.time_xp1);

            final boolean xm1 = (0L != this.time_xm1);

            /*
             * Acceleration and Velocity (N 100.0)
             */
            if (xp0){

                if (xm0){

                    acceleration_x = 0.0f;
                }
                else {
                    final long ldt = Math.min(t_delta,Math.min(time_xp0,time_source));

                    if (0L != ldt){
                        final double dt = ((double)ldt/1000.0);

                        final double da0 = Z(dt * DockingGameLevel.Current.accel_thruster_0);

                        acceleration_x = +(da0);

                        velocity_x += da0;

                        time_xp0 = SubClampZP(time_xp0,ldt);

                        time_source = SubClampZP(time_source,(long)((double)ldt * DockingGameLevel.Current.cost_thruster_0));
                    }
                }
            }
            else if (xm0){

                final long ldt = Math.min(t_delta,Math.min(time_xm0,time_source));

                if (0L != ldt){
                    final double dt = ((double)ldt/1000.0);

                    final double da0 = Z(dt * DockingGameLevel.Current.accel_thruster_0);

                    acceleration_x = -(da0);

                    velocity_x -= da0;

                    time_xm0 = SubClampZP(time_xm0,ldt);

                    time_source = SubClampZP(time_source,(long)((double)ldt * DockingGameLevel.Current.cost_thruster_0));
                }
            }
            else {

                acceleration_x = 0.0f;
            }

            /*
             * Acceleration and Velocity (N 10.0)
             */
            if (xp1){

                if (!xm1){

                    final long ldt = Math.min(t_delta,Math.min(time_xp1,time_source));

                    if (0L != ldt){
                        final double dt = ((double)ldt/1000.0);

                        final double da1 = Z(dt * DockingGameLevel.Current.accel_thruster_1);

                        acceleration_x += da1;

                        velocity_x += da1;

                        time_xp1 = SubClampZP(time_xp1,ldt);

                        time_source = SubClampZP(time_source,(long)((double)ldt * DockingGameLevel.Current.cost_thruster_1));
                    }
                }
            }
            else if (xm1){

                final long ldt = Math.min(t_delta,Math.min(time_xm1,time_source));

                if (0L != ldt){
                    final double dt = ((double)ldt/1000.0);

                    final double da1 = Z(dt * DockingGameLevel.Current.accel_thruster_1);

                    acceleration_x -= da1;

                    velocity_x -= da1;

                    time_xm1 = SubClampZP(time_xm1,ldt);

                    time_source = SubClampZP(time_source,(long)((double)ldt * DockingGameLevel.Current.cost_thruster_1));
                }
            }

            /*
             * Range
             */
            if (0.0f != velocity_x){

                final double dt = ((double)t_delta)/1000.0;

                final double dr = Z(dt*velocity_x);

                range_x -= dr;
            }

            /*
             * Output
             */
            if (time > (this.copy+copy)){

                this.copy = time;

                DockingPageGameAbstract.Play();
            }
            else {
                DockingPageGameAbstract.Range((float)range_x);
            }
        }
        else {
            this.time_start = time;
        }
        this.time_last = time;
        this.time_clock = (time-this.time_start);

        return running();
    }
    /**
     * Alternative to {@link #create} reads a pre-positioned cursor.
     */
    protected synchronized void create(){

        this.cursor = -1;

        this.level = DockingGameLevel.Current;

        this.created = new Date();

        this.completed = null;

        this.score = 0.0f;

        this.label = created.toString();

        this.identifier = BID.Identifier();

        this.range_x = DockingGameLevel.Current.range_x;

        this.velocity_x = DockingGameLevel.Current.velocity_x;

        this.acceleration_x = DockingGameLevel.Current.acceleration_x;

        this.time_last = 0;

        this.time_start = 0;

        this.time_clock = 0;

        this.time_source = DockingGameLevel.Current.time_source;

        this.time_xp0 = 0;

        this.time_xm0 = 0;

        this.time_xp1 = 0;

        this.time_xm1 = 0;

        this.copy = 0L;
    }
    /**
     * Alternative to {@link #create} reads a pre-positioned cursor.
     */
    protected synchronized DockingCraftStateVector read(Cursor cursor){

        this.cursor = cursor.getLong(cursor.getColumnIndexOrThrow(DockingDatabaseHistory.State._ID));

        this.identifier = cursor.getString(cursor.getColumnIndexOrThrow(DockingDatabaseHistory.State.IDENTIFIER));
        this.label = cursor.getString(cursor.getColumnIndexOrThrow(DockingDatabaseHistory.State.LABEL));

        this.level = DockingGameLevel.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DockingDatabaseHistory.State.LEVEL)));

        this.velocity_x = cursor.getFloat(cursor.getColumnIndexOrThrow(DockingDatabaseHistory.State.VX));
        this.acceleration_x = cursor.getFloat(cursor.getColumnIndexOrThrow(DockingDatabaseHistory.State.AX));
        this.range_x = cursor.getFloat(cursor.getColumnIndexOrThrow(DockingDatabaseHistory.State.RX));

        this.time_last = 0L;

        this.time_source = cursor.getLong(cursor.getColumnIndexOrThrow(DockingDatabaseHistory.State.T_SOURCE));
        this.time_clock = cursor.getLong(cursor.getColumnIndexOrThrow(DockingDatabaseHistory.State.T_CLOCK));
        this.time_xp0 = cursor.getLong(cursor.getColumnIndexOrThrow(DockingDatabaseHistory.State.T_XP0));
        this.time_xm0 = cursor.getLong(cursor.getColumnIndexOrThrow(DockingDatabaseHistory.State.T_XM0));
        this.time_xp1 = cursor.getLong(cursor.getColumnIndexOrThrow(DockingDatabaseHistory.State.T_XP1));
        this.time_xm1 = cursor.getLong(cursor.getColumnIndexOrThrow(DockingDatabaseHistory.State.T_XM1));

        final long created = cursor.getLong(cursor.getColumnIndexOrThrow(DockingDatabaseHistory.State.CREATED));
        if (0 < created){
            this.created = new Date(created);
        }
        else {
            this.created = null;
        }

        final long completed = cursor.getLong(cursor.getColumnIndexOrThrow(DockingDatabaseHistory.State.COMPLETED));
        if (0 < completed){
            this.completed = new Date(completed);
        }
        else {
            this.completed = null;
        }

        this.score = cursor.getFloat(cursor.getColumnIndexOrThrow(DockingDatabaseHistory.State.SCORE));

        return this;
    }
    /**
     * Called once (and only once) per game (record) by {@link DockingDatabase#GameOver}
     */
    protected synchronized ContentValues write(){

        ContentValues values = new ContentValues();

        values.put(DockingDatabaseHistory.State.IDENTIFIER,this.identifier);

        values.put(DockingDatabaseHistory.State.LABEL,this.label);

        values.put(DockingDatabaseHistory.State.LEVEL,this.level.name());

        /*
         * Clamp Velocity
         */
        final float velocity_x = ClampRP(this.velocity_x,1000.0);

        values.put(DockingDatabaseHistory.State.VX,velocity_x);

        /*
         * Clamp Acceleration
         */
        final float acceleration_x = ClampRP(this.acceleration_x,1000.0);

        values.put(DockingDatabaseHistory.State.AX,acceleration_x);

        /*
         * Clamp Range
         */
        final float range_x = ClampRP(this.range_x,1000.0);

        values.put(DockingDatabaseHistory.State.RX,range_x);

        /*
         * Game time
         */
        this.time_last = 0L;

        this.time_start = 0L;

        values.put(DockingDatabaseHistory.State.T_SOURCE,this.time_source);

        values.put(DockingDatabaseHistory.State.T_CLOCK,this.time_clock);

        values.put(DockingDatabaseHistory.State.T_XP0,this.time_xp0);

        values.put(DockingDatabaseHistory.State.T_XM0,this.time_xm0);

        values.put(DockingDatabaseHistory.State.T_XP1,this.time_xp1);

        values.put(DockingDatabaseHistory.State.T_XM1,this.time_xm1);

        /*
         * Date-time
         */
        values.put(DockingDatabaseHistory.State.CREATED,this.created.getTime());

        this.completed = new Date();

        values.put(DockingDatabaseHistory.State.COMPLETED,this.completed.getTime());

        /*
         * Score
         */
        if (dock()){

            this.score = 4.0f;
        }
        else if (stall()){

            this.score = 1.0f;
        }
        else if (3.0f < this.velocity_x){

            this.score = 1.0f;
        }
        else if (1.0f < this.velocity_x){

            this.score = 2.0f;
        }
        else {

            this.score = 3.0f;
        }

        values.put(DockingDatabaseHistory.State.SCORE,this.score);

        return values;
    }
    protected synchronized long cursor(){

        return this.cursor;
    }
    protected synchronized void cursor(long c){

        this.cursor = c;

        DockingGameLevel.Review(this.score);
    }
}
