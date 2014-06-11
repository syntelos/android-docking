/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.RectF;

import static android.opengl.GLES10.*;

import fv3.math.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * An instance of this class is able to maintain a dynamic bounding
 * box for a fixed substring of the 3D text label.
 * 
 * @see ViewPage3DTextLabel
 */
public class ViewPage3DTextSelection
    extends ViewPage3DComponentAbstract
{
    protected final static int N_LINES = 4;

    protected final static int N_VERTICES = 2*N_LINES;

    protected final static int N_FLOATS = 3*N_VERTICES;


    protected final int begin;

    protected final int end;

    protected final float z;

    private RectF[] outline = new RectF[]{
        new RectF(), new RectF()
    };

    private volatile int b_current;

    private FloatBuffer[] b_m = new FloatBuffer[2];

    private FloatBuffer[] b_gv = new FloatBuffer[2];


    public ViewPage3DTextSelection(int begin, double z){
        this(begin,Integer.MAX_VALUE,z);
    }
    public ViewPage3DTextSelection(int begin, int end, double z){
        super();
        if (-1 < begin && begin < end){
            this.begin = begin;
            this.end = end;
            {
                final ByteBuffer ib = ByteBuffer.allocateDirect(16 * bpf);
                ib.order(nativeOrder);
                this.b_m[0] = ib.asFloatBuffer();
                this.b_m[0].put(Matrix.Identity());
                this.b_m[0].position(0);
            }
            {
                final ByteBuffer ib = ByteBuffer.allocateDirect(16 * bpf);
                ib.order(nativeOrder);
                this.b_m[1] = ib.asFloatBuffer();
                this.b_m[1].put(Matrix.Identity());
                this.b_m[1].position(0);
            }
            this.z = (float)z;
        }
        else {
            throw new IllegalArgumentException();
        }
    }


    protected void open(){

        final int next = (0 == this.b_current)?(1):(0);

        final RectF outline = this.outline[next];

        outline.set(Float.MAX_VALUE,Float.MAX_VALUE,Float.MIN_VALUE,Float.MIN_VALUE);
    }
    protected void update(int index, double ix){

        final float x = (float)ix;

        if (begin <= index && index < end){

            final int next = (0 == this.b_current)?(1):(0);

            final RectF outline = this.outline[next];

            outline.left = Math.min(outline.left,x);
            outline.right = Math.max(outline.right,x);
        }
    }
    protected void update(int index, double ix, double iy){

        final float x = (float)ix;
        final float y = (float)iy;

        if (begin <= index && index < end){

            final int next = (0 == this.b_current)?(1):(0);

            final RectF outline = this.outline[next];

            outline.left = Math.min(outline.left,x);
            outline.right = Math.max(outline.right,x);

            outline.top = Math.min(outline.top,y);
            outline.bottom = Math.max(outline.bottom,y);
        }
    }
    protected void update(float[] m){

        final int next = (0 == b_current)?(1):(0);

        final FloatBuffer b_m = this.b_m[next];
        {
            b_m.put(m);
            b_m.position(0);
        }
    }
    protected void close(){

        final int next = (0 == this.b_current)?(1):(0);

        final RectF outline = this.outline[next];

        FloatBuffer gv = this.b_gv[next];

        final float pad = (Math.max((outline.right-outline.left),(outline.bottom-outline.top)) * 0.10f);
        {
            outline.left   -= pad;
            outline.top    -= pad;
            outline.right  += pad;
            outline.bottom += pad;
        }

        float[] lines = new float[N_FLOATS];
        {
            lines[ 0] = outline.left;
            lines[ 1] = outline.top;
            lines[ 2] = z;

            lines[ 3] = outline.left;
            lines[ 4] = outline.bottom;
            lines[ 5] = z;

            lines[ 6] = outline.left;
            lines[ 7] = outline.bottom;
            lines[ 8] = z;

            lines[ 9] = outline.right;
            lines[10] = outline.bottom;
            lines[11] = z;

            lines[12] = outline.right;
            lines[13] = outline.bottom;
            lines[14] = z;

            lines[15] = outline.right;
            lines[16] = outline.top;
            lines[17] = z;

            lines[18] = outline.right;
            lines[19] = outline.top;
            lines[20] = z;

            lines[21] = outline.left;
            lines[22] = outline.top;
            lines[23] = z;
        }

        final int cap = (N_FLOATS * bpf);

        if (null == gv || cap > gv.capacity()){

            final ByteBuffer ib = ByteBuffer.allocateDirect(cap);

            ib.order(nativeOrder);

            gv = ib.asFloatBuffer();

            b_gv[next] = gv;
        }

        gv.clear();
        gv.put(lines);
        gv.position(0);

        this.b_current = next;
    }
    public void draw(){

        final int current = this.b_current;

        final FloatBuffer gv = this.b_gv[current];

        final FloatBuffer gm = this.b_m[current];

        if (null != gv){

            glPushMatrix();

            glMultMatrixf(gm);

            glEnableClientState(GL_VERTEX_ARRAY);

            glVertexPointer(3,GL_FLOAT,stride,gv);

            glDrawArrays(GL_LINES,0,N_VERTICES);

            glPopMatrix();
        }
    }

}
