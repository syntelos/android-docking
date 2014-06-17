/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingOutputHardwareVendor
    extends ViewPage3DTextLabel
{

    public DockingOutputHardwareVendor(double x, double y, double z, double h){
        super(x,y,z,h);
    }


    protected final void format(String vendor, String version, String model, String build){

        setText(vendor+' '+version+' '+model+' '+build);
    }
}
