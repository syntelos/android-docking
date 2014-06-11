/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingCraftModel {

    /**
     * kg
     */
    public final static double mass_static = 1000.0;
    /**
     * N = kg m/s/s
     */
    public final static double force_thruster = 10.0;

    public final static double acceleration_under_thrust = (force_thruster / mass_static);


    private DockingCraftModel(){
        super();
    }

}
