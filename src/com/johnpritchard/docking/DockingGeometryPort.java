/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import static android.opengl.GLES10.*;

import fv3.math.Vector;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 
 */
public final class DockingGeometryPort
    extends View3DGeometryCircle
{

    private final static int Res = 32;

    private final static float[] VN_X1 = {0f,0f,1f};


    public final static DockingGeometryPort Instance = new DockingGeometryPort();

    protected static void Init(){
    }


    /**
     */
    private final int ts_count;

    private final FloatBuffer b_ts_n, b_ts_v;


    private DockingGeometryPort(){
        super();

        /*
         */
        final int table_1 = CircleTableSize(Res);

        double[] sin_t1 = new double[table_1];
        double[] cos_t1 = new double[table_1];

        CircleTable(sin_t1,cos_t1,-(Res));

        /*
         * Port center
         */
        float z = -0.040f;
        float r = +0.940f;

        int cc = 0;
        int count = (9*Res);
        float[] disk_A_v = new float[count];

        for (int a = 0; a < Res; a++){

            disk_A_v[cc++] = 0.0f;
            disk_A_v[cc++] = 0.0f;
            disk_A_v[cc++] = 0.0f;

            disk_A_v[cc++] = (float)(cos_t1[a]*r);
            disk_A_v[cc++] = (float)(sin_t1[a]*r);
            disk_A_v[cc++] = z;

            disk_A_v[cc++] = (float)(cos_t1[a+1]*r);
            disk_A_v[cc++] = (float)(sin_t1[a+1]*r);
            disk_A_v[cc++] = z;
        }


        this.ts_count = count/3;

        float[] ts_n = Copy(new float[count],0,count,VN_X1);
        float[] ts_v = disk_A_v;

        {
            final ByteBuffer ib_ts = ByteBuffer.allocateDirect(ts_n.length * bpf);
            ib_ts.order(nativeOrder);
            this.b_ts_n = ib_ts.asFloatBuffer();
            this.b_ts_n.put(ts_n);
            this.b_ts_n.position(0);
        }
        {
            final ByteBuffer ib_ts = ByteBuffer.allocateDirect(ts_v.length * bpf);
            ib_ts.order(nativeOrder);
            this.b_ts_v = ib_ts.asFloatBuffer();
            this.b_ts_v.put(ts_v);
            this.b_ts_v.position(0);
        }

    }

    public void draw(){

        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_VERTEX_ARRAY);

        glNormalPointer(GL_FLOAT,stride,this.b_ts_n);
        glVertexPointer(3,GL_FLOAT,stride,this.b_ts_v);
        glDrawArrays(GL_TRIANGLE_STRIP,0,this.ts_count);

    }
}
