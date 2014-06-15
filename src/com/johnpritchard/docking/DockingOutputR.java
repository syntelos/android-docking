/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * Model R(Y)
 */
public class DockingOutputR
    extends ViewPage3DTextLabel
{
    private final static String FMT = "R %s";


    public DockingOutputR(double x, double y, double z, double h){
        super(x,y,z,h);

        format(0);
    }


    protected final void format(float R){

        format(FMT,Format8(R));
    }
}
