/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import java.nio.ByteOrder;

/**
 * 
 */
public abstract class View3DGeometry
    extends Epsilon
{
    /**
     * Bytes per float (32 bits) is four.
     */
    protected final static int bpf = 4;
    /**
     * Anything using nio buffers and 3-tuple floats will have stride
     * (3*bpf) because the nio buffers are employed as a partial or
     * incomplete pointer lacking position - offset.
     */
    protected final static int stride = (3*bpf);

    protected final static java.nio.ByteOrder nativeOrder = java.nio.ByteOrder.nativeOrder();

    public View3DGeometry(){
        super();
    }


    protected final static float[] Add(float[] a, float[] b){

        int alen = a.length;
        int blen = b.length;
        int len = (alen+blen);
        float[] copier = new float[len];
        System.arraycopy(a,0,copier,0,alen);
        System.arraycopy(b,0,copier,alen,blen);
        return copier;

    }
    protected final static float[] Copy(float[] tgt, int ofs, int count, float[] v){

        int t = ofs;

        final int z = (ofs+count);

        while (t < z){

            System.arraycopy(v,0,tgt,t,3);

            t += 3;
        }

        return tgt;
    }
    /**
     * Convert a list of triangles (N/3) into a list of lines (2N-2)
     * -- for N vertices.
     */
    protected final static float[] Lines(float[] triangles){
        final int tfc = triangles.length;
        final int tvc = (tfc/3);
        final int lvc = (tvc<<1)-2;
        final int lfc = (lvc*3);
        final int lft = (lfc-3);

        final float[] lines = new float[lfc];

        for (int ct = 0, cl = 0; ct < tfc; ct += 3, cl += 3){
            System.arraycopy(triangles,ct,lines,cl,3);
            if (0 != ct){
                cl += 3;
                if (cl < lft){
                    System.arraycopy(triangles,ct,lines,cl,3);
                }
            }
        }

        return lines;
    }

}
