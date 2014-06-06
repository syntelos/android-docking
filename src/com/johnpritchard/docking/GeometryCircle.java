/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.llg;

/**
 * 
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
