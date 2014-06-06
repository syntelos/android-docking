/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.llg;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;

/**
 * Page components are always visible and focusable and do not overlap
 * in X-Y space.  This component is comparable to the Android
 * Drawable, and differs primarily in its emphasis on a simplified
 * model of interaction.
 * 
 * @see ViewPageComponentPath
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
     * Geometric clipping area should default to the shape boundary
     * @see Clip#shape
     */
    public Clip clip();
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
     * Measure the distance between screen coordinates X and Y and the
     * centroid of this component
     */
    public float distance(float x, float y);
    /**
     * Measure the distance between the centroids of two components
     */
    public float distance(ViewPageComponent c);
    /**
     * Indicate the apparent visual relationship between screen
     * coordinates X and Y and this component, including the geometric
     * "contains" relation represented by the "Enter" value
     */
    public Input direction(float x, float y);

    public void clearCardinals();

    public int countCardinals();
    /**
     * Initialization of the navigational operator: accept multiple
     * components in any one direction, keeping the one which is least
     * distant from this one.
     * 
     * The "Enter" input may be called with the "this" reference.
     * 
     * @param direction An input having geometric application
     * @param component A component member of the same page
     */
    public void setCardinal(Input direction, ViewPageComponent component);
    /**
     * Navigational operator
     */
    public ViewPageComponent getCardinal(Input direction);
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
    /**
     * Visual output operator
     */
    public void draw(Canvas c);

}
