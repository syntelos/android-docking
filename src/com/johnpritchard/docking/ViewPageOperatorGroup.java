/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.RectF;

/**
 * Accumulation
 * 
 * @see ViewPageOperatorSelection
 */
public interface ViewPageOperatorGroup
    extends ViewPageOperatorSelection
{
    /**
     * Retrieve results (read only object: not a copy, do not modify)
     */
    public RectF group();
}
