/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import java.nio.ByteOrder;

/**
 * 
 */
public abstract class View3DGeometry {
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
