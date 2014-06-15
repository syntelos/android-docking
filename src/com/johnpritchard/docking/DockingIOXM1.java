/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public final class DockingIOXM1
    extends DockingFieldIO
{


    public DockingIOXM1(double x, double y, double z, double h){
        super(PhysicsOperator.TX1, PhysicsDOF.XM, x, y, z, h);

        this.selection = DockingFieldIOGroup.SEL_XM1;

        format();
    }

}
