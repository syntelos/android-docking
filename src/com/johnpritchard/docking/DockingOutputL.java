/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingOutputL
    extends ViewPage3DTextLabel
{

    public DockingOutputL(double x, double y, double z, double h){
        super(x,y,z,h);

        format(DockingGameLevel.Current);
    }


    protected final void format(DockingGameLevel level){
        setText(level.name());
    }
}
