/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.RectF;

/**
 * A geometry listener which is applied to multiple members of a group
 * to produce the union of their bounding box.
 * 
 * In the static case the members of the group can continuously
 * integrate their union without needing the group open and close
 * method calls.  In the dynamic case, the group open method call is
 * required to reinitialize the accumulation box.
 * 
 * @see ViewPageOperatorSelection
 */
public interface ViewPageOperatorGroup
    extends ViewPageOperatorSelection
{
    /**
     * 
     */
    public void groupOpen();
    /**
     * 
     */
    public void groupClose();
    /**
     * Retrieve results (read only object: not a copy, do not modify)
     */
    public RectF group();
}
