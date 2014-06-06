/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public interface InterfaceRotation {

    public interface Listener {

        public void update(InterfaceRotation r);
    }

    public float[] rotationMatrix();

    public double rotationX();
    public double rotationY();
    public double rotationZ();

    public double rotationEL();
    public double rotationAZ();
}
