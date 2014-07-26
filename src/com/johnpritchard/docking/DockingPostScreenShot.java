/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 */
public class DockingPostScreenShot
    extends View3DScreenShot
    implements Runnable
{

    private final boolean report;


    public DockingPostScreenShot(View3D view, boolean report){
        super(view);
        this.report = report;
    }


    public void run(){
        try {
            screenshot_ext();
            save();

            if (report){

                Docking.Toast3D("Screenshot");
            }
        }
        catch (Throwable t){

            Docking.Toast3D("Screenshot internal error");

            error("Screenshot internal error",t);
        }
    }
}
