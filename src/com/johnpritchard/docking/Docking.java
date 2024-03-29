/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.Context;
import android.content.Intent;
import static android.content.Intent.*;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

/**
 * 
 */
public final class Docking
    extends android.app.Application
{
    private final static String TAG = ObjectLog.TAG;

    protected final static int FileModePublic = (Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE);

    private final static int IntentFlags = (FLAG_ACTIVITY_CLEAR_TOP|FLAG_DEBUG_LOG_RESOLUTION);

    private static DockingActivity2D Activity2D;
    private static DockingActivity3D Activity3D;

    /**
     * Called from activity2D to start activity3D.
     */
    public final static void StartActivity3D(){

        Intent intent = new Intent(Activity2D, DockingActivity3D.class);
        {
            intent.setFlags(IntentFlags);
        }
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
    protected final static void ScreenShot3D(boolean report){
        try {
            Post3D(new DockingPostScreenShot(Activity3D.view,report));
        }
        catch (RuntimeException storage){
            Info("ScreenShot3D",storage);
        }
    }
    protected final static void Toast2D(String msg){

        Toast toast = Toast.makeText(Activity2D, msg, Toast.LENGTH_SHORT);

        toast.show();
    }
    protected final static void Toast3D(String msg){

        Toast toast = Toast.makeText(Activity3D, msg, Toast.LENGTH_SHORT);

        toast.show();
    }
    protected final static File ExternalDirectory2D(String type){
        return Activity2D.getExternalFilesDir(type);
    }
    protected final static File ExternalDirectory3D(String type){
        return Activity3D.getExternalFilesDir(type);
    }
    protected final static FileOutputStream InternalFile2D(String filename)
        throws FileNotFoundException
    {
        return Activity2D.openFileOutput(filename,FileModePublic);
    }
    protected final static FileOutputStream InternalFile3D(String filename)
        throws FileNotFoundException
    {
        return Activity3D.openFileOutput(filename,FileModePublic);
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
