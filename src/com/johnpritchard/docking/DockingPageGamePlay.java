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
public final class DockingPageGamePlay
    extends DockingPageGameAbstract
{

    public final static DockingPageGamePlay Instance = new DockingPageGamePlay();

    private final static int TITLE = 90;


    private boolean stale = true;

    private volatile boolean title = true;



    private DockingPageGamePlay(){
        super(new ViewPage3DComponent[]{
                in_xp0, in_xm0, in_xp1, in_xm1
            });
    }


    @Override
    public String name(){
        return Page.gameplay.name();
    }
    @Override
    public Page value(){
        return Page.gameplay;
    }
    public void draw(GL10 gl){

        if (stale){

            stale = false;

            glClearColor(0.0f,0.0f,0.0f,1.0f);
        }
        else if (title){

            glClear(CLR);
            {
                glColor4f(0.9f,0.9f,1.0f,1.0f);

                DockingGeometryStarfield.Instance.draw();

                out_movie.draw();
            }
            glFlush();
        }
        else {

            glClear(CLR);
            {
                glColor4f(0.9f,0.9f,1.0f,1.0f);

                DockingGeometryStarfield.Instance.draw();
            }
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
    public ViewPage up(View view, int w, int h){
        super.up(view,w,h);

        DockingPhysics.Start();

        return this;
    }
    @Override
    public void down(){
        super.down();

        DockingPhysics.Stop();
    }
    @Override
    protected void focus(){

        stale = true;

        for (ViewPage3DComponent c: this.components){

            if (c instanceof DockingFieldIO){

                c.clearCurrent();
            }
        }
    }
    protected boolean isTitle(){

        return title;
    }
    protected void clearTitle(){

        title = false;
    }
}
