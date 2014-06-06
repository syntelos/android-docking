/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.llg;

import java.nio.ByteOrder;

/**
 * 
 */
public abstract class Geometry {
    /**
     * Bytes per float (32 bits) is four.
     */
    protected final static int bpf = 4;
    /**
     * Anything using nio buffers and 3-tuple floats will have stride
     * (3*bpf) because the nio buffers are employed as a partial or
     * incomplete pointer lacking position - offset.
     */
    protected final static int stride = (3*bpf);

    protected final static java.nio.ByteOrder nativeOrder = java.nio.ByteOrder.nativeOrder();

    public Geometry(){
        super();
    }

    public abstract void draw();

}
