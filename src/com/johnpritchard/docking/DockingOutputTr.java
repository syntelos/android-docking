/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingOutputTr
    extends ViewPage3DTextLabel
{
    private final static String FMT = "R %s";


    public DockingOutputTr(double x, double y, double z, double h){
        super(x,y,z,h);

        format(0);
    }


    protected final void format(float Tr){
        format(FMT,FormatT(Tr));
    }
}
