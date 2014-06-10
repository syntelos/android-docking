/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodInfo;
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
    private static DockingKeyboard Keyboard;

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
     * Called from view to raise keyboard
     */
    public final static void RaiseKeyboard(){

        if (View.view.requestFocus()){

            Info("RaiseKeyboard succeeded to focus view");
            try {
                InputMethodManager imm = (InputMethodManager)View.getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.showSoftInput(View.view,InputMethodManager.SHOW_IMPLICIT);

                Info("RaiseKeyboard completed");

                {
                    List<InputMethodInfo> list = imm.getEnabledInputMethodList();
                    for (InputMethodInfo info: list){
                        Info("input-method enabled "+info.getId()+" "+info.getPackageName()+" "+info.getServiceName());
                    }
                }
            }
            catch (Throwable t){
                Error("RaiseKeyboard",t);
            }
        }
        else {
            Info("RaiseKeyboard failed to focus view");
        }
    }
    public final static void LowerKeyboard(){

        InputMethodManager imm = (InputMethodManager)View.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(View.view.getWindowToken(),0); 
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
    /**
     * Called from keyboard
     */
    protected final static void KeyboardActivate(DockingKeyboard instance){

        Keyboard = instance;
    }
    /**
     * Called from DockingPhysics
     */
    protected final synchronized static void PhysicsUpdate(){

        if (null != View){

            View.physicsUpdate();
        }
    }


    public Docking(){
        super();
    }


    protected void verbose(String m){
        Log.i(TAG,getClass().getName()+' '+m);
    }
    protected void verbose(String m, Throwable t){
        Log.i(TAG,getClass().getName()+' '+m,t);
    }
    protected void debug(String m){
        Log.d(TAG,getClass().getName()+' '+m);
    }
    protected void debug(String m, Throwable t){
        Log.d(TAG,getClass().getName()+' '+m,t);
    }
    protected void info(String m){
        Log.i(TAG,getClass().getName()+' '+m);
    }
    protected void info(String m, Throwable t){
        Log.i(TAG,getClass().getName()+' '+m,t);
    }
    protected void warn(String m){
        Log.w(TAG,getClass().getName()+' '+m);
    }
    protected void warn(String m, Throwable t){
        Log.w(TAG,getClass().getName()+' '+m,t);
    }
    protected void error(String m){
        Log.e(TAG,getClass().getName()+' '+m);
    }
    protected void error(String m, Throwable t){
        Log.e(TAG,getClass().getName()+' '+m,t);
    }
    protected void wtf(String m){
        Log.wtf(TAG,getClass().getName()+' '+m);
    }
    protected void wtf(String m, Throwable t){
        Log.wtf(TAG,getClass().getName()+' '+m,t);
    }
    protected static void Verbose(String m){
        Log.i(TAG,"com.johnpritchard.docking.Docking "+m);
    }
    protected static void Verbose(String m, Throwable t){
        Log.i(TAG,"com.johnpritchard.docking.Docking "+m,t);
    }
    protected static void Debug(String m){
        Log.d(TAG,"com.johnpritchard.docking.Docking "+m);
    }
    protected static void Debug(String m, Throwable t){
        Log.d(TAG,"com.johnpritchard.docking.Docking "+m,t);
    }
    protected static void Info(String m){
        Log.i(TAG,"com.johnpritchard.docking.Docking "+m);
    }
    protected static void Info(String m, Throwable t){
        Log.i(TAG,"com.johnpritchard.docking.Docking "+m,t);
    }
    protected static void Warn(String m){
        Log.w(TAG,"com.johnpritchard.docking.Docking "+m);
    }
    protected static void Warn(String m, Throwable t){
        Log.w(TAG,"com.johnpritchard.docking.Docking "+m,t);
    }
    protected static void Error(String m){
        Log.e(TAG,"com.johnpritchard.docking.Docking "+m);
    }
    protected static void Error(String m, Throwable t){
        Log.e(TAG,"com.johnpritchard.docking.Docking "+m,t);
    }
    protected static void WTF(String m){
        Log.wtf(TAG,"com.johnpritchard.docking.Docking "+m);
    }
    protected static void WTF(String m, Throwable t){
        Log.wtf(TAG,"com.johnpritchard.docking.Docking "+m,t);
    }
}
