/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.RectF;

/**
 * 
 */
public interface ViewPage2DComponentGroup
    extends ViewPage2DComponent
{
    /**
     * Group dimension and derived margin
     */
    public void group(RectF dim, float pad);
}
