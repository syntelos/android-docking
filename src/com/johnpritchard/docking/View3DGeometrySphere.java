/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
/*
 * freeglut_geometry.c
 *
 * Freeglut geometry rendering methods.
 *
 * Copyright (c) 1999-2000 Pawel W. Olszta. All Rights Reserved.
 * Written by Pawel W. Olszta, <olszta@sourceforge.net>
 * Creation date: Fri Dec 3 1999
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL
 * PAWEL W. OLSZTA BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.johnpritchard.docking;

import static android.opengl.GLES10.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Largely derived from Freeglut geometry by Pawel W. Olszta.
 */
public final class View3DGeometrySphere
    extends View3DGeometryCircle
{

    private final static double SphereRadius = 1.0;
    private final static int SphereSlices = 32;

    public final static View3DGeometrySphere Instance = new View3DGeometrySphere(SphereRadius,SphereSlices);

    /**
     * Triangle fan
     */
    public final float[] top_n;
    public final float[] top_v;
    public final int top_count;
    /**
     * Quad strip (triangle fan)
     */
    public final float[] middle_n;
    public final float[] middle_v;
    public final int middle_count;
    /**
     * Triangle fan
     */
    public final float[] bottom_n;
    public final float[] bottom_v;
    public final int bottom_count;

    private final FloatBuffer b_top_n, b_top_v, b_middle_n, b_middle_v, b_bottom_n, b_bottom_v;


    private View3DGeometrySphere(final double radius, final int slices){
        this(radius,slices,slices);
    }
    private View3DGeometrySphere(final double radius, final int slices, final int stacks){
        super();

        int i,j,cc,count;

        /* Adjust z and radius as stacks are drawn.
         */
        double z0,z1;
        double r0,r1;

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

        /* The top stack is covered with a triangle fan
         */

        z0 = 1.0;
        z1 = cos_t2[(stacks>0)?1:0];
        r0 = 0.0;
        r1 = sin_t2[(stacks>0)?1:0];

        /*
         * Normals
         */
        cc = 0;
        count = 3*(slices+2);
        float[] top_n = new float[count];
        float[] top_v = new float[count];

        top_n[cc++] = 0.0f;
        top_n[cc++] = 0.0f;
        top_n[cc++] = 1.0f;

        for (j=slices; j>=0; j--){

            top_n[cc++] = (float)(cos_t1[j]*r1);
            top_n[cc++] = (float)(sin_t1[j]*r1);
            top_n[cc++] = (float)(z1);
        }
        /*
         * Vertices
         */
        cc = 0;
        top_v[cc++] = 0.0f;
        top_v[cc++] = 0.0f;
        top_v[cc++] = (float)radius;

        for (j=slices; j>=0; j--){

            top_v[cc++] = (float)(cos_t1[j]*r1*radius);
            top_v[cc++] = (float)(sin_t1[j]*r1*radius);
            top_v[cc++] = (float)(z1*radius);
        }

        /*
         * Normals
         */
        cc = 0;
        count = 3*(stacks-2)*((slices+1)*2);
        float[] middle_n = new float[count];
        float[] middle_v = new float[count];

        for( i=1; i<stacks-1; i++ ){
            z0 = z1; z1 = cos_t2[i+1];
            r0 = r1; r1 = sin_t2[i+1];

            for(j=0; j<=slices; j++){
                middle_n[cc++] = (float)(cos_t1[j]*r1);
                middle_n[cc++] = (float)(sin_t1[j]*r1);
                middle_n[cc++] = (float)(z1);

                middle_n[cc++] = (float)(cos_t1[j]*r0);
                middle_n[cc++] = (float)(sin_t1[j]*r0);
                middle_n[cc++] = (float)(z0);
            }
        }

        z0 = 1.0;
        z1 = cos_t2[(stacks>0)?1:0];
        r0 = 0.0;
        r1 = sin_t2[(stacks>0)?1:0];

        /*
         * Vertices
         */
        cc = 0;

        for( i=1; i<stacks-1; i++ ){
            z0 = z1; z1 = cos_t2[i+1];
            r0 = r1; r1 = sin_t2[i+1];

            for(j=0; j<=slices; j++){

                middle_v[cc++] = (float)(cos_t1[j]*r1*radius);
                middle_v[cc++] = (float)(sin_t1[j]*r1*radius);
                middle_v[cc++] = (float)(z1*radius);

                middle_v[cc++] = (float)(cos_t1[j]*r0*radius);
                middle_v[cc++] = (float)(sin_t1[j]*r0*radius);
                middle_v[cc++] = (float)(z0*radius);
            }
        }

        z0 = z1;
        r0 = r1;

        /*
         * Normals
         */
        cc = 0;
        count = 3*(slices+2);
        float[] bottom_n = new float[count];
        float[] bottom_v = new float[count];

        bottom_n[cc++] = 0.0f;
        bottom_n[cc++] = 0.0f;
        bottom_n[cc++] = -1.0f;

        for (j=0; j<=slices; j++){

            bottom_n[cc++] = (float)(cos_t1[j]*r0);
            bottom_n[cc++] = (float)(sin_t1[j]*r0);
            bottom_n[cc++] = (float)(z0);
        }

        /*
         * Vertices
         */
        cc = 0;

        bottom_v[cc++] = 0.0f;
        bottom_v[cc++] = 0.0f;
        bottom_v[cc++] = (float)(-radius);

        for (j=0; j<=slices; j++){

            bottom_v[cc++] = (float)(cos_t1[j]*r0*radius);
            bottom_v[cc++] = (float)(sin_t1[j]*r0*radius);
            bottom_v[cc++] = (float)(z0*radius);
        }


        this.top_n = top_n;
        this.top_v = top_v;
        this.top_count = top_v.length/3;

        this.middle_n = middle_n;
        this.middle_v = middle_v;
        this.middle_count = middle_v.length/3;

        this.bottom_n = bottom_n;
        this.bottom_v = bottom_v;
        this.bottom_count = bottom_v.length/3;
        {
            final ByteBuffer ib_top = ByteBuffer.allocateDirect(this.top_n.length * bpf);
            ib_top.order(nativeOrder);
            this.b_top_n = ib_top.asFloatBuffer();
            this.b_top_n.put(this.top_n);
            this.b_top_n.position(0);
        }
        {
            final ByteBuffer ib_top = ByteBuffer.allocateDirect(this.top_v.length * bpf);
            ib_top.order(nativeOrder);
            this.b_top_v = ib_top.asFloatBuffer();
            this.b_top_v.put(this.top_v);
            this.b_top_v.position(0);
        }
        {
            final ByteBuffer ib_middle = ByteBuffer.allocateDirect(this.middle_n.length * bpf);
            ib_middle.order(nativeOrder);
            this.b_middle_n = ib_middle.asFloatBuffer();
            this.b_middle_n.put(this.middle_n);
            this.b_middle_n.position(0);
        }
        {
            final ByteBuffer ib_middle = ByteBuffer.allocateDirect(this.middle_v.length * bpf);
            ib_middle.order(nativeOrder);
            this.b_middle_v = ib_middle.asFloatBuffer();
            this.b_middle_v.put(this.middle_v);
            this.b_middle_v.position(0);
        }
        {
            final ByteBuffer ib_bottom = ByteBuffer.allocateDirect(this.bottom_n.length * bpf);
            ib_bottom.order(nativeOrder);
            this.b_bottom_n = ib_bottom.asFloatBuffer();
            this.b_bottom_n.put(this.bottom_n);
            this.b_bottom_n.position(0);
        }
        {
            final ByteBuffer ib_bottom = ByteBuffer.allocateDirect(this.bottom_v.length * bpf);
            ib_bottom.order(nativeOrder);
            this.b_bottom_v = ib_bottom.asFloatBuffer();
            this.b_bottom_v.put(this.bottom_v);
            this.b_bottom_v.position(0);
        }
    }

    public void draw(){

        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_VERTEX_ARRAY);

        glNormalPointer(GL_FLOAT,stride,this.b_top_n);
        glVertexPointer(3,GL_FLOAT,stride,this.b_top_v);
        glDrawArrays(GL_TRIANGLE_FAN,0,this.top_count);

        glNormalPointer(GL_FLOAT,stride,this.b_middle_n);
        glVertexPointer(3,GL_FLOAT,stride,this.b_middle_v);
        glDrawArrays(GL_TRIANGLE_FAN,0,this.middle_count);

        glNormalPointer(GL_FLOAT,stride,this.b_bottom_n);
        glVertexPointer(3,GL_FLOAT,stride,this.b_bottom_v);
        glDrawArrays(GL_TRIANGLE_FAN,0,this.bottom_count);
    }
}
