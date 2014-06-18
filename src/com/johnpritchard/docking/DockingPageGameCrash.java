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
    extends DockingPageGameView
{

    public final static DockingPageGameCrash Instance = new DockingPageGameCrash();


    private DockingPageGameCrash(){
        super(new ViewPage3DComponent[]{
                in_xp0, in_xm0, in_xp1, in_xm1
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
    public void draw(GL10 gl){

        if (stale){
            stale = false;

            glClearColor(0.0f,0.0f,0.0f,1.0f);
        }
        else {

            glClear(CLR);
            // {
            //     glColor4f(STF_COL_R,STF_COL_G,STF_COL_B,STF_COL_A);

            //     DockingGeometryStarfield.Instance.draw();
            // }
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

        out_crash.draw();
        out_identifier.draw();
        out_created.draw();
        out_completed.draw();

        super.draw();
    }
}
