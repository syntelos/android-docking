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

/**
 * Largely derived from Freeglut geometry by Pawel W. Olszta.
 */
public abstract class GeometryCircle
    extends Geometry
{

    protected final static int CircleTableSize(int n){
        return (n+1);
    }
    protected final static void CircleTable(double[] sint, double[] cost, final int n){

        /* Table size, the sign of n flips the circle direction
         */
        final int size = Math.abs(n);

        /* Determine the angle between samples
         */
        final double angle = 2.0*Math.PI/((double)( ( n == 0 ) ? 1 : n ));

        /*
         * Compute cos and sin around the circle
         */
        sint[0] = 0.0;
        cost[0] = 1.0;

        for (int cc = 1; cc < size; cc++){

            sint[cc] = Math.sin(angle*cc);

            cost[cc] = Math.cos(angle*cc);
        }
        /*
         * Last sample is duplicate of the first
         */
        sint[size] = sint[0];
        cost[size] = cost[0];
    }


    public GeometryCircle(){
        super();
    }

}
