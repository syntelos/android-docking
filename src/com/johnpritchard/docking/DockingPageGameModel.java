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
public final class DockingPageGameModel
    extends DockingPageGameAbstract
{

    public final static DockingPageGameModel Instance = new DockingPageGameModel();


    private boolean stale = true;

    private double rotation_y;


    private DockingPageGameModel(){
        super();
    }


    @Override
    public String name(){
        return Page.gamemodel.name();
    }
    @Override
    public Page value(){
        return Page.gamemodel;
    }
    public void draw(GL10 gl){

        if (stale){
            stale = false;

            glClearColor(1.0f,1.0f,1.0f,1.0f);

            glEnable(GL_LIGHT0);
            glEnable(GL_LIGHT1);
            glEnable(GL_LIGHTING);
            glEnable(GL_COLOR_MATERIAL);
        }
        else {

            glClear(CLR);

            model();

            glColor4f(0.0f,0.0f,0.0f,1.0f);

            out_ry.draw();

            out_rx.draw();

            glFlush();
        }
    }
    @Override
    protected void focus(){
    }
    @Override
    protected void navigation(){
    }
    @Override
    public void input(InputScript in){

        Input type = in.type();
        switch(type){
        case Up:

            ModelRangeInc(rotation_y);
            break;

        case Down:

            ModelRangeDec(rotation_y);
            break;

        case Left:

            rotation_y = ModelRotYM(rotation_y);
            break;

        case Right:

            rotation_y = ModelRotYP(rotation_y);
            break;

        default:

            view.script(Page.start);
            break;
        }
    }
}
