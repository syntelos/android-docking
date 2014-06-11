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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 
 */
public final class DockingPageGameCrash
    extends DockingPageGameAbstract
{

    public final static DockingPageGameCrash Instance = new DockingPageGameCrash();


    private DockingPageGameCrash(){
        super(new ViewPage3DComponent[]{
                new ViewPage3DTextLabel(-1.5,-0.5,1.0,0.5,"Crash!")
            });
    }


    @Override
    public String name(){
        return Page.gamecrash.name();
    }
    @Override
    public Page value(){
        return Page.gamecrash;
    }
    public void init(GL10 gl){
        super.init(gl);

        glClearColor(0.0f,0.0f,0.0f,1.0f);

    }
    public void draw(GL10 gl){

        if (stale){
            init(gl);
        }
        else {

            glClear(CLR);

            glColor4f(1.0f,1.0f,1.0f,1.0f);

            draw();

            glFlush();
        }
    }
}
