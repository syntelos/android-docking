/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.RectF;

/**
 * Geometric membership listener is applied with a protocol (syntax
 * and semantics) defined by the producer.
 * 
 * Each producer (of calls to this interface) has one or more logical
 * member elements, for example the glyphs in a glyph vector.  The
 * update methods are called as necessary to describe each logical
 * member as identified by a unique index.  
 *
 * Update calls may include all vertices in the geometric member
 * element, or may describe the boundary coordinates of each member
 * element.
 * 
 * @see ViewPageOperatorSelection
 */
public interface ViewPageOperatorSelection {

    /**
     * Start producer interaction
     */
    public void open(int count);
    /**
     * Producer member element (index) X dimension coordinate
     */
    public void update(int index, double maxX);
    /**
     * Producer member element (index) dimensions
     */
    public void update(int index, RectF bounds);
    /**
     * Producer member element (index) X,Y dimension coordinates
     */
    public void update(int index, double maxX, double maxY);
    /**
     * Producer member element (index) X,Y dimension coordinates
     */
    public void update(int index, double minX, double minY, double maxX, double maxY);
    /**
     * Producer's transformation matrix
     */
    public void update(float[] m);
    /**
     * Finish producer interaction
     */
    public void close();

}
