/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import path.Operand;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;

/**
 * Implementation of the page component interface independent of the
 * implementation of geometry
 */
public abstract class ViewPage2DComponentAbstract
    extends ViewPageComponentAbstract
    implements ViewPage2DComponent
{
    public final static ViewPage2DComponent Nil = null;


    protected ViewPage2DComponent[] cardinals = new ViewPage2DComponent[Input.GeometricCount];

    protected final ViewPage2DClip clip = new ViewPage2DClip();


    public ViewPage2DComponentAbstract(){
        super();
    }
    public ViewPage2DComponentAbstract(ViewPageOperatorSelection sel){
        super(sel);
    }


    public final ViewPage2DClip clip(){

        return this.clip;
    }
    public ViewPage2DComponentAbstract clip(Operand[] list){
        if (null != list){
            this.clear();

            this.clip.apply(list);
        }
        return this;
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
    public final void setCardinal(Input direction, ViewPage2DComponent component){

        if (direction.geometric){

            ViewPage2DComponent existing = cardinals[direction.index];

            if (null == existing || distance(component) < distance(existing)){

                cardinals[direction.index] = component;
            }
        }
    }
    public final ViewPage2DComponent getCardinal(Input direction){

        if (direction.geometric){

            return cardinals[direction.index];
        }
        else
            throw new IllegalArgumentException(direction.name());
    }
    public final Input direction(float x, float y){

        final RectF bounds = bounds();

        if (bounds.contains(x,y)){

            return Input.Enter;
        }
        else if (y < bounds.top){

            return Input.Up;
        }
        else if (y > bounds.bottom){

            return Input.Down;
        }
        else if (x < bounds.left){

            return Input.Left;
        }
        else {
            return Input.Right;
        }
    }

}
