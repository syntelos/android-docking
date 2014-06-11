/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingOutputAx
    extends ViewPage3DTextLabel
{
    private final static String FMT = "Ax %s";


    public DockingOutputAx(double x, double y, double z, double h){
        super(x,y,z,h);

        format(0);
    }


    protected final void format(float Ax){
        format(FMT,Format7(Ax));
    }
}
