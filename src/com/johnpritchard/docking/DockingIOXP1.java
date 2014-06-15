/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public final class DockingIOXP1
    extends DockingFieldIO
{


    public DockingIOXP1(double x, double y, double z, double h){
        super(PhysicsOperator.TX1, PhysicsDOF.XP, x, y, z, h);

        this.selection = DockingFieldIOGroup.SEL_XP1;

        format();
    }

}
