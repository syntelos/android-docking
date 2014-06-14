/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public interface View3DScreenShot {


    public boolean screenshot(int format, int type, java.nio.ByteBuffer buffer)
        throws InterruptedException;

}
