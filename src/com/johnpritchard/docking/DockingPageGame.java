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
public final class DockingPageGame
    extends ViewPage3D
{

    private final static float[] CAMERA = fv3.math.Matrix.Identity();
    static {
        Matrix.setLookAtM(CAMERA, 0,
                          0.0f,  0.0f,  5.0f,
                          0.0f,  0.0f,  0.0f,
                          0.0f,  1.0f,  0.0f);
    }
    private final static float[] LIGHT_AMB = {
        0.3f, 0.3f, 0.3f, 1.0f
    };
    private final static float[] LIGHT_POS0 = {
        1.0f, 1.0f, 0.0f, 0.0f
    };
    private final static float[] LIGHT_DIF0 = {
        0.8f, 0.8f, 0.8f, 1.0f
    };
    private final static float[] LIGHT_POS1 = {
        5.0f, -5.0f, 0.0f, 0.0f
    };
    private final static float[] LIGHT_DIF1 = {
        0.4f, 0.4f, 0.4f, 1.0f
    };

    private final static float[] MAT_SHIN = {
        5.0f
    };
    private final static float[] MAT_SPEC = {
        0.5f, 0.5f, 0.5f, 1.0f
    };


    public final static DockingPageGame Instance = new DockingPageGame();


    private final FloatBuffer camera;

    private final FloatBuffer lightAmbient, lightPos0, lightDif0, lightPos1, lightDif1;

    private final FloatBuffer matShin, matSpec;

    private final FloatBuffer matrixR, matrixI;


    private final FontGlyphVector m0 = new FontGlyphVector(-2.5, -1.2,  1.0, 0.1);
    private final FontGlyphVector m1 = new FontGlyphVector(-2.5, -1.35, 1.0, 0.1);
    private final FontGlyphVector m2 = new FontGlyphVector(-2.5, -1.5,  1.0, 0.1);

    private final FontGlyphVector el = new FontGlyphVector(+1.0, +1.2,  1.0, 0.2);
    private final FontGlyphVector az = new FontGlyphVector(+1.0, +0.9,  1.0, 0.2);


    private DockingPageGame(){
        super();
        {
            final ByteBuffer ib_camera = ByteBuffer.allocateDirect(CAMERA.length * bpf);
            ib_camera.order(nativeOrder);
            this.camera = ib_camera.asFloatBuffer();
            this.camera.put(CAMERA);
            this.camera.position(0);
        }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT_AMB.length * bpf);
            ib_light.order(nativeOrder);
            this.lightAmbient = ib_light.asFloatBuffer();
            this.lightAmbient.put(LIGHT_AMB);
            this.lightAmbient.position(0);
        }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT_POS0.length * bpf);
            ib_light.order(nativeOrder);
            this.lightPos0 = ib_light.asFloatBuffer();
            this.lightPos0.put(LIGHT_POS0);
            this.lightPos0.position(0);
        }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT_DIF0.length * bpf);
            ib_light.order(nativeOrder);
            this.lightDif0 = ib_light.asFloatBuffer();
            this.lightDif0.put(LIGHT_DIF0);
            this.lightDif0.position(0);
        }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT_POS1.length * bpf);
            ib_light.order(nativeOrder);
            this.lightPos1 = ib_light.asFloatBuffer();
            this.lightPos1.put(LIGHT_POS1);
            this.lightPos1.position(0);
        }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT_DIF1.length * bpf);
            ib_light.order(nativeOrder);
            this.lightDif1 = ib_light.asFloatBuffer();
            this.lightDif1.put(LIGHT_DIF1);
            this.lightDif1.position(0);
        }
        {
            final ByteBuffer ib_mat = ByteBuffer.allocateDirect(MAT_SHIN.length * bpf);
            ib_mat.order(nativeOrder);
            this.matShin = ib_mat.asFloatBuffer();
            this.matShin.put(MAT_SHIN);
            this.matShin.position(0);
        }
        {
            final ByteBuffer ib_mat = ByteBuffer.allocateDirect(MAT_SPEC.length * bpf);
            ib_mat.order(nativeOrder);
            this.matSpec = ib_mat.asFloatBuffer();
            this.matSpec.put(MAT_SPEC);
            this.matSpec.position(0);
        }
        {
            final ByteBuffer ib_mat = ByteBuffer.allocateDirect(16 * bpf);
            ib_mat.order(nativeOrder);
            this.matrixR = ib_mat.asFloatBuffer();
        }
        {
            final ByteBuffer ib_mat = ByteBuffer.allocateDirect(16 * bpf);
            ib_mat.order(nativeOrder);
            this.matrixI = ib_mat.asFloatBuffer();
        }
    }


    @Override
    public String name(){
        return Page.game.name();
    }
    @Override
    public Page value(){
        return Page.game;
    }
    public void init(GL10 gl){

        stale = false;

        glViewport(0,0,width,height);

        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_POINT_SMOOTH);
        glShadeModel(GL_SMOOTH);    
        glDisable(GL_DITHER);

        glLineWidth(1.0f);
        glPointSize(1.0f);
        glFrontFace(GL_CCW);
        glDisable(GL_CULL_FACE);
        glClearColor(1.0f,1.0f,1.0f,1.0f);
        glEnable(GL_COLOR_MATERIAL);

        /*
         * Perspective
         */
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        final float fovy = 45.0f;
        final float aspect = ( (float)width / (float)height);
        final float near = 0.1f;
        final float far = 1000.0f;

        gluPerspective(fovy,aspect,near,far);

        /*
         * View
         */
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glMultMatrixf(camera);

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
            glColor4f(0.0f,0.4f,0.7f,1.0f);


            glMaterialfv(GL_FRONT,GL_SHININESS,matShin);
            glMaterialfv(GL_FRONT,GL_SPECULAR,matSpec);

            GeometryAttitude.Instance.draw();

            GeometrySyntelos.Instance.draw();

            // glPopMatrix();

            // m0.draw();
            // m1.draw();
            // m2.draw();

            // el.draw();
            // az.draw();

            glFlush();
        }
    }
    @Override
    public void input(Input in){

        switch(in){

        case Enter:

            return;

        case Back:
            Docking.StartMain();
            return;

        default:
            return;
        }
    }
    public void update(InterfaceRotation r){
        /*
         * Absolute put for coherency in the race condition
         * 
         * The MT conflict between the Sensor and the GPU is limited
         * to the changes in values within the buffer.
         */
        float[] m = r.rotationMatrix();

        MatrixCopy(m,this.matrixR);

        m0.format("M00 %s M01 %s M02 %s",Format7(m[0]),Format7(m[1]),Format7(m[2]));
        m1.format("M10 %s M11 %s M12 %s",Format7(m[4]),Format7(m[5]),Format7(m[6]));
        m2.format("M20 %s M21 %s M22 %s",Format7(m[8]),Format7(m[9]),Format7(m[10]));

        el.format("EL %s",Format4(r.rotationEL()*DEG));
        az.format("AZ %s",Format4(r.rotationAZ()*DEG));
    }
    private final static double DEG = 180.0/Math.PI;

    private final static String Format7(float value){
        final String string = String.format("%3.4f",value);
        final char[] sary = string.toCharArray();
        final int strlen = sary.length;
        if (7 <= strlen)
            return string;
        else {
            char[] cary = new char[7];
            {
                for (int cc = 0, sp = (7-strlen); cc < 7; cc++){
                    if (cc < sp){
                        cary[cc] = ' ';
                    }
                    else {
                        cary[cc] = sary[cc-sp];
                    }
                }
            }
            return new String(cary,0,7);
        }
    }
    private final static String Format4(double value){
        final String string = String.format("%3.0f",value);
        final char[] sary = string.toCharArray();
        final int strlen = sary.length;
        if (4 <= strlen)
            return string;
        else {
            char[] cary = new char[4];
            {
                for (int cc = 0, sp = (4-strlen); cc < 4; cc++){
                    if (cc < sp){
                        cary[cc] = ' ';
                    }
                    else {
                        cary[cc] = sary[cc-sp];
                    }
                }
            }
            return new String(cary,0,4);
        }
    }

}
