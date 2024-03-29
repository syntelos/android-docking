/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

/**
 * Principle simulation components and their integration.
 */
public final class DockingCraftStateVector
    extends Epsilon
{

    public final static DockingCraftStateVector Instance =
        new DockingCraftStateVector();

    public static Page PageToEnd(){
        final DockingCraftStateVector sv = DockingCraftStateVector.Instance;
        if (sv.dock()){

            return Page.gamedock;
        }
        else if (sv.stall()){

            return Page.gamestall;
        }
        else if (sv.free()){

            return Page.gamelost;
        }
        else {
            return Page.gamecrash;
        }
    }


    public volatile DockingGameLevel meta, level;
    /**
     * m
     */
    public volatile double range_x;
    /**
     * m/s
     */
    public volatile double velocity_x;
    /**
     * m/s/s
     */
    public volatile double acceleration_x;
    /**
     * millis */
    public volatile long time_start;
    public volatile long time_last;
    public volatile long time_clock;
    public volatile long time_source;
    public volatile long time_xp0;
    public volatile long time_xm0;
    public volatile long time_xp1;
    public volatile long time_xm1;

    /*
     * infrastructure
     */
    private volatile long copy;

    private volatile long cursor = -1;

    protected volatile String label, identifier;

    protected volatile Date created, completed;

    protected volatile float score;


    private DockingCraftStateVector(){
        super();
    }


    public synchronized boolean inGame(){

        return (DockingGameLevel.G == this.meta && 0 != this.level.number);
    }
    public synchronized boolean inModel(){

        return (DockingGameLevel.M == this.meta && DockingGameLevel.M == this.level);
    }
    public synchronized boolean inHardware(){

        return (DockingGameLevel.H == this.meta && DockingGameLevel.H == this.level);
    }
    public synchronized boolean inHistory(){

        return (DockingGameLevel.H == this.meta && 0 != this.level.number);
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
        return (0.010f < range_x && 0.011f > velocity_x && 0L >= time_source);
    }
    public boolean crash(){
        return (0.011f > range_x && 0.010f < velocity_x);
    }
    public boolean dock(){
        return (0.011f > range_x && 0.011f > velocity_x);
    }
    public boolean stall(){
        return (0.010f < range_x && 0.00f == velocity_x && 0L >= time_source);
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

        final long time = ViewClock.uptimeMillis();

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

                        acceleration_x = +(DockingGameLevel.Current.accel_thruster_0);

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

                    acceleration_x = -(DockingGameLevel.Current.accel_thruster_0);

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

                        acceleration_x += DockingGameLevel.Current.accel_thruster_1;

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

                    acceleration_x -= DockingGameLevel.Current.accel_thruster_1;

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
    protected synchronized void game(){

        this.meta = DockingGameLevel.G;

        this.cursor = -1L;

        this.level = DockingGameLevel.Current;

        this.created = new Date();

        this.completed = null;

        this.score = 0.0f;

        this.label = created.toString();

        this.identifier = BID.Identifier();

        this.range_x = this.level.range_x;

        this.velocity_x = this.level.velocity_x;

        this.acceleration_x = this.level.acceleration_x;

        this.time_last = 0;

        this.time_start = 0;

        this.time_clock = 0;

        this.time_source = this.level.time_source;

        this.time_xp0 = 0;

        this.time_xm0 = 0;

        this.time_xp1 = 0;

        this.time_xm1 = 0;

        this.copy = 0L;
    }
    protected synchronized void model(){

        this.meta = DockingGameLevel.M;

        this.cursor = -1L;

        this.level = DockingGameLevel.M;

        this.created = new Date();

        this.completed = new Date();

        this.score = 0.0f;

        this.label = "model";

        this.identifier = "model";

        this.range_x = this.level.range_x;

        this.velocity_x = this.level.velocity_x;

        this.acceleration_x = this.level.acceleration_x;

        this.time_last = 0;

        this.time_start = 0;

        this.time_clock = 0;

        this.time_source = this.level.time_source;

        this.time_xp0 = 0;

        this.time_xm0 = 0;

        this.time_xp1 = 0;

        this.time_xm1 = 0;

        this.copy = 0L;
    }
    protected synchronized void hardware(){

        this.meta = DockingGameLevel.H;

        this.cursor = -1L;

        this.level = DockingGameLevel.H;

        this.created = new Date();

        this.completed = new Date();

        this.score = 0.0f;

        this.label = "hardware";

        this.identifier = "hardware";

        this.range_x = this.level.range_x;

        this.velocity_x = this.level.velocity_x;

        this.acceleration_x = this.level.acceleration_x;

        this.time_last = 0;

        this.time_start = 0;

        this.time_clock = 0;

        this.time_source = this.level.time_source;

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

        this.meta = DockingGameLevel.H;

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
    }
}
