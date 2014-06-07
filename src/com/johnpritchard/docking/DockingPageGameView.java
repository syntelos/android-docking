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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 
 */
public final class DockingPageGameView
    extends ViewPage3D
{

    public final static DockingPageGameView Instance = new DockingPageGameView();


    private DockingPageGameView(){
        super();
    }


    @Override
    public String name(){
        return Page.gameView.name();
    }
    @Override
    public Page value(){
        return Page.gameView;
    }
    public void init(GL10 gl){
        super.init(gl);
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
    public void draw(GL10 gl){

        if (stale){
            init(gl);
        }
        else {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // glPushMatrix();

            // glMultMatrixf(matrixR);

            /* Model
             */
            glColor4f(0.9f,0.9f,0.9f,1.0f);


            glMaterialfv(GL_FRONT,GL_SHININESS,matShin);
            glMaterialfv(GL_FRONT,GL_SPECULAR,matSpec);

            GeometrySphere.Instance.draw();

            // glPopMatrix();

            m0.draw();
            m1.draw();
            m2.draw();

            el.draw();
            az.draw();

            glFlush();
        }
    }
}
