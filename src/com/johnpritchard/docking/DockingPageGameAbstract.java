/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.opengl.GLES10;
import static android.opengl.GLES10.*;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.util.Log;

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

    protected final static DockingOutputR out_ry = new DockingOutputR     (-3.0, +1.50, Z, 0.12); //model R(Y)

    protected final static DockingOutputAx out_ax = new DockingOutputAx   (-0.7, +1.50, Z, 0.12);

    protected final static DockingOutputT  out_t  = new DockingOutputT    (+1.7, +1.50, Z, 0.12);


    protected final static DockingOutputRx out_rx = new DockingOutputRx   (-3.0, +1.25, Z, 0.12);

    protected final static DockingOutputTr out_tr = new DockingOutputTr   (+1.7, +1.25, Z, 0.12);

    protected final static DockingOutputL out_lv = new DockingOutputL     (+1.7, +1.00, Z, 0.12);

    protected final static DockingOutputM out_m = new DockingOutputM      (+1.7, +0.75, Z, 0.12);

    protected final static DockingOutputT10 out_t10 = new DockingOutputT10(+1.7, +0.50, Z, 0.12);

    protected final static DockingOutputT01 out_t01 = new DockingOutputT01(+1.7, +0.25, Z, 0.12);


    protected final static DockingIOXP0 in_xp0 = new DockingIOXP0         (-3.0, +1.00, Z, 0.12);

    protected final static DockingIOXM0 in_xm0 = new DockingIOXM0         (-3.0, +0.75, Z, 0.12);


    protected final static DockingIOXP1 in_xp1 = new DockingIOXP1         (-3.0, +0.50, Z, 0.12);

    protected final static DockingIOXM1 in_xm1 = new DockingIOXM1         (-3.0, +0.25, Z, 0.12);


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
            final float rx = (float)state.range_x;
            final float ax = (float)state.acceleration_x;
            final float vx = (float)state.velocity_x;

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

            final DockingGameLevel level = state.level; //
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

            in_xp0.format(Seconds(t_xp0));
            in_xm0.format(Seconds(t_xm0));
            in_xp1.format(Seconds(t_xp1));
            in_xm1.format(Seconds(t_xm1));

            out_lv.format(level);                       //
            out_m.format(level);                        //
            out_t10.format(level);                      //
            out_t01.format(level);                      //

            Range(rx);
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
            final float rx = (float)state.range_x;
            final float ax = (float)state.acceleration_x;
            final float vx = (float)state.velocity_x;

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

            Range(rx);
        }
    }
    /**
     * Copy
     */
    public final static void Range(){

        final DockingCraftStateVector state = DockingCraftStateVector.Instance;

        synchronized(state){
            /*
             * Read
             */
            final float rx = (float)state.range_x;
            /*
             * Write
             */
            out_rx.format(rx);

            Range(rx);
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

    private final static double MOD_LIN = 1.0;
    private final static double MOD_ROT = (Math.PI / 6.0);

    protected final static void ModelRangeInc(double ry){
        final DockingCraftStateVector state = DockingCraftStateVector.Instance;

        float rx;

        synchronized(state){

            state.range_x = Z(state.range_x + MOD_LIN);

            rx = (float)state.range_x;
        }
        out_rx.format(rx);

        ModelMatrixTR(rx,(float)ry);
    }
    protected final static void ModelRangeDec(double ry){
        final DockingCraftStateVector state = DockingCraftStateVector.Instance;

        float rx;

        synchronized(state){

            state.range_x = Z(state.range_x - MOD_LIN);

            rx = (float)state.range_x;
        }
        out_rx.format(rx);

        ModelMatrixTR(rx,(float)ry);
    }
    protected final static double ModelRotYM(double ry){

        ry = Z(ry - MOD_ROT);

        float rx;

        final DockingCraftStateVector state = DockingCraftStateVector.Instance;

        synchronized(state){

            rx = (float)state.range_x;
        }
        out_ry.format((float)ry);

        ModelMatrixTR(rx,(float)ry);

        return ry;
    }
    protected final static double ModelRotYP(double ry){

        ry = Z(ry + MOD_ROT);

        float rx;

        final DockingCraftStateVector state = DockingCraftStateVector.Instance;

        synchronized(state){

            rx = (float)state.range_x;
        }
        out_ry.format((float)ry);

        ModelMatrixTR(rx,(float)ry);

        return ry;
    }
    private final static void ModelMatrixTR(float t_x, float r_y){

        final int next = (0 == model_matrix_current)?(1):(0);

        final FloatBuffer mm = model_matrix[next];
        {
            float[] r = fv3.math.Matrix.RotateY(r_y);

            float[] t = fv3.math.Matrix.Translate(0f,0f,-t_x);

            float[] m = fv3.math.Matrix.Mul(r,t); // (rotate that [there])

            mm.put(m);
            mm.position(0);
        }
        model_matrix_current = next;

    }

    protected final static float MOD_COL_R = 0.9f;
    protected final static float MOD_COL_G = 0.9f;
    protected final static float MOD_COL_B = 0.9f;
    protected final static float MOD_COL_A = 1.0f;

    protected final static float[] CAMERA = fv3.math.Matrix.Identity();
    static {
        Matrix.setLookAtM(CAMERA, 0,
                          0.0f,  0.0f,  5.0f,
                          0.0f,  0.0f,  0.0f,
                          0.0f,  1.0f,  0.0f);
    }
    // protected final static float[] L_GLOB_AMB = {
    //     0.0f, 0.0f, 0.3f, 1.0f
    // };

    protected final static float[] LIGHT0_POS = {
        0.0f, 10.0f, -2.0f, 0.0f
    };
    protected final static float[] LIGHT0_AMB = {
        0.1f, 0.1f, 0.1f, 1.0f
    };
    protected final static float[] LIGHT0_DIF = {
        0.2f, 0.2f, 0.2f, 1.0f
    };
    protected final static float[] LIGHT0_SPE = {
        1.0f, 1.0f, 0.97f, 1.0f
    };
    protected final static float[] LIGHT0_DIR = {
        0.0f, 0.0f, -1000.0f
    };

    protected final static float[] LIGHT1_POS = {
        100.0f, -100.0f, -10.0f, 0.0f
    };
    protected final static float[] LIGHT1_AMB = {
        0.1f, 0.1f, 0.11f, 1.0f
    };
    protected final static float[] LIGHT1_DIF = {
        0.9f, 0.9f, 0.8f, 1.0f
    };
    protected final static float[] LIGHT1_SPE = {
        0.9f, 0.9f, 0.8f, 1.0f
    };
    protected final static float[] LIGHT1_DIR = {
        0.0f, 0.0f, -1000.0f
    };
    protected final static float LIGHT_C_ATT     = 1.5f;
    protected final static float LIGHT_B_ATT     = 0.5f;
    protected final static float LIGHT_A_ATT     = 0.2f;
    protected final static float LIGHT_SPOT_ADEG = 45f;
    protected final static float LIGHT_SPOT_EXP  = 2.0f;

    protected final static float[] MAT_SHIN = {
        5.0f
    };
    protected final static float[] MAT_SPEC = {
        0.5f, 0.5f, 0.5f, 1.0f
    };

    protected final static FloatBuffer camera;

    // protected final static FloatBuffer lightGlobal;

    protected final static FloatBuffer light0_pos, light0_amb, light0_dif, light0_spe, light0_dir;

    protected final static FloatBuffer light1_pos, light1_amb, light1_dif, light1_spe, light1_dir;

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
        // {
        //     final ByteBuffer ib_light = ByteBuffer.allocateDirect(L_GLOB_AMB.length * bpf);
        //     ib_light.order(nativeOrder);
        //     lightGlobal = ib_light.asFloatBuffer();
        //     lightGlobal.put(L_GLOB_AMB);
        //     lightGlobal.position(0);
        // }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT0_POS.length * bpf);
            ib_light.order(nativeOrder);
            light0_pos = ib_light.asFloatBuffer();
            light0_pos.put(LIGHT0_POS);
            light0_pos.position(0);
        }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT0_AMB.length * bpf);
            ib_light.order(nativeOrder);
            light0_amb = ib_light.asFloatBuffer();
            light0_amb.put(LIGHT0_AMB);
            light0_amb.position(0);
        }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT0_DIF.length * bpf);
            ib_light.order(nativeOrder);
            light0_dif = ib_light.asFloatBuffer();
            light0_dif.put(LIGHT0_DIF);
            light0_dif.position(0);
        }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT0_SPE.length * bpf);
            ib_light.order(nativeOrder);
            light0_spe = ib_light.asFloatBuffer();
            light0_spe.put(LIGHT0_SPE);
            light0_spe.position(0);
        }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT0_DIR.length * bpf);
            ib_light.order(nativeOrder);
            light0_dir = ib_light.asFloatBuffer();
            light0_dir.put(LIGHT0_DIR);
            light0_dir.position(0);
        }

        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT1_POS.length * bpf);
            ib_light.order(nativeOrder);
            light1_pos = ib_light.asFloatBuffer();
            light1_pos.put(LIGHT1_POS);
            light1_pos.position(0);
        }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT1_AMB.length * bpf);
            ib_light.order(nativeOrder);
            light1_amb = ib_light.asFloatBuffer();
            light1_amb.put(LIGHT1_AMB);
            light1_amb.position(0);
        }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT1_DIF.length * bpf);
            ib_light.order(nativeOrder);
            light1_dif = ib_light.asFloatBuffer();
            light1_dif.put(LIGHT1_DIF);
            light1_dif.position(0);
        }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT1_SPE.length * bpf);
            ib_light.order(nativeOrder);
            light1_spe = ib_light.asFloatBuffer();
            light1_spe.put(LIGHT1_SPE);
            light1_spe.position(0);
        }
        {
            final ByteBuffer ib_light = ByteBuffer.allocateDirect(LIGHT1_DIR.length * bpf);
            ib_light.order(nativeOrder);
            light1_dir = ib_light.asFloatBuffer();
            light1_dir.put(LIGHT1_DIR);
            light1_dir.position(0);
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
         * View
         */
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glMultMatrixf(camera);

        /*
         */
        glClearColor(1.0f,1.0f,1.0f,1.0f);

        /*
         * Lighting
         */
        // glLightModelfv(GL_LIGHT_MODEL_AMBIENT,lightGlobal);

        glLightfv( GL_LIGHT0, GL_AMBIENT,               light0_amb);
        glLightfv( GL_LIGHT0, GL_DIFFUSE,               light0_dif);
        glLightfv( GL_LIGHT0, GL_SPECULAR,              light0_spe);
        glLightfv( GL_LIGHT0, GL_POSITION,              light0_pos);
        glLightf ( GL_LIGHT0, GL_CONSTANT_ATTENUATION,  LIGHT_C_ATT);
        glLightf ( GL_LIGHT0, GL_LINEAR_ATTENUATION,    LIGHT_B_ATT);
        glLightf ( GL_LIGHT0, GL_QUADRATIC_ATTENUATION, LIGHT_A_ATT);
        glLightf ( GL_LIGHT0, GL_SPOT_CUTOFF,           LIGHT_SPOT_ADEG);
        glLightfv( GL_LIGHT0, GL_SPOT_DIRECTION,        light0_dir);
        glLightf ( GL_LIGHT0, GL_SPOT_EXPONENT,         LIGHT_SPOT_EXP);

        glLightfv( GL_LIGHT1, GL_AMBIENT,               light1_amb);
        glLightfv( GL_LIGHT1, GL_DIFFUSE,               light1_dif);
        glLightfv( GL_LIGHT1, GL_SPECULAR,              light1_spe);
        glLightfv( GL_LIGHT1, GL_POSITION,              light1_pos);
        glLightf ( GL_LIGHT1, GL_CONSTANT_ATTENUATION,  LIGHT_C_ATT);
        glLightf ( GL_LIGHT1, GL_LINEAR_ATTENUATION,    LIGHT_B_ATT);
        glLightf ( GL_LIGHT1, GL_QUADRATIC_ATTENUATION, LIGHT_A_ATT);
        glLightf ( GL_LIGHT1, GL_SPOT_CUTOFF,           LIGHT_SPOT_ADEG);
        glLightfv( GL_LIGHT1, GL_SPOT_DIRECTION,        light1_dir);
        glLightf ( GL_LIGHT1, GL_SPOT_EXPONENT,         LIGHT_SPOT_EXP);

    }
    @Override
    protected void draw(){

        out_vx.draw();

        out_ax.draw();

        out_t.draw();

        out_rx.draw();

        out_tr.draw();

        out_lv.draw();

        out_m.draw();

        out_t10.draw();

        out_t01.draw();

        super.draw();
    }

    @Override
    protected boolean navigationInclude(int index, ViewPage3DComponent c){
        return (c instanceof DockingFieldIO);
    }
}
