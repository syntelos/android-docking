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
            FloatBuffer mm = model_matrix[model_matrix_current];

            glClear(CLR);
            {
                glPushMatrix();

                glMultMatrixf(mm);

                glColor4f(0.7f,0.7f,0.7f,1.0f);

                /* Model
                 */

                glMaterialfv(GL_FRONT,GL_SHININESS,matShin);
                glMaterialfv(GL_FRONT,GL_SPECULAR,matSpec);

                DockingGeometryPort.Instance.draw();

                glPopMatrix();
            }

            glColor4f(0.0f,0.0f,0.0f,1.0f);

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
            /*
             * TODO: translate model matrix +/- Z
             */
        case Up:
            break;
        case Left:
            break;
            /*
             * TODO: rotate model matrix +/- R(X/Y)
             */
        case Down:
            break;
        case Right:
            break;
            /*
             * Exit
             */
        default:
            view.script(Page.start);
            break;
        }
    }
}
