/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.opengl.GLES10;
import static android.opengl.GLES10.*;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.os.Build;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 
 */
public final class DockingPageGameHardware
    extends DockingPageGameAbstract
{

    public final static DockingPageGameHardware Instance = new DockingPageGameHardware();

    private final static double COL2_X0 = -3.30;
    private final static double COL2_X1 = +0.00;

    private final static double COL3_X0 = -3.30;
    private final static double COL3_X1 = -1.70;
    private final static double COL3_X2 = +0.00;

    private final static double Y0 = +1.50;
    private final static double Y1 = +1.61;

    private final static double DY = -0.25;

    private final static int COL2 = 13;
    private final static int COL3 = (COL2<<1);

    private boolean stale = true;

    private String vendor, version, extensions;

    private final DockingOutputHardwareVendor out_hw_vendor = 
        new DockingOutputHardwareVendor    (COL2_X0, +1.75, Z, 0.12);

    private DockingOutputHardwareExtensions[] out_hw_extensions;

    private int out_hw_extensions_count;


    private DockingPageGameHardware(){
        super();
    }


    @Override
    public String name(){
        return Page.gamehardware.name();
    }
    @Override
    public Page value(){
        return Page.gamehardware;
    }
    public void draw(GL10 gl){

        if (stale){
            stale = false;

            glClearColor(0.0f,0.0f,0.0f,1.0f);

            if (0 == out_hw_extensions_count){

                vendor = glGetString(GL_VENDOR);
                version = glGetString(GL_VERSION);
                extensions = glGetString(GL_EXTENSIONS);

                out_hw_vendor.format(vendor, version, Build.MODEL, Build.DISPLAY);

                extensions(extensions);
            }
        }
        else {

            glClear(CLR);

            glColor4f(1.0f,1.0f,1.0f,1.0f);

            draw();

            glFlush();
        }
    }
    @Override
    protected void draw(){

        out_hw_vendor.draw();

        for (int cc = 0, count = out_hw_extensions_count; cc < count; cc++){

            out_hw_extensions[cc].draw();
        }
    }
    @Override
    protected void focus(){

        stale = true;

        for (ViewPage3DComponent c: this.components){

            if (c instanceof DockingFieldIO){

                c.clearCurrent();
                c.setCurrent();
            }
        }
    }
    @Override
    protected void navigation(){
    }
    @Override
    public void input(InputScript in){

        Input type = in.type();
        switch(type){
        case Up:
            /*
             * ad astra
             */
            Docking.Post2D(new DockingPostStartModel());
            break;
        case Down:
            view.script(Page.about);
            break;
        case Left:
        case Right:
        case Enter:
            view.script(Page.start);
            break;
        }
    }
    protected void extensions(String source){

        String[] extensions = null;
        {
            int start = 0, end = 0;
            do {
                end = source.indexOf(' ',start);
                if (start < end){

                    String item = source.substring(start,end);

                    extensions = Add(extensions,item);

                    start = (end+1);
                }
                else {
                    break;
                }
            }
            while (true);
        }

        if (null != extensions){

            final int count = extensions.length;

            DockingOutputHardwareExtensions[] list = new DockingOutputHardwareExtensions[count];

            if (COL3 < count){

                double y0 = Y0, y1 = Y1, y2 = Y0;

                for (int cc = 0; cc < count; cc++){

                    DockingOutputHardwareExtensions item;

                    if (COL3 < cc){

                        item = new DockingOutputHardwareExtensions(COL3_X2, y2, Z, 0.12);
                        y2 += DY;
                    }
                    else if (COL2 < cc){

                        item = new DockingOutputHardwareExtensions(COL3_X1, y1, Z, 0.12);
                        y1 += DY;
                    }
                    else {
                        item = new DockingOutputHardwareExtensions(COL3_X0, y0, Z, 0.12);
                        y0 += DY;
                    }

                    item.format(extensions[cc]);

                    list[cc] = item;
                }
            }
            else {
                double y0 = Y0, y1 = Y1;

                for (int cc = 0; cc < count; cc++){

                    DockingOutputHardwareExtensions item;

                    if (COL2 < cc){

                        item = new DockingOutputHardwareExtensions(COL2_X1, y1, Z, 0.12);
                        y1 += DY;
                    }
                    else {
                        item = new DockingOutputHardwareExtensions(COL2_X0, y0, Z, 0.12);
                        y0 += DY;
                    }

                    item.format(extensions[cc]);

                    list[cc] = item;
                }
            }
            this.out_hw_extensions = list;
            this.out_hw_extensions_count = count;
        }
        else {
            this.out_hw_extensions_count = 0;
        }
    }

}
