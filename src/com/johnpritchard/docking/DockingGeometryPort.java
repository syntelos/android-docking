/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import static android.opengl.GLES10.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 
 */
public final class DockingGeometryPort
    extends View3DGeometryCircle
{

    private final static double Radius = 1.0;
    private final static int Slices = 32;

    public final static DockingGeometryPort Instance = new DockingGeometryPort(Radius,Slices);

    /**
     * Disk: triangle fan
     */
    private final float[] disk_A_n;
    private final float[] disk_A_v;
    private final int disk_A_count;

    private final FloatBuffer b_disk_A_n, b_disk_A_v;


    private DockingGeometryPort(final double radius, final int slices){
        this(radius,slices,slices);
    }
    private DockingGeometryPort(final double radius, final int slices, final int stacks){
        super();

        final double r_A = radius;
        final double r_B = (radius/2.0);

        int i,j,cc,count;


        /* Pre-computed circle
         * Allocate memory for n samples, plus duplicate of first entry at the end
         */
        final int table_1 = CircleTableSize(slices);
        final int table_2 = CircleTableSize(stacks<<1);

        double[] sin_t1 = new double[table_1];
        double[] cos_t1 = new double[table_1];
        double[] sin_t2 = new double[table_2];
        double[] cos_t2 = new double[table_2];

        CircleTable(sin_t1,cos_t1,(-slices));
        CircleTable(sin_t2,cos_t2,(stacks<<1));

        float z = 0.0f;

        /* Disk A
         */

        /*
         * Normals
         */
        cc = 0;
        count = 3*(slices+2);
        float[] disk_A_n = new float[count];
        float[] disk_A_v = new float[count];

        disk_A_n[cc++] = 0.0f;
        disk_A_n[cc++] = 0.0f;
        disk_A_n[cc++] = 1.0f;

        for (j=slices; j>=0; j--){

            disk_A_n[cc++] = 0.0f;
            disk_A_n[cc++] = 0.0f;
            disk_A_n[cc++] = 1.0f;
        }

        /*
         * Vertices
         */

        cc = 0;
        disk_A_v[cc++] = 0.0f;
        disk_A_v[cc++] = 0.0f;
        disk_A_v[cc++] = 0.0f;

        for (j=slices; j>=0; j--){

            disk_A_v[cc++] = (float)(cos_t1[j]*r_A);
            disk_A_v[cc++] = (float)(sin_t1[j]*r_A);
            disk_A_v[cc++] = z;
        }


        this.disk_A_n = disk_A_n;
        this.disk_A_v = disk_A_v;
        this.disk_A_count = disk_A_v.length/3;

        {
            final ByteBuffer ib_disk_A = ByteBuffer.allocateDirect(this.disk_A_n.length * bpf);
            ib_disk_A.order(nativeOrder);
            this.b_disk_A_n = ib_disk_A.asFloatBuffer();
            this.b_disk_A_n.put(this.disk_A_n);
            this.b_disk_A_n.position(0);
        }
        {
            final ByteBuffer ib_disk_A = ByteBuffer.allocateDirect(this.disk_A_v.length * bpf);
            ib_disk_A.order(nativeOrder);
            this.b_disk_A_v = ib_disk_A.asFloatBuffer();
            this.b_disk_A_v.put(this.disk_A_v);
            this.b_disk_A_v.position(0);
        }

    }

    public void draw(){

        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_VERTEX_ARRAY);

        glNormalPointer(GL_FLOAT,stride,this.b_disk_A_n);
        glVertexPointer(3,GL_FLOAT,stride,this.b_disk_A_v);
        glDrawArrays(GL_TRIANGLE_FAN,0,this.disk_A_count);

    }
}
