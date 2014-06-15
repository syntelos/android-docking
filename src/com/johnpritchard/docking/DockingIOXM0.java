/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public final class DockingIOXM0
    extends DockingFieldIO
{


    public DockingIOXM0(double x, double y, double z, double h){
        super(PhysicsOperator.TX0, PhysicsDOF.XM, x, y, z, h);

        this.selection = DockingFieldIOGroup.SEL_XM0;

        format();
    }

}
