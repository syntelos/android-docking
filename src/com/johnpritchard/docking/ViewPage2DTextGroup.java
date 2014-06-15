/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.RectF;

/**
 * 
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

            outlineNext.set(outlinePrev);
        }
    }
}
