/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import path.Operand;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.Log;

import java.nio.ByteOrder;

/**
 * Implementation of the page component interface independent of the
 * implementation of geometry
 */
public abstract class ViewPage3DComponentAbstract
    extends ViewPageComponentAbstract
    implements ViewPage3DComponent
{
    /**
     * Bytes per float (32 bits) is four.
     */
    protected final static int bpf = 4;
    /**
     * Bytes for float 3-tuple
     */
    protected final static int bpf3 = (bpf * 3);
    /**
     * Anything using nio buffers and 3-tuple floats will have stride
     * (3*bpf) because the nio buffers are employed as a partial or
     * incomplete pointer (not using position - offset information).
     */
    protected final static int stride = bpf3;



    protected final static java.nio.ByteOrder nativeOrder = java.nio.ByteOrder.nativeOrder();


    protected ViewPage3DComponent[] cardinals = new ViewPage3DComponent[Input.GeometricCount];

    private long blinkTime;

    private boolean blinkState = true;


    public ViewPage3DComponentAbstract(){
        super();
    }


    public final void clearCardinals(){

        for (Input dir: Input.Geometric){

            cardinals[dir.index] = null;
        }
    }
    public final int countCardinals(){
        int count = 0;

        for (Input dir: Input.Geometric){

            if (null != cardinals[dir.index]){

                count += 1;
            }
        }
        return count;
    }
    public final void setCardinal(Input direction, ViewPage3DComponent component){

        if (direction.geometric){

            ViewPage3DComponent existing = cardinals[direction.index];

            if (null == existing || distance(component) < distance(existing)){

                cardinals[direction.index] = component;

                info("navigation "+getName()+' '+direction+" = "+component.getName());
            }
        }
    }
    public final ViewPage3DComponent getCardinal(Input direction){

        if (direction.geometric){

            return cardinals[direction.index];
        }
        else
            throw new IllegalArgumentException(direction.name());
    }
    public final void unblink(){

        blinkTime = 0L;
        blinkState = true;

        draw();
    }
    public final void blink(long period){

        final long time = SystemClock.uptimeMillis();

        if (time >= (blinkTime+period)){

            blinkTime = time;
            blinkState = (!blinkState);
        }

        if (blinkState){

            draw();
        }
    }
    public final Input direction(float x, float y){

        final RectF bounds = bounds();

        if (bounds.contains(x,y)){

            return Input.Enter;
        }
        else if (y < bounds.top){

            return Input.Down;
        }
        else if (y > bounds.bottom){

            return Input.Up;
        }
        else if (x < bounds.left){

            return Input.Left;
        }
        else {
            return Input.Right;
        }
    }

}
