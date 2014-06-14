/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.Surface;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 
 */
public class DockingPostScreenShot
    extends ObjectLog
    implements Runnable
{
    private final static Class[] SS_PARAMS = {
        Integer.TYPE, Integer.TYPE
    };

    private final View3D view;
    private final int width, height;
    private final String id;
    private final boolean media;
    private final boolean valid;

    public DockingPostScreenShot(View3D view){
        super();
        if (null != view){

            this.view = view;
            this.width = view.getWidth();
            this.height = view.getHeight();

            this.id = DockingCraftStateVector.Instance.identifier;
            this.valid = (null != this.id && 0 < this.id.length());

            this.media = (valid && 
                          Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()));
        }
        else {
            throw new IllegalArgumentException();
        }
    }


    protected Bitmap screenshot(){
    // public static native void glReadPixels(
    //     int x,
    //     int y,
    //     int width,
    //     int height,
    //     int format,
    //     int type,
    //     java.nio.Buffer pixels
    // );

        Class surface = Surface.class;
        try {
            Method screenshot = surface.getMethod("screenshot",SS_PARAMS);

            return (Bitmap)screenshot.invoke(null,width,height);
        }
        catch (NoSuchMethodException exc){
            throw new RuntimeException("screenshot",exc);
        }
        catch (IllegalAccessException exc){
            throw new RuntimeException("screenshot",exc);
        }
        catch (InvocationTargetException exc){
            throw new RuntimeException("screenshot",exc);
        }
    }

    public void run(){
        if (media){
            try {
                final File dir = 
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                dir.mkdirs();

                final String filename = "Docking-"+this.id+".png";

                final File file = new File(dir,filename);

                final Bitmap raw = screenshot();

                FileOutputStream fout = new FileOutputStream(file);
                try {

                    raw.compress(Bitmap.CompressFormat.PNG,100,fout);

                    fout.flush();

                    info(filename);
                }
                finally {
                    fout.close();
                }
            }
            catch (Exception exc){
                error("post",exc);
            }
        }
        else if (valid){
            error("missing media");
        }
        else {
            error("missing identifier");
        }
    }

}
