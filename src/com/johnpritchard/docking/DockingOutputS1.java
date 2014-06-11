/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingOutputS1
    extends ViewPage3DTextLabel
{
    private final static String FMT = "Rx %s           T %s";


    public DockingOutputS1(double x, double y, double z, double h){
        super(x,y,z,h);

        format(0,0);
    }


    protected final void format(float r, float t){

        format(FMT,Format7(r),FormatT(t));
    }

}
