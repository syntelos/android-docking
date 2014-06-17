/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingOutputHardwareExtensions
    extends ViewPage3DTextLabel
{


    public DockingOutputHardwareExtensions(double x, double y, double z, double h){
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

    protected final static DockingOutputHardwareExtensions[] Add(DockingOutputHardwareExtensions[] list, 
                                                                 DockingOutputHardwareExtensions item)
    {
        if (null == item)
            return list;
        else if (null == list)
            return new DockingOutputHardwareExtensions[]{item};
        else {
            int len = list.length;
            DockingOutputHardwareExtensions[] copier = 
                new DockingOutputHardwareExtensions[len+1];
            System.arraycopy(list,0,copier,0,len);
            copier[len] = item;
            return copier;
        }
    }
}
