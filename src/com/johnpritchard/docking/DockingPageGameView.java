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
public final class DockingPageGameView
    extends DockingPageGameAbstract
{

    public final static DockingPageGameView Instance = new DockingPageGameView();


    private boolean stale = true;


    private DockingPageGameView(){
        super(new ViewPage3DComponent[]{
                in_xp0, in_xm0, in_xp1, in_xm1
            });
    }


    @Override
    public String name(){
        return Page.gameview.name();
    }
    @Override
    public Page value(){
        return Page.gameview;
    }
    public void draw(GL10 gl){

        if (stale){
            stale = false;
            /*
             * Light
             */
            glLightModelfv(GL_LIGHT_MODEL_AMBIENT,lightAmbient);

            glLightfv(GL_LIGHT0,GL_POSITION,lightPos0);
            glLightfv(GL_LIGHT0,GL_DIFFUSE,lightDif0);

            glLightfv(GL_LIGHT1,GL_POSITION,lightPos1);
            glLightfv(GL_LIGHT1,GL_DIFFUSE,lightDif1);

            glEnable(GL_LIGHT0);
            glEnable(GL_LIGHT1);
            glEnable(GL_LIGHTING);
        }
        else {
            FloatBuffer mm = model_matrix[model_matrix_current];

            glClear(CLR);

            glPushMatrix();

            glMultMatrixf(mm);

            glColor4f(0.0f,0.0f,0.0f,1.0f);

            /* Model
             */

            glMaterialfv(GL_FRONT,GL_SHININESS,matShin);
            glMaterialfv(GL_FRONT,GL_SPECULAR,matSpec);

            DockingGeometryPort.Instance.draw();

            glPopMatrix();

            draw();

            glFlush();
        }
    }
    @Override
    public ViewPage up(View view, int w, int h){
        super.up(view,w,h);

        DockingPhysics.Start(view.preferences());

        return this;
    }
    @Override
    public void down(SharedPreferences.Editor state){
        super.down(state);

        DockingPhysics.Stop(state);
    }
}
