/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.RectF;

/**
 * 
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
    }


    public RectF group(){

        return this.outline[this.b_current];
    }
    public void open(int count){

        final int prev = this.b_current;

        final int next = (0 == prev)?(1):(0);

        final RectF outlinePrev = this.outline[prev];

        final RectF outlineNext = this.outline[next];

        if (outlinePrev.isEmpty()){

            outlineNext.set(Float.MAX_VALUE,Float.MAX_VALUE,Float.MIN_VALUE,Float.MIN_VALUE);
        }
        else {

            outlineNext.set(outlinePrev.left,outlinePrev.top,outlinePrev.right,outlinePrev.bottom);
        }
    }
}
