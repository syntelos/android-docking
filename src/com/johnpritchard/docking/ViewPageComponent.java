/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;

/**
 * Page components are always visible and focusable and do not overlap
 * in X-Y space.  This component is comparable to the Android
 * Drawable, and differs primarily in its emphasis on a simplified
 * model of interaction.
 * 
 * @see ViewPage2DComponentPath
 */
public interface ViewPageComponent {
    /**
     * Instance object identity defaults to a derivation of the class
     * name
     */
    public String getName();
    /**
     * Define a more unique instance object identifier.
     */
    public void setName(String name);
    /**
     * Centroid location
     */
    public float getCX();
    /**
     * Centroid location
     */
    public float getCY();
    /**
     * Derived from bounds
     */
    public float getX();
    /**
     * Derived from bounds
     */
    public float getY();
    /**
     * Derived from bounds
     */
    public float getWidth();
    /**
     * Derived from bounds
     */
    public float getHeight();
    /**
     */
    public RectF bounds();
    /**
     * Transformation of the vertex set
     */
    public void translate(float dx, float dy);
    /**
     * Transformation of the vertex set
     */
    public void scale(float s);
    /**
     * Transformation of the vertex set
     */
    public void transform(Matrix m);
    /**
     * Measure the distance between component (centroid) coordinates CX
     * and CY and the centroid of this component
     */
    public float distance(float cx, float cy);
    /**
     * Measure the distance between the centroids of two components
     */
    public float distance(ViewPageComponent c);
    /**
     * Indicate the apparent visual relationship between the component
     * (centroid) and this component (centroid), including the
     * geometric "contains" relation represented by the "Enter" value
     */
    public Input direction(ViewPageComponent c);
    /**
     * Indicate the apparent visual relationship between component
     * (centroid) coordinates CX and CY and this component, including
     * the geometric "contains" relation represented by the "Enter"
     * value
     */
    public Input direction(float cx, float cy);

    public void clearCardinals();

    public int countCardinals();
    /**
     * Navigational focus
     */
    public boolean isCurrent();
    /**
     * Navigational focus state change operator
     */
    public void setCurrent();
    /**
     * Navigational focus state change operator
     */
    public void clearCurrent();

}
