/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * @see DockingCraftStateVector
 */
public final class DockingCraftModel
    extends Epsilon
{

    /**
     * kg
     */
    public final static double mass_static = 1000.0;
    /**
     * N = kg m/s/s
     */
    public final static double force_thruster_0 = 100.0;

    public final static double force_thruster_1 = 10.0;
    /**
     * m/s/s
     */
    public final static double accel_thruster_0 = (force_thruster_0 / mass_static);

    public final static double accel_thruster_1 = (force_thruster_1 / mass_static);


    private DockingCraftModel(){
        super();
    }

}
