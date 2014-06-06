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
public final class GeometryAttitude
    extends GeometryCircle
{

    private final static double SphereRadius = 1.0;
    private final static int SphereSlices = 32;

    public final static GeometryAttitude Instance = new GeometryAttitude(SphereRadius,SphereSlices);

    /**
     * Line loop
     */
    public final float[] circle_xy;
    public final int circle_xy_count;
    /**
     * Line loop
     */
    public final float[] circle_yz;
    public final int circle_yz_count;
    /**
     * Line list
     */
    public final float[] axes;
    public final int axes_count;
    /**
     * Triangles
     */
    public final float[] triangleX;
    public final int triangleX_count;
    /**
     * Triangles
     */
    public final float[] triangleY;
    public final int triangleY_count;
    /**
     * Triangles
     */
    public final float[] triangleZ;
    public final int triangleZ_count;
    /**
     * Line list
     */
    public final float[] glyphX;
    public final int glyphX_count;
    /**
     * Line list
     */
    public final float[] glyphY;
    public final int glyphY_count;
    /**
     * Line list
     */
    public final float[] glyphZ;
    public final int glyphZ_count;

    private final FloatBuffer b_circle_xy;

    private final FloatBuffer b_circle_yz;

    private final FloatBuffer b_axes;

    private final FloatBuffer b_triangleX;

    private final FloatBuffer b_triangleY;

    private final FloatBuffer b_triangleZ;

    private final FloatBuffer b_glyphX;

    private final FloatBuffer b_glyphY;

    private final FloatBuffer b_glyphZ;


    private GeometryAttitude(final double radius, final int slices){
        super();

        final int table = CircleTableSize(slices);

        double[] tab_sin = new double[table];
        double[] tab_cos = new double[table];

        CircleTable(tab_sin,tab_cos,(-slices));

        float[] circle_xy = new float[slices*3];
        {
            for (int i = 0, j = 0; i < slices; i++){
                circle_xy[j++] = (float)(tab_cos[i]*radius);
                circle_xy[j++] = (float)(tab_sin[i]*radius);
                circle_xy[j++] = 0.0f;
            }
        }
        this.circle_xy = circle_xy;
        this.circle_xy_count = slices;

        float[] circle_yz = new float[slices*3];
        {
            for (int i = 0, j = 0; i < slices; i++){
                circle_yz[j++] = 0.0f;
                circle_yz[j++] = (float)(tab_cos[i]*radius);
                circle_yz[j++] = (float)(tab_sin[i]*radius);
            }
        }
        this.circle_yz = circle_yz;
        this.circle_yz_count = slices;

        float[] axes = new float[6*3];
        {
            axes[ 0] = -1.0f;
            axes[ 1] = 0.0f;
            axes[ 2] = 0.0f;

            axes[ 3] = +1.0f;
            axes[ 4] = 0.0f;
            axes[ 5] = 0.0f;

            axes[ 6] = 0.0f;
            axes[ 7] = -1.0f;
            axes[ 8] = 0.0f;

            axes[ 9] = 0.0f;
            axes[10] = +1.0f;
            axes[11] = 0.0f;

            axes[12] = 0.0f;
            axes[13] = 0.0f;
            axes[14] = -1.0f;

            axes[15] = 0.0f;
            axes[16] = 0.0f;
            axes[17] = +1.0f;
        }
        this.axes = axes;
        this.axes_count = 6;

        float[] triangleX = new float[3*3];
        {
            triangleX[ 0] = 1.0f;
            triangleX[ 1] = 0.0f;
            triangleX[ 2] = 0.0f;

            triangleX[ 3] = 0.8f;
            triangleX[ 4] = -0.2f;
            triangleX[ 5] = 0.0f;

            triangleX[ 6] = 0.8f;
            triangleX[ 7] = 0.2f;
            triangleX[ 8] = 0.0f;
        }
        this.triangleX = triangleX;
        this.triangleX_count = 3;

        float[] triangleY = new float[3*3];
        {
            triangleY[ 0] = 0.0f;
            triangleY[ 1] = 1.0f;
            triangleY[ 2] = 0.0f;

            triangleY[ 3] = -0.2f;
            triangleY[ 4] = 0.8f;
            triangleY[ 5] = 0.0f;

            triangleY[ 6] = 0.2f;
            triangleY[ 7] = 0.8f;
            triangleY[ 8] = 0.0f;
        }
        this.triangleY = triangleY;
        this.triangleY_count = 3;

        float[] triangleZ = new float[3*3];
        {
            triangleZ[ 0] = 0.0f;
            triangleZ[ 1] = 0.0f;
            triangleZ[ 2] = 1.0f;

            triangleZ[ 3] = 0.0f;
            triangleZ[ 4] = -0.2f;
            triangleZ[ 5] = 0.8f;

            triangleZ[ 6] = 0.0f;
            triangleZ[ 7] = 0.2f;
            triangleZ[ 8] = 0.8f;
        }
        this.triangleZ = triangleZ;
        this.triangleZ_count = 3;

        float[] glyphX = new float[12];
        {
            glyphX[ 0] = -0.1250000f;
            glyphX[ 1] =  1.4000000f;
            glyphX[ 2] =  0.0000000f;
            glyphX[ 3] =  0.1250000f;
            glyphX[ 4] =  1.0250000f;
            glyphX[ 5] =  0.0000000f;
            glyphX[ 6] =  0.1250000f;
            glyphX[ 7] =  1.4000000f;
            glyphX[ 8] =  0.0000000f;
            glyphX[ 9] = -0.1250000f;
            glyphX[ 10] =  1.0250000f;
            glyphX[ 11] =  0.0000000f;
        }
        this.glyphX = glyphX;
        this.glyphX_count = 4;

        float[] glyphY = new float[18];
        {
            glyphY[ 0] =  1.4125000f;
            glyphY[ 1] =  0.1500000f;
            glyphY[ 2] =  0.0000000f;
            glyphY[ 3] =  1.2339286f;
            glyphY[ 4] =  0.0071429f;
            glyphY[ 5] =  0.0000000f;
            glyphY[ 6] =  1.2339286f;
            glyphY[ 7] =  0.0071429f;
            glyphY[ 8] =  0.0000000f;
            glyphY[ 9] =  1.0375000f;
            glyphY[ 10] =  0.0071429f;
            glyphY[ 11] =  0.0000000f;
            glyphY[ 12] =  1.4125000f;
            glyphY[ 13] = -0.1357143f;
            glyphY[ 14] =  0.0000000f;
            glyphY[ 15] =  1.2339286f;
            glyphY[ 16] =  0.0071429f;
            glyphY[ 17] =  0.0000000f;
        }
        this.glyphY = glyphY;
        this.glyphY_count = 6;

        float[] glyphZ = new float[18];
        {
            glyphZ[ 0] =  0.0000000f;
            glyphZ[ 1] =  0.1250000f;
            glyphZ[ 2] =  1.4125000f;
            glyphZ[ 3] =  0.0000000f;
            glyphZ[ 4] = -0.1250000f;
            glyphZ[ 5] =  1.0375000f;
            glyphZ[ 6] =  0.0000000f;
            glyphZ[ 7] = -0.1250000f;
            glyphZ[ 8] =  1.4125000f;
            glyphZ[ 9] =  0.0000000f;
            glyphZ[ 10] =  0.1250000f;
            glyphZ[ 11] =  1.4125000f;
            glyphZ[ 12] =  0.0000000f;
            glyphZ[ 13] = -0.1250000f;
            glyphZ[ 14] =  1.0375000f;
            glyphZ[ 15] =  0.0000000f;
            glyphZ[ 16] =  0.1250000f;
            glyphZ[ 17] =  1.0375000f;
        }
        this.glyphZ = glyphZ;
        this.glyphZ_count = 6;

        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.circle_xy.length * bpf);
            ib.order(nativeOrder);
            this.b_circle_xy = ib.asFloatBuffer();
            this.b_circle_xy.put(this.circle_xy);
            this.b_circle_xy.position(0);
        }
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.circle_yz.length * bpf);
            ib.order(nativeOrder);
            this.b_circle_yz = ib.asFloatBuffer();
            this.b_circle_yz.put(this.circle_yz);
            this.b_circle_yz.position(0);
        }
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.axes.length * bpf);
            ib.order(nativeOrder);
            this.b_axes = ib.asFloatBuffer();
            this.b_axes.put(this.axes);
            this.b_axes.position(0);
        }
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.triangleX.length * bpf);
            ib.order(nativeOrder);
            this.b_triangleX = ib.asFloatBuffer();
            this.b_triangleX.put(this.triangleX);
            this.b_triangleX.position(0);
        }
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.triangleY.length * bpf);
            ib.order(nativeOrder);
            this.b_triangleY = ib.asFloatBuffer();
            this.b_triangleY.put(this.triangleY);
            this.b_triangleY.position(0);
        }
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.triangleZ.length * bpf);
            ib.order(nativeOrder);
            this.b_triangleZ = ib.asFloatBuffer();
            this.b_triangleZ.put(this.triangleZ);
            this.b_triangleZ.position(0);
        }
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.glyphX.length * bpf);
            ib.order(nativeOrder);
            this.b_glyphX = ib.asFloatBuffer();
            this.b_glyphX.put(this.glyphX);
            this.b_glyphX.position(0);
        }
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.glyphY.length * bpf);
            ib.order(nativeOrder);
            this.b_glyphY = ib.asFloatBuffer();
            this.b_glyphY.put(this.glyphY);
            this.b_glyphY.position(0);
        }
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.glyphZ.length * bpf);
            ib.order(nativeOrder);
            this.b_glyphZ = ib.asFloatBuffer();
            this.b_glyphZ.put(this.glyphZ);
            this.b_glyphZ.position(0);
        }
    }


    public void draw(){

        glEnableClientState(GL_VERTEX_ARRAY);

        glVertexPointer(3,GL_FLOAT,stride,this.b_circle_xy);
        glDrawArrays(GL_LINE_LOOP,0,this.circle_xy_count);

        glVertexPointer(3,GL_FLOAT,stride,this.b_circle_yz);
        glDrawArrays(GL_LINE_LOOP,0,this.circle_yz_count);

        glVertexPointer(3,GL_FLOAT,stride,this.b_axes);
        glDrawArrays(GL_LINES,0,this.axes_count);

        glVertexPointer(3,GL_FLOAT,stride,this.b_triangleX);
        glDrawArrays(GL_TRIANGLES,0,this.triangleX_count);

        glVertexPointer(3,GL_FLOAT,stride,this.b_triangleY);
        glDrawArrays(GL_TRIANGLES,0,this.triangleY_count);

        glVertexPointer(3,GL_FLOAT,stride,this.b_triangleZ);
        glDrawArrays(GL_TRIANGLES,0,this.triangleZ_count);

        glVertexPointer(3,GL_FLOAT,stride,this.b_glyphX);
        glDrawArrays(GL_LINES,0,this.glyphX_count);

        glVertexPointer(3,GL_FLOAT,stride,this.b_glyphY);
        glDrawArrays(GL_LINES,0,this.glyphY_count);

        glVertexPointer(3,GL_FLOAT,stride,this.b_glyphZ);
        glDrawArrays(GL_LINES,0,this.glyphZ_count);

    }
}
