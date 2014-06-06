/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.llg;

/**
 * 
 */
public interface InterfacePerspective {

    public interface Listener {

        public void update(InterfacePerspective p);
    }

    /**
     * PX
     */
    public float perspectiveWidth();
    public float perspectiveHeight();
    public float perspectiveAspect();
    /**
     * DEG
     */
    public float perspectiveFOV();
    /**
     * M
     */
    public float perspectiveNear();
    public float perspectiveFar();
    /**
     * PX
     */
    public float perspectiveTop();
    public float perspectiveBottom();
    public float perspectiveLeft();
    public float perspectiveRight();
    /**
     */
    public float[] perspectiveMatrix();
}
