/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingOutputT01
    extends ViewPage3DTextLabel
{
    protected final static String FMT = "T01 %s N";


    public DockingOutputT01(double x, double y, double z, double h){
        super(x,y,z,h);

        format(DockingGameLevel.Current);
    }


    protected final void format(DockingGameLevel level){
        format(FMT,Format4(level.force_thruster_1));
    }
}
