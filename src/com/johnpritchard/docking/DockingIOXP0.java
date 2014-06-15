/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public final class DockingIOXP0
    extends DockingFieldIO
{


    public DockingIOXP0(double x, double y, double z, double h){
        super(PhysicsOperator.TX0, PhysicsDOF.XP, x, y, z, h);

        this.selection = DockingFieldIOGroup.SEL_XP0;

        format();
    }

}
