/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingOutputRx
    extends ViewPage3DTextLabel
{
    private final static String FMT = "D %s";


    public DockingOutputRx(double x, double y, double z, double h){
        super(x,y,z,h);

        format(0);
    }


    protected final void format(float Rx){
        format(FMT,Format7(Rx));
    }
}
