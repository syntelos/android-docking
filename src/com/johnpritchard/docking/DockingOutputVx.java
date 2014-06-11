/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingOutputVx
    extends ViewPage3DTextLabel
{
    private final static String FMT = "Vx %s";


    public DockingOutputVx(double x, double y, double z, double h){
        super(x,y,z,h);

        format(0);
    }


    protected final void format(float Vx){
        format(FMT,Format7(Vx));
    }
}
