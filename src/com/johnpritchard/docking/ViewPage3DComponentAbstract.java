/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import path.Operand;

import android.graphics.Matrix;
import android.graphics.RectF;
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
     * Anything using nio buffers and 3-tuple floats will have stride
     * (3*bpf) because the nio buffers are employed as a partial or
     * incomplete pointer lacking position - offset.
     */
    protected final static int stride = (3*bpf);

    protected final static java.nio.ByteOrder nativeOrder = java.nio.ByteOrder.nativeOrder();


    protected ViewPage3DComponent[] cardinals = new ViewPage3DComponent[Input.GeometricCount];


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

}