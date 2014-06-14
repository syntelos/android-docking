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

import java.util.Date;

import javax.microedition.khronos.opengles.GL10;

/**
 * 
 */
public abstract class DockingPageGameAbstract
    extends ViewPage3D
{
    protected final static double Z = 0.11; // Near Plane (see "Perspective", below)

    protected final static DockingOutputVx out_vx = new DockingOutputVx   (-3.0, +1.50, Z, 0.12);

    protected final static DockingOutputAx out_ax = new DockingOutputAx   (-0.7, +1.50, Z, 0.12);

    protected final static DockingOutputT  out_t  = new DockingOutputT    (+1.7, +1.50, Z, 0.12);


    protected final static DockingOutputRx out_rx = new DockingOutputRx   (-3.0, +1.25, Z, 0.12);

    protected final static DockingOutputTr out_tr = new DockingOutputTr   (+1.7, +1.25, Z, 0.12);

    protected final static DockingOutputL out_lv = new DockingOutputL     (+1.7, +1.00, Z, 0.12);

    protected final static DockingOutputM out_m = new DockingOutputM      (+1.7, +0.75, Z, 0.12);

    protected final static DockingOutputT10 out_t10 = new DockingOutputT10(+1.7, +0.50, Z, 0.12);

    protected final static DockingOutputT01 out_t01 = new DockingOutputT01(+1.7, +0.25, Z, 0.12);


    protected final static DockingFieldIO in_xp0 = new DockingFieldIO(PhysicsOperator.TX0, PhysicsDOF.XP,
                                                                        -3.0, +1.00, Z, 0.12);

    protected final static DockingFieldIO in_xm0 = new DockingFieldIO(PhysicsOperator.TX0, PhysicsDOF.XM,
                                                                        -3.0, +0.75, Z, 0.12);


    protected final static DockingFieldIO in_xp1 = new DockingFieldIO(PhysicsOperator.TX1, PhysicsDOF.XP,
                                                                        -3.0, +0.50, Z, 0.12);

    protected final static DockingFieldIO in_xm1 = new DockingFieldIO(PhysicsOperator.TX1, PhysicsDOF.XM,
                                                                        -3.0, +0.25, Z, 0.12);


    protected final static DockingOutputScore      out_score = new DockingOutputScore (-1.5, -0.6, Z, 0.3);

    protected final static ViewPage3DTextLabel     out_crash = new ViewPage3DTextLabel(-1.5, -0.6, Z, 0.5,"Crash!");

    protected final static DockingOutputIdentifier out_identifier = new DockingOutputIdentifier(-3.0, -1.25, Z, 0.12);
    protected final static DockingOutputCreated    out_created    = new DockingOutputCreated   (-3.0, -1.50, Z, 0.12);
    protected final static DockingOutputCompleted  out_completed  = new DockingOutputCompleted (-3.0, -1.75, Z, 0.12);

    /**
     * Copy
     */
    public final static void View(){

        final DockingCraftStateVector state = DockingCraftStateVector.Instance;

        synchronized(state){
            /*
             * Read
             */
            final float rx = state.range_x;
            final float ax = state.acceleration_x;
            final float vx = state.velocity_x;

            final long t_clock = state.time_clock;
            final long t_source = state.time_source;
            final long t_xp0 = state.time_xp0;
            final long t_xm0 = state.time_xm0;
            final long t_xp1 = state.time_xp1;
            final long t_xm1 = state.time_xm1;

            final String id = state.identifier;
            final float score = state.score;
            final Date created = state.created;
            final Date completed = state.completed;
            final DockingGameLevel level = state.level;
            /*
             * Write
             */
            out_rx.format(rx);
            out_vx.format(vx);
            out_ax.format(ax);

            out_identifier.format(id);
            out_score.format(score);
            out_created.format(created);
            out_completed.format(completed);

            out_t.format(Seconds(t_clock));
            out_tr.format(Seconds(t_source));

            out_lv.format(level);
            out_m.format(level);
            out_t10.format(level);
            out_t01.format(level);

            in_xp0.format(Seconds(t_xp0));
            in_xm0.format(Seconds(t_xm0));
            in_xp1.format(Seconds(t_xp1));
            in_xm1.format(Seconds(t_xm1));

            Range(state.range_x);
        }
    }
    /**
     * Copy
     */
    public final static void Play(){

        final DockingCraftStateVector state = DockingCraftStateVector.Instance;

        synchronized(state){
            /*
             * Read
             */
            final float rx = state.range_x;
            final float ax = state.acceleration_x;
            final float vx = state.velocity_x;

            final long t_clock = state.time_clock;
            final long t_source = state.time_source;
            final long t_xp0 = state.time_xp0;
            final long t_xm0 = state.time_xm0;
            final long t_xp1 = state.time_xp1;
            final long t_xm1 = state.time_xm1;
            /*
             * Write
             */
            out_rx.format(rx);
            out_vx.format(vx);
            out_ax.format(ax);

            out_t.format(Seconds(t_clock));
            out_tr.format(Seconds(t_source));

            in_xp0.format(Seconds(t_xp0));
            in_xm0.format(Seconds(t_xm0));
            in_xp1.format(Seconds(t_xp1));
            in_xm1.format(Seconds(t_xm1));

            Range(state.range_x);
        }
    }
    /**
     * Copy
     */
    public final static void Range(float r_x){

        final int next = (0 == model_matrix_current)?(1):(0);

        final FloatBuffer mm = model_matrix[next];
        {
            float[] m = fv3.math.Matrix.Identity();

            fv3.math.Matrix.Translate(m,0f,0f,-r_x);

            mm.put(m);
            mm.position(0);
        }
        model_matrix_current = next;
    }


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

    protected final static FloatBuffer[] model_matrix = new FloatBuffer[2];

    protected volatile static int model_matrix_current = -1;

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
            final ByteBuffer ib_mx = ByteBuffer.allocateDirect(16 * bpf);
            ib_mx.order(nativeOrder);
            model_matrix[0] = ib_mx.asFloatBuffer();
        }
        {
            final ByteBuffer ib_mx = ByteBuffer.allocateDirect(16 * bpf);
            ib_mx.order(nativeOrder);
            model_matrix[1] = ib_mx.asFloatBuffer();
        }
        model_matrix_current = 0;
    }


    public DockingPageGameAbstract(){
        super();
    }
    public DockingPageGameAbstract(ViewPage3DComponent[] li){
        super(li);
    }


    @Override
    public void init(GL10 gl){

        glViewport(0,0,width,height);

        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_POINT_SMOOTH);
        glShadeModel(GL_SMOOTH);    
        glDisable(GL_DITHER);

        glLineWidth(1.0f);
        glPointSize(1.0f);
        glFrontFace(GL_CCW);
        glDisable(GL_CULL_FACE);

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
         * Lighting
         */
        glLightModelfv(GL_LIGHT_MODEL_AMBIENT,lightAmbient);

        glLightfv(GL_LIGHT0,GL_POSITION,lightPos0);
        glLightfv(GL_LIGHT0,GL_DIFFUSE,lightDif0);

        glLightfv(GL_LIGHT1,GL_POSITION,lightPos1);
        glLightfv(GL_LIGHT1,GL_DIFFUSE,lightDif1);

        /*
         * View
         */
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glMultMatrixf(camera);

        /*
         */
        glClearColor(1.0f,1.0f,1.0f,1.0f);

    }


    protected void draw(){

        out_vx.draw();

        out_ax.draw();

        out_t.draw();

        out_rx.draw();

        out_tr.draw();

        super.draw();
    }

}
