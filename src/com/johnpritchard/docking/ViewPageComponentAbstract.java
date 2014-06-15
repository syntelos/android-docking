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
public abstract class ViewPageComponentAbstract
    extends Epsilon
    implements ViewPageComponent
{
    protected final static ViewPageOperatorSelection NSel = null;

    protected String name;

    protected volatile boolean current;

    protected final RectF bounds = new RectF();

    protected ViewPageOperatorSelection selection;


    public ViewPageComponentAbstract(){
        super();
        this.name = this.baseName;
    }
    public ViewPageComponentAbstract(ViewPageOperatorSelection sel){
        super();
        this.name = this.baseName;
        this.selection = sel;
    }


    protected final void clear(){

        this.bounds.setEmpty();
    }
    public final String getName(){

        return this.name;
    }
    public final void setName(String name){

        if (null != name && 0 < name.length()){

            this.name = name;
        }
        else
            throw new IllegalArgumentException();
    }
    protected final void appendName(String name){

        if (null != name && 0 < name.length()){

            if (0 > this.name.indexOf('/')){

                this.name = this.name+'/'+name;
            }
        }
    }
    public final boolean hasSelection(){

        return (null != selection);
    }
    public final boolean hasSelectionGroup(){

        return (selection instanceof ViewPageOperatorGroup);
    }
    public final ViewPageOperatorSelection getSelection(){

        return selection;
    }
    public final ViewPageOperatorGroup getSelectionGroup(){

        return (ViewPageOperatorGroup)selection;
    }
    // public boolean pageMeasureByGroup(){

    //     return false;
    // }
    public final void setSelection(ViewPageOperatorSelection selection){

        this.selection = selection;
    }
    public RectF bounds(){
        return bounds;
    }
    public final float getCX(){
        final RectF bounds = this.bounds();
        final float cx0 = bounds.left;
        final float cx1 = bounds.right;

        return (cx0+cx1)/2.0f;
    }
    public final float getCY(){
        final RectF bounds = this.bounds();
        final float cy0 = bounds.top;
        final float cy1 = bounds.bottom;

        return (cy0+cy1)/2.0f;
    }
    public final float getX(){
        final RectF bounds = this.bounds();

        return bounds.left;
    }
    public final float getY(){
        final RectF bounds = this.bounds();

        return bounds.top;
    }
    public final float getWidth(){
        final RectF bounds = this.bounds();

        return (bounds.right-bounds.left);
    }
    public final float getHeight(){
        final RectF bounds = this.bounds();

        return (bounds.bottom-bounds.top);
    }
    public final void translate(float x, float y){

        if (0.0f != Z(x) || 0.0f != Z(y)){

            Matrix m = new Matrix();
            {
                m.setTranslate(x,y);
            }
            this.transform(m);
        }
    }
    public final void scale(float s){

        if (0.0f < Z(s) && 1e-7f < Math.abs(1.0f-s)){

            Matrix m = new Matrix();
            {
                m.setScale(s,s);
            }
            this.transform(m);
        }
    }
    public void transform(Matrix m){
    }
    /**
     * Component coordinate space
     */
    public final float distance(float cx, float cy){

        final float dx = (cx-getCX());
        final float dy = (cy-getCY());

        return (float)Math.sqrt(Z(dx*dx) + Z(dy*dy));
    }
    public final float distance(ViewPageComponent c){

        if (c == this){

            return 0.0f;
        }
        else {
            return distance( c.getCX(), c.getCY());
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
}
