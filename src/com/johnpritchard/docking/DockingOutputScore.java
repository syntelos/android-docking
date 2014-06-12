/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingOutputScore
    extends ViewPage3DTextLabel
{
    private final static String FMT = "Score %1.1f/4.0";


    public DockingOutputScore(double x, double y, double z, double h){
        super(x,y,z,h);

        format(0);
    }


    protected final void format(float score){
        format(FMT,score);
    }
}
