/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.util.Log;

/**
 * One page is displayed by {@link View} at the exclusion of all other
 * pages, during which time it represents the input and output
 * function of the {@link View}.  
 * 
 * The {@link Page} enum is employed to represent pages for navigation
 * operations, and the {@link Input} enum is employed to represent
 * associations for navigation operations.
 * 
 * The page I/O model provides equivalence for internal and external
 * access to the operation of the interaction machine.
 * 
 * Input and (visual) state change events are processed by the {@link
 * ViewAnimator} (via the {@link View} "script" methods) with
 * repaints.  
 * 
 * <h3>Structure<h3>
 * 
 * A {@link ViewPage page} is a list of {@link ViewPage2DComponent
 * components}, each always visible and focusable and occupying a
 * unique region in 2D XY space (without overlapping).
 * 
 * Conceptually a {@link ViewPage2DComponent page component} is a list
 * of vertices and functions for operating on that set of geometric
 * vertices.  In practice, a vertex set may be represented by a {@link
 * ViewPage2DComponentPath path}.  In all cases, matrix operations
 * transform the geometry of a component, and the boundaries of the
 * component geometry are independent of its location.
 * 
 * <h3>Initialization<h3>
 * 
 * The "init" method is called from the constructor to process
 * geometry into a scale independent structure having any dimensions
 * that are convenient.  The minimal X and Y coordinates of this
 * initial structure represent boundary margin in the geometry's own
 * scale.
 * 
 * This phase occurs once and therefore includes all geometric
 * construction.
 * 
 * <h3>Layout</h3>
 * 
 * The "layout" method is called with visual display dimensions to
 * transform the abstract geometry into practical visual display
 * geometry.  When this process has completed, the minimal and maximal
 * X and Y coordinates of the set of (page) components are expected to
 * fit within the dimensions of the display (surface).
 * 
 * This phase occurs each time the page is entered (via {@link View}
 * pageTo), and may transform existing geometry for device orientation
 * and display surface dimensions.
 * 
 * @see View
 * @see ViewPage2DComponent
 */
public abstract class ViewPage
    extends Epsilon
{

    /**
     * Surface properties
     */
    protected int width, height;

    protected float pad;
    /**
     * Completed up
     */
    protected boolean plumb;

    protected View view;

    protected boolean landscape, portrait;



    protected ViewPage(){
        super();
    }


    /**
     * Return static constant page nav identifier.
     */
    public abstract String name();
    /**
     * Return static constant page nav identifier.
     */
    public abstract Page value();
    /**
     * Called by {@link View} after filling the background.
     */
    public abstract void draw(Canvas g);
    /**
     * @return Use gestures and simple geometric navigation with this page
     */
    public final boolean simpleInput(){

        return value().simpleInput;
    }
    public ViewPage up(View view, int width, int height)
    {
        if (width != this.width || height != this.height){
            this.view = view;
            this.width = width;
            this.height = height;

            this.portrait = (height > width);
            this.landscape = (width > height);
            this.pad = ((float)Math.max(width,height)) * PAD_RATIO;

            info("layout");

            layout();

            info("navigation");

            navigation();

        }
        info("focus");

        focus();

        plumb = true;

        return this;
    }
    public void down(){

        plumb = false;
    }
    public void down(SharedPreferences.Editor preferences){

        this.down();
    }
    /**
     * super class ctor initialization
     */
    protected void init(){
    }
    /**
     */
    protected void layout(){
    }
    /**
     */
    protected void navigation(){
    }
    /**
     */
    protected void focus(){
    }
    /**
     * Called from {@link ViewAnimation} to convert pointer activity
     * to navigation activity for subsequent delivery to the input
     * method.
     */
    public abstract InputScript[] script(MotionEvent event);

    protected void input_emphasis(){
    }
    protected void input_deemphasis(){
    }
    /**
     * Convert navigation activity to navigational focus status.
     * 
     * The back button or input script should always have the same
     * effect as on devices where the back button operates directly on
     * the activity stack (without passing through the View key event
     * process).
     */
    public void input(InputScript in){

        switch(in.type()){

        case Emphasis:
            input_emphasis();
            break;

        case Deemphasis:
            input_deemphasis();
            break;

        default:
            break;
        }
    }
    protected final SharedPreferences preferences(){

        if (null != view){

            return view.preferences();
        }
        else {
            throw new IllegalStateException();
        }
    }
    protected static float[] Convert(MotionEvent event){
        if (1 == event.getPointerCount()){

            float[] re = new float[2];
            {
                re[0] = event.getX(0);
                re[1] = event.getY(0);
            }
            return re;
        }
        else {
            return null;
        }
    }

}
