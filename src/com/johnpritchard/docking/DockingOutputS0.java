/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingOutputS0
    extends ViewPage3DTextLabel
{
    private final static String FMT = "Vx %s   Ax %s   T %s";


    public DockingOutputS0(double x, double y, double z, double h){
        super(x,y,z,h);

        format(0,0,0);
    }


    protected final void format(float v_x, float a_x, float t){

        format(FMT,Format7(v_x),Format7(a_x),FormatT(t));
    }

}
