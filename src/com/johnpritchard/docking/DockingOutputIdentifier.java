/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingOutputIdentifier
    extends ViewPage3DTextLabel
{

    public DockingOutputIdentifier(double x, double y, double z, double h){
        super(x,y,z,h);
    }


    protected final void format(String identifier){
        if (null != identifier){

            setText(identifier);
        }
        else {
            setText("");
        }
    }
}
