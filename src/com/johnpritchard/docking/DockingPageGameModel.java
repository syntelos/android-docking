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

    private final static double RY_INIT = -(2*MOD_ROT);


    private boolean stale = true;

    private boolean lines = true;

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

            glClearColor(0.0f,0.0f,0.0f,1.0f);
        }
        else {

            glClear(CLR);

            if (lines){

                FloatBuffer mm = model_matrix[model_matrix_current];

                glPushMatrix();

                glMultMatrixf(mm);

                glColor4f(1.0f,1.0f,1.0f,1.0f);

                DockingGeometryPort.Instance.lines();

                glPopMatrix();
            }
            else
            {
                glEnable(GL_LIGHT0);
                glEnable(GL_LIGHTING);

                glPushMatrix();

                glMultMatrixf(model_matrix[model_matrix_current]);

                glColor4f(MOD_COL_R,MOD_COL_G,MOD_COL_B,MOD_COL_A);

                glEnable(GL_COLOR_MATERIAL);

                DockingGeometryPort.Instance.triangles();

                glDisable(GL_COLOR_MATERIAL);

                glPopMatrix();

                glDisable(GL_LIGHT0);
                glDisable(GL_LIGHTING);
            }
            {
                glColor4f(1.0f,1.0f,1.0f,1.0f);

                draw();
            }
            glFlush();
        }
    }
    @Override
    protected void draw(){

        out_ry.draw();

        out_rx.draw();
    }
    @Override
    protected void focus(){

        stale = true;

        rotation_y = ModelRotY(RY_INIT);
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

        case Enter:

            lines = (!lines);
            break;

        default:
            break;
        }
    }
}
