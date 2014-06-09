/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

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
public abstract class DockingPageGameAbstract
    extends ViewPage3D
{
    protected final static float[] CAMERA = fv3.math.Matrix.Identity();
    static {
        Matrix.setLookAtM(CAMERA, 0,
                          0.0f,  0.0f,  5.0f,
                          0.0f,  0.0f,  0.0f,
                          0.0f,  1.0f,  0.0f);
    }
    protected final static float[] LIGHT_AMB = {
        0.3f, 0.3f, 0.3f, 1.0f
    };
    protected final static float[] LIGHT_POS0 = {
        1.0f, 1.0f, 0.0f, 0.0f
    };
    protected final static float[] LIGHT_DIF0 = {
        0.8f, 0.8f, 0.8f, 1.0f
    };
    protected final static float[] LIGHT_POS1 = {
        5.0f, -5.0f, 0.0f, 0.0f
    };
    protected final static float[] LIGHT_DIF1 = {
        0.4f, 0.4f, 0.4f, 1.0f
    };

    protected final static float[] MAT_SHIN = {
        5.0f
    };
    protected final static float[] MAT_SPEC = {
        0.5f, 0.5f, 0.5f, 1.0f
    };

    protected final static FloatBuffer camera;

    protected final static FloatBuffer lightAmbient, lightPos0, lightDif0, lightPos1, lightDif1;

    protected final static FloatBuffer matShin, matSpec;

    protected final static FloatBuffer matrixR, matrixI;


    protected final static View3DTextLabel m0 = new View3DTextLabel(-2.5, -1.2,  1.0, 0.1);
    protected final static View3DTextLabel m1 = new View3DTextLabel(-2.5, -1.35, 1.0, 0.1);
    protected final static View3DTextLabel m2 = new View3DTextLabel(-2.5, -1.5,  1.0, 0.1);

    protected final static View3DTextLabel el = new View3DTextLabel(+1.0, +1.2,  1.0, 0.2);
    protected final static View3DTextLabel az = new View3DTextLabel(+1.0, +0.9,  1.0, 0.2);

    static {
        {
            final ByteBuffer ib_camera = ByteBuffer.allocateDirect(CAMERA.length * bpf);
            ib_camera.order(nativeOrder);
            camera = ib_camera.asFloatBuffer();
            camera.put(CAMERA);
            camera.position(0);
        }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT_AMB.length * bpf);
            ib_light.order(nativeOrder);
            lightAmbient = ib_light.asFloatBuffer();
            lightAmbient.put(LIGHT_AMB);
            lightAmbient.position(0);
        }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT_POS0.length * bpf);
            ib_light.order(nativeOrder);
            lightPos0 = ib_light.asFloatBuffer();
            lightPos0.put(LIGHT_POS0);
            lightPos0.position(0);
        }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT_DIF0.length * bpf);
            ib_light.order(nativeOrder);
            lightDif0 = ib_light.asFloatBuffer();
            lightDif0.put(LIGHT_DIF0);
            lightDif0.position(0);
        }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT_POS1.length * bpf);
            ib_light.order(nativeOrder);
            lightPos1 = ib_light.asFloatBuffer();
            lightPos1.put(LIGHT_POS1);
            lightPos1.position(0);
        }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT_DIF1.length * bpf);
            ib_light.order(nativeOrder);
            lightDif1 = ib_light.asFloatBuffer();
            lightDif1.put(LIGHT_DIF1);
            lightDif1.position(0);
        }
        {
            final ByteBuffer ib_mat = ByteBuffer.allocateDirect(MAT_SHIN.length * bpf);
            ib_mat.order(nativeOrder);
            matShin = ib_mat.asFloatBuffer();
            matShin.put(MAT_SHIN);
            matShin.position(0);
        }
        {
            final ByteBuffer ib_mat = ByteBuffer.allocateDirect(MAT_SPEC.length * bpf);
            ib_mat.order(nativeOrder);
            matSpec = ib_mat.asFloatBuffer();
            matSpec.put(MAT_SPEC);
            matSpec.position(0);
        }
        {
            final ByteBuffer ib_mat = ByteBuffer.allocateDirect(16 * bpf);
            ib_mat.order(nativeOrder);
            matrixR = ib_mat.asFloatBuffer();
        }
        {
            final ByteBuffer ib_mat = ByteBuffer.allocateDirect(16 * bpf);
            ib_mat.order(nativeOrder);
            matrixI = ib_mat.asFloatBuffer();
        }
    }


    public DockingPageGameAbstract(){
        super();
    }


    @Override
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
    }
    @Override
    public void input(InputScript in){

        if (Input.Back == in){

            Docking.StartMain();
        }
    }
    public void update(){

        // m0.format("M00 %s M01 %s M02 %s",Format7(m[0]),Format7(m[1]),Format7(m[2]));
        // m1.format("M10 %s M11 %s M12 %s",Format7(m[4]),Format7(m[5]),Format7(m[6]));
        // m2.format("M20 %s M21 %s M22 %s",Format7(m[8]),Format7(m[9]),Format7(m[10]));

        // el.format("EL %s",Format4(r.rotationEL()*DEG));
        // az.format("AZ %s",Format4(r.rotationAZ()*DEG));
    }
    protected final static double DEG = 180.0/Math.PI;

    protected final static String Format7(float value){
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
    protected final static String Format4(double value){
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
