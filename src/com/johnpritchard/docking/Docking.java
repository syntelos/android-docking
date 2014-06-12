/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

/**
 * 
 */
public final class Docking
    extends android.app.Application
{
    private final static String TAG = ObjectLog.TAG;

    private static DockingActivity2D Activity2D;
    private static DockingActivity3D Activity3D;

    /**
     * Called from activity2D to start activity3D.
     */
    public final static void StartActivity3D(){

        Intent intent = new Intent(Activity2D, DockingActivity3D.class);

        Activity2D.startActivity(intent);
    }
    /**
     * Called from activity3D to start activity2D.
     */
    public final static void StartActivity2D(){

        Activity3D.finish();
    }
    /**
     * Called from activity2D
     */
    protected final static void Activate2D(DockingActivity2D instance){

        Activity2D = instance;
    }
    /**
     * Called from activity3D
     */
    protected final static void Activate3D(DockingActivity3D instance){

        Activity3D = instance;
    }
    protected final static void Post2D(Runnable action){

        Activity2D.runOnUiThread(action);
    }
    protected final static void Post3D(Runnable action){

        Activity3D.runOnUiThread(action);
    }


    public Docking(){
        super();
    }


    protected void verbose(String m){
        Log.i(TAG,"Docking "+m);
    }
    protected void verbose(String m, Throwable t){
        Log.i(TAG,"Docking "+m,t);
    }
    protected void debug(String m){
        Log.d(TAG,"Docking "+m);
    }
    protected void debug(String m, Throwable t){
        Log.d(TAG,"Docking "+m,t);
    }
    protected void info(String m){
        Log.i(TAG,"Docking "+m);
    }
    protected void info(String m, Throwable t){
        Log.i(TAG,"Docking "+m,t);
    }
    protected void warn(String m){
        Log.w(TAG,"Docking "+m);
    }
    protected void warn(String m, Throwable t){
        Log.w(TAG,"Docking "+m,t);
    }
    protected void error(String m){
        Log.e(TAG,"Docking "+m);
    }
    protected void error(String m, Throwable t){
        Log.e(TAG,"Docking "+m,t);
    }
    protected void wtf(String m){
        Log.wtf(TAG,"Docking "+m);
    }
    protected void wtf(String m, Throwable t){
        Log.wtf(TAG,"Docking "+m,t);
    }
    protected static void Verbose(String m){
        Log.i(TAG,"Docking "+m);
    }
    protected static void Verbose(String m, Throwable t){
        Log.i(TAG,"Docking "+m,t);
    }
    protected static void Debug(String m){
        Log.d(TAG,"Docking "+m);
    }
    protected static void Debug(String m, Throwable t){
        Log.d(TAG,"Docking "+m,t);
    }
    protected static void Info(String m){
        Log.i(TAG,"Docking "+m);
    }
    protected static void Info(String m, Throwable t){
        Log.i(TAG,"Docking "+m,t);
    }
    protected static void Warn(String m){
        Log.w(TAG,"Docking "+m);
    }
    protected static void Warn(String m, Throwable t){
        Log.w(TAG,"Docking "+m,t);
    }
    protected static void Error(String m){
        Log.e(TAG,"Docking "+m);
    }
    protected static void Error(String m, Throwable t){
        Log.e(TAG,"Docking "+m,t);
    }
    protected static void WTF(String m){
        Log.wtf(TAG,"Docking "+m);
    }
    protected static void WTF(String m, Throwable t){
        Log.wtf(TAG,"Docking "+m,t);
    }
}
