/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.llg;

import static android.opengl.GLES10.*;

import fv3.math.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Double buffered vertex array assures clean display of dynamic
 * content.
 */
public final class FontGlyphVector
    extends Geometry
{
    protected final static int bpf3 = (bpf * 3);


    private final double x, y, z, h;

    private double minX, minY;

    private double maxX, maxY;

    private float[] array;

    private FloatBuffer[] b_m = new FloatBuffer[2];

    private FloatBuffer[] b_gv = new FloatBuffer[2];

    private int[] b_count = {0,0};

    private int b_current = 0;


    public FontGlyphVector(double x, double y, double z, double h){
        super();
        this.x = x;
        this.y = y;
        this.z = z;
        this.h = h;
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
    }


    public void format(String fmt, Object... args){

        this.define(String.format(fmt,args));
    }
    public void define(String sstring){

        array = null;

        if (null != sstring){
            final char[] string = sstring.toCharArray();
            final int string_len = string.length;
            if (0 < string_len){
                /*
                 * String geometry constants
                 */
                final float em = this.em();
                final float leading = this.leading();
                final float fw = (em+leading);
                /*
                 * String geometry bounds
                 */
                minX = Double.MAX_VALUE;
                minY = Double.MAX_VALUE;

                maxX = Double.MIN_VALUE;
                maxY = Double.MIN_VALUE;

                int cc;
                char ch;
                float xp = 0f;

                for (cc = 0; cc < string_len; cc++){

                    ch = string[cc];

                    float[] glyph = this.glyph(ch);

                    if (null == glyph || 9 == glyph.length){
                        /*
                         * translate string geometry for space character
                         */
                        double x = xp;

                        xp += fw;

                        minX = Math.min(minX,x);
                        maxX = Math.max(maxX,xp);
                    }
                    else {
                        int glen = glyph.length-9;
                        int olen = (null != array)?(array.length):(0);
                        int nlen = (olen+glen);
                        /*
                         * Glyph copy
                         */
                        append(glyph,9,glen);

                        /*
                         * Glyph translate and bounds
                         */
                        for (int xo = olen; xo < nlen; xo++){

                            if (0f != xp){

                                array[xo] += xp;
                            }

                            double x = array[xo++];
                            double y = array[xo++];

                            minX = Math.min(minX,x);
                            maxX = Math.max(maxX,x);

                            minY = Math.min(minY,y);
                            maxY = Math.max(maxY,y);
                        }

                        /*
                         * Increment string geometry for subsequent
                         * glyph translation
                         */
                        xp += fw;
                    }
                }

                fit();

                pack();
            }
        }
    }
    public void draw(){

        final int current = this.b_current;

        final FloatBuffer gv = this.b_gv[current];

        final FloatBuffer gm = this.b_m[current];

        final int gv_count = this.b_count[current];

        if (null != gv){

            glPushMatrix();

            glMultMatrixf(gm);

            glEnableClientState(GL_VERTEX_ARRAY);

            glVertexPointer(3,GL_FLOAT,stride,gv);

            glDrawArrays(GL_LINES,0,gv_count);

            glPopMatrix();
        }
    }
    public float em(){

        return FontFutural.Em;
    }
    public float ascent(){

        return FontFutural.Ascent;
    }
    public float descent(){

        return FontFutural.Descent;
    }
    public float leading(){

        return FontFutural.Leading;
    }
    public float[] glyph(char ch){
        int idx = (int)(ch-' ');

        if (-1 < idx && idx < FontFutural.GlyphSet.length){

            return FontFutural.GlyphSet[idx];
        }
        else
            return null;
    }
    protected void append(float[] vertices, int ofs, int len){
        if (null == array){

            if (0 == ofs && len == vertices.length){

                this.array = vertices;
            }
            else {
                float[] copier = new float[len];
                System.arraycopy(vertices,ofs,copier,0,len);
                this.array = copier;
            }
        }
        else {
            int array_len = array.length;
            float[] copier = new float[array_len + len];
            System.arraycopy(array,0,copier,0,array_len);
            System.arraycopy(vertices,ofs,copier,array_len,len);
            this.array = copier;
        }
    }
    /**
     * Define "next" matrix.  The {@link #pack pack} method modifies
     * "current/next".
     */
    protected void fit(){

        final int next = (0 == b_current)?(1):(0);

        float[] array = this.array;
        if (null != array){
            final int count = array.length;
            if (0 != count){
                /*
                 * Fit vertices to this geometry
                 */
                final double s = (h / (maxY-minY));

                final double tx = ((x/s) - minX);
                final double ty = ((y/s) - minY);
                final double tz = (z/s);

                double[] m = Matrix.IVIdentity();

                Matrix.Scale(m,s);

                Matrix.Translate(m,tx,ty,tz);

                float[] fm = Matrix.Copy(m);
                {
                    FloatBuffer b_m = this.b_m[next];

                    b_m.put(fm);
                    b_m.position(0);
                }
                this.b_count[next] = (count/3);
            }
            else {
                this.b_count[next] = 0;
            }
        }
        else {
            this.b_count[next] = 0;
        }
    }
    /**
     * In order to minimize reallocation, the pack method employs all
     * available information to allocate each native buffer.
     * Reallocation is employed to seek the maximal buffer length.
     * Double buffering depends on nio's garbage collection strategy
     * for reallocation.
     * 
     * @see #pack()
     */
    protected int cap(int b_len){

        FloatBuffer gv0 = b_gv[0];
        FloatBuffer gv1 = b_gv[1];
        if (null != gv0){

            if (null != gv1){

                return Math.max(gv0.capacity(),Math.max(gv1.capacity(),b_len));
            }
            else {
                return Math.max(gv0.capacity(),b_len);
            }
        }
        else if (null != gv1){

            return Math.max(gv1.capacity(),b_len);
        }
        else
            return b_len;
    }
    /**
     * Define "next" vertex buffer and update "current".  The {@link
     * #fit fit} method must be called before the {@link #pack pack}
     * method.
     */
    protected void pack(){

        final int next = (0 == b_current)?(1):(0);

        float[] array = this.array;
        if (null != array){
            final int len = array.length;
            if (0 != len){

                final int cap = cap(len * bpf);

                FloatBuffer gv = b_gv[next];

                if (null == gv || cap > gv.capacity()){

                    final ByteBuffer ib = ByteBuffer.allocateDirect(cap);

                    ib.order(nativeOrder);

                    gv = ib.asFloatBuffer();

                    b_gv[next] = gv;
                }

                gv.clear();
                gv.put(array);
                gv.position(0);

                this.b_current = next;
            }
        }
    }
}
