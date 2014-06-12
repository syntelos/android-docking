/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import java.util.Date;

/**
 * 
 */
public class DockingOutputCreated
    extends ViewPage3DTextLabel
{


    public DockingOutputCreated(double x, double y, double z, double h){
        super(x,y,z,h);
    }


    protected final void format(Date created){
        if (null != created){

            setText(created.toString());
        }
        else {
            setText("");
        }
    }
}
