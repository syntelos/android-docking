/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingOutputT
    extends ViewPage3DTextLabel
{
    private final static String FMT = "C %s";


    public DockingOutputT(double x, double y, double z, double h){
        super(x,y,z,h);

        format(0);
    }


    protected final void format(float T){
        format(FMT,FormatT(T));
    }
}
