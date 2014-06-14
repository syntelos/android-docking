/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * An instance of this class is able to maintain a dynamic bounding
 * box for a fixed substring of the 2D text label.
 * 
 * @see ViewPage2DTextLabel
 */
public class ViewPage2DTextSelection
    extends ViewPage2DComponentAbstract
    implements ViewPageOperatorSelection
{

    protected final int begin;

    protected final int end;

    protected final RectF[] outline = new RectF[]{
        new RectF(), new RectF()
    };

    protected volatile int b_current;


    public ViewPage2DTextSelection(int begin){
        this(begin,Integer.MAX_VALUE);
    }
    public ViewPage2DTextSelection(int begin, int end){
        super();
        if (-1 < begin && begin < end){
            this.begin = begin;
            this.end = end;
        }
        else {
            throw new IllegalArgumentException();
        }
    }


    public void open(int count){

        final int next = (0 == this.b_current)?(1):(0);

        final RectF outline = this.outline[next];

        outline.set(Float.MAX_VALUE,Float.MAX_VALUE,Float.MIN_VALUE,Float.MIN_VALUE);
    }
    public void update(int index, double ix){

        final float x = (float)ix;

        if (begin <= index && index < end){

            final int next = (0 == this.b_current)?(1):(0);

            final RectF outline = this.outline[next];

            outline.left = Math.min(outline.left,x);
            outline.right = Math.max(outline.right,x);
        }
    }
    public void update(int index, double ix, double iy){

        final float x = (float)ix;
        final float y = (float)iy;

        if (begin <= index && index < end){

            final int next = (0 == this.b_current)?(1):(0);

            final RectF outline = this.outline[next];

            outline.left = Math.min(outline.left,x);
            outline.right = Math.max(outline.right,x);

            outline.top = Math.min(outline.top,y);
            outline.bottom = Math.max(outline.bottom,y);
        }
    }
    public void update(int index, double minX, double minY, double maxX, double maxY){

        final float x0 = (float)minX;
        final float y0 = (float)minY;

        final float x1 = (float)maxX;
        final float y1 = (float)maxY;

        if (begin <= index && index < end){

            final int next = (0 == this.b_current)?(1):(0);

            final RectF outline = this.outline[next];

            outline.left = Math.min(outline.left,x0);
            outline.right = Math.max(outline.right,x1);

            outline.top = Math.min(outline.top,y0);
            outline.bottom = Math.max(outline.bottom,y1);
        }
    }
    public void update(int index, RectF bounds){

        final float x0 = bounds.left;
        final float y0 = bounds.top;

        final float x1 = bounds.right;
        final float y1 = bounds.bottom;

        if (begin <= index && index < end){

            final int next = (0 == this.b_current)?(1):(0);

            final RectF outline = this.outline[next];

            outline.left = Math.min(outline.left,x0);
            outline.right = Math.max(outline.right,x1);

            outline.top = Math.min(outline.top,y0);
            outline.bottom = Math.max(outline.bottom,y1);
        }
    }
    public void update(float[] m){
    }
    public void close(){

        final int next = (0 == this.b_current)?(1):(0);

        this.pad(next);

        this.b_current = next;
    }
    protected void pad(int which){

        final RectF outline = this.outline[which];

        final float pad = (Math.max((outline.right-outline.left),(outline.bottom-outline.top)) * 0.10f);
        {
            outline.left   -= pad;
            outline.top    -= pad;
            outline.right  += pad;
            outline.bottom += pad;
        }
    }
    public void draw(Canvas c){
    }
}
