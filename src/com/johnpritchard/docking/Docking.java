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

    private static DockingActivityMain Main;
    private static DockingActivityView View;

    /**
     * Called from main to start view.
     */
    public final static void StartView(){

        Intent intent = new Intent(Main, DockingActivityView.class);

        Main.startActivity(intent);
    }
    /**
     * Called from view to start main.
     */
    public final static void StartMain(){

        View.finish();
    }
    /**
     * Called from main
     */
    protected final static void MainActivate(DockingActivityMain instance){

        Main = instance;
    }
    /**
     * Called from view
     */
    protected final static void ViewActivate(DockingActivityView instance){

        View = instance;
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
