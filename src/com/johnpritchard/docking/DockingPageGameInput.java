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

            sv0.draw();
            sv1.draw();
            sv2.draw();

            glFlush();
        }
    }
    @Override
    public void input(InputScript in){

        info(in.toString());

        Input type = in.type();

        if (Input.Enter == type){

            view.script(Page.gameView);
        }
        else {

            super.input(in);
        }
    }
}
