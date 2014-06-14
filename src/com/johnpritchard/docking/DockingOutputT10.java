/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingOutputT10
    extends ViewPage3DTextLabel
{
    protected final static String FMT = "T10 %s N";


    public DockingOutputT10(double x, double y, double z, double h){
        super(x,y,z,h);

        format(DockingGameLevel.Current);
    }


    protected final void format(DockingGameLevel level){
        format(FMT,Format4(level.force_thruster_0));
    }
}
