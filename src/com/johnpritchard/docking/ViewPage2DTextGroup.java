/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.RectF;

/**
 * An instance of this class is able to maintain a dynamic bounding
 * box for a fixed substring of the 2D text label.
 * 
 * @see ViewPage2DTextLabel
 */
public class ViewPage2DTextGroup
    extends ViewPage2DTextSelection
    implements ViewPageOperatorGroup
{


    public ViewPage2DTextGroup(){
        this(0);
    }
    public ViewPage2DTextGroup(int begin){
        this(begin,Integer.MAX_VALUE);
    }
    public ViewPage2DTextGroup(int begin, int end){
        super(begin,end);

        groupOpen();
    }


    public void groupOpen(){

        final int next = (0 == this.b_current)?(1):(0);

        this.outline[next].set(Float.MAX_VALUE,Float.MAX_VALUE,Float.MIN_VALUE,Float.MIN_VALUE);
    }
    public void groupClose(){
    }
    public RectF group(){

        return this.outline[this.b_current];
    }
    public void open(int count){
    }
}
