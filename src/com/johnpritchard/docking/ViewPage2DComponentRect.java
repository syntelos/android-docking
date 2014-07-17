/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;

/**
 * Invisible rectangle component for a navigation window
 */
public class ViewPage2DComponentRect
    extends RectF
    implements ViewPage2DComponent
{


    protected final String baseName;

    protected volatile boolean current;

    protected ViewPage2DComponent[] cardinals = new ViewPage2DComponent[Input.GeometricCount];


    public ViewPage2DComponentRect(){
        super();
        baseName = ObjectLog.Basename(this.getClass().getName());
    }
    public ViewPage2DComponentRect(float left, float top, float right, float bottom){
        this();
        set(left,top,right,bottom);
    }

    public String getName(){
        return baseName;
    }
    public void setName(String name){
        throw new UnsupportedOperationException();
    }
    public boolean hasSelection(){
        return false;
    }
    public boolean hasSelectionGroup(){
        return false;
    }
    public ViewPageOperatorSelection getSelection(){
        return null;
    }
    public ViewPageOperatorGroup getSelectionGroup(){
        return null;
    }
    public void setSelection(ViewPageOperatorSelection selection){
        throw new UnsupportedOperationException();
    }
    public RectF bounds(){

        return this;
    }
    public final float getCX(){

        final float cx0 = this.left;
        final float cx1 = this.right;

        return (cx0+cx1)/2.0f;
    }
    public final float getCY(){

        final float cy0 = this.top;
        final float cy1 = this.bottom;

        return (cy0+cy1)/2.0f;
    }
    public final float getX(){

        return this.left;
    }
    public final float getY(){

        return this.top;
    }
    public final float getWidth(){

        return (this.right-this.left);
    }
    public final float getHeight(){

        return (this.bottom-this.top);
    }
    public final void translate(float x, float y){

        if (0.0f != Epsilon.Z(x) || 0.0f != Epsilon.Z(y)){

            this.left += x;
            this.top += y;
        }
    }
    public final void scale(float s){

        if (0.0f < Epsilon.Z(s) && 1e-7f < Math.abs(1.0f-s)){

            Matrix m = new Matrix();
            {
                m.setScale(s,s);
            }
            this.transform(m);
        }
    }
    public void transform(Matrix m){

        float[] src = {left,top,right,bottom};
        float[] dst = {0f,0f,0f,0f};

        m.mapPoints(dst,0,src,0,2);

        left   = dst[0];
        top    = dst[1];
        right  = dst[2];
        bottom = dst[3];
    }
    /**
     * Component coordinate space
     */
    public final float distance(float cx, float cy){

        final float dx = (cx-getCX());
        final float dy = (cy-getCY());

        return (float)Math.sqrt(Epsilon.Z(dx*dx) + Epsilon.Z(dy*dy));
    }
    public final float distance(ViewPageComponent c){

        if (c == this){

            return 0.0f;
        }
        else {
            return distance(c.getCX(),c.getCY());
        }
    }
    public final Input direction(ViewPageComponent c){

        return direction(c.getCX(),c.getCY());
    }
    public final boolean isCurrent(){

        return this.current;
    }
    public void setCurrent(){

        this.current = true;
    }
    public void clearCurrent(){

        this.current = false;
    }
    public ViewPage2DClip clip(){
        throw new UnsupportedOperationException();
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

        if (this.contains(x,y)){

            return Input.Enter;
        }
        else if (y < this.top){

            return Input.Up;
        }
        else if (y > this.bottom){

            return Input.Down;
        }
        else if (x < this.left){

            return Input.Left;
        }
        else {
            return Input.Right;
        }
    }
    public void draw(Canvas c){
    }
}
