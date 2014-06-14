/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.RectF;

/**
 * An instance of this class is able to maintain a dynamic bounding
 * box for a fixed substring of the 3D text label.
 * 
 * @see ViewPage3DTextLabel
 */
public class ViewPage3DTextGroup
    extends ViewPage3DTextSelection
    implements ViewPageOperatorGroup
{


    public ViewPage3DTextGroup(double z){
        this(0,z);
    }
    public ViewPage3DTextGroup(int begin, double z){
        this(begin,Integer.MAX_VALUE,z);
    }
    public ViewPage3DTextGroup(int begin, int end, double z){
        super(begin,end,z);

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
