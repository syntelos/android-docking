/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingOutputM
    extends ViewPage3DTextLabel
{
    protected final static String FMT = "M %s KG";


    public DockingOutputM(double x, double y, double z, double h){
        super(x,y,z,h);

        format(DockingGameLevel.Current);
    }


    protected final void format(DockingGameLevel level){
        format(FMT,Format5(level.mass_static));
    }
}
