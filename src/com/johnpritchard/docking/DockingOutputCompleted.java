/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import java.util.Date;

/**
 * 
 */
public class DockingOutputCompleted
    extends ViewPage3DTextLabel
{


    public DockingOutputCompleted(double x, double y, double z, double h){
        super(x,y,z,h);
    }


    protected final void format(Date completed){
        if (null != completed){

            setText(completed.toString());
        }
        else {
            setText("");
        }
    }
}
