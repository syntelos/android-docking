/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

import android.opengl.GLES10;
import static android.opengl.GLES10.*;
import android.opengl.GLU;
import android.opengl.Matrix;

import javax.microedition.khronos.opengles.GL10;

/**
 * 
 */
public final class DockingPageGameInput
    extends DockingPageGameAbstract
{
    public final static DockingPageGameInput Instance = new DockingPageGameInput();




    private DockingPageGameInput(){
        super();
    }


    @Override
    public String name(){
        return Page.gameInput.name();
    }
    @Override
    public Page value(){
        return Page.gameInput;
    }
    public void draw(GL10 gl){

        if (stale){
            init(gl);
        }
        else {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glColor4f(0.0f,0.0f,0.0f,1.0f);

            m0.draw();
            m1.draw();
            m2.draw();

            el.draw();
            az.draw();

            glFlush();
        }
    }
    @Override
    public void input(InputScript in){

        super.input(in);
    }
}
