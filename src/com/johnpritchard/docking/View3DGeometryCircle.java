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

import android.util.Log;

/**
 * Largely derived from Freeglut geometry by Pawel W. Olszta.
 */
public abstract class View3DGeometryCircle
    extends View3DGeometry
{
    protected final static double PI_M2 = (Math.PI * 2.0);


    protected final static int CircleTableSize(int n){
        return (n+1);
    }
    protected final static void CircleTable(double[] sin_t, double[] cos_t, final int n){

        final int size = Math.abs(n);

        final double angle = PI_M2/(double)n;

        cos_t[0] = 1.0;
        sin_t[0] = 0.0;

        for (int cc = 1; cc < size; cc++){

            cos_t[cc] = Math.cos(angle*cc);
            sin_t[cc] = Math.sin(angle*cc);
        }

        sin_t[size] = sin_t[0];
        cos_t[size] = cos_t[0];
    }


    public View3DGeometryCircle(){
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

        int c = ofs;

        final int z = (ofs+count);

        while (c < z){

            System.arraycopy(v,0,tgt,c,3);

            c += 3;
        }

        return tgt;
    }
}
