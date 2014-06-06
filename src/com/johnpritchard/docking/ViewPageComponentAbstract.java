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
    public final static String TAG = ObjectLog.TAG;


    protected String name;

    protected ViewPageComponent[] cardinals = new ViewPageComponent[Input.GeometricCount];

    protected boolean focus;

    protected final Clip clip = new Clip();

    protected final RectF bounds = new RectF();


    public ViewPageComponentAbstract(){
        super();
        this.name = Basename(getClass().getName());
    }


    protected final void clear(){
        this.bounds.setEmpty();
        //this.clip.reset();
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
    public final Clip clip(){

        return this.clip;
    }
    public ViewPageComponentAbstract clip(Operand[] list){
        if (null != list){
            this.clear();

            this.clip.apply(list);
        }
        return this;
    }
    public final float getCX(){
        final float cx0 = getX();
        final float cx1 = (cx0+getWidth());

        return (cx0+cx1)/2.0f;
    }
    public final float getCY(){
        final float cy0 = getY();
        final float cy1 = (cy0+getHeight());

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
    public final float distance(float x, float y){

        final float dx = (x-getCX());
        final float dy = (y-getCY());

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
    public final Input direction(float x, float y){

        final float cx0 = getX();
        final float cy0 = getY();
        final float cx1 = (cx0+getWidth());
        final float cy1 = (cy0+getHeight());

        if (cx0 <= x && cx1 >= x &&
            cy0 <= y && cy1 >= y)
        {
            return Input.Enter;
        }
        else {

            if (y < cy0){

                return Input.Up;
            }
            else if (y > cy1){

                return Input.Down;
            }
            else if (x < cx0){

                return Input.Left;
            }
            else {
                return Input.Right;
            }
        }
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
    public final void setCardinal(Input direction, ViewPageComponent component){

        if (direction.geometric){

            ViewPageComponent existing = cardinals[direction.index];

            if (null == existing || distance(component) < distance(existing)){

                //info("navigation "+getName()+" ("+direction.name()+")-> "+component.getName());

                cardinals[direction.index] = component;
            }
        }
    }
    public final ViewPageComponent getCardinal(Input direction){

        if (direction.geometric){

            return cardinals[direction.index];
        }
        else
            throw new IllegalArgumentException(direction.name());
    }
    public final boolean isCurrent(){

        return this.focus;
    }
    public void setCurrent(){

        this.focus = true;
    }
    public void clearCurrent(){

        this.focus = false;
    }
    protected void verbose(String m){
        Log.i(TAG,(getClass().getName()+' '+m));
    }
    protected void verbose(String m, Throwable t){
        Log.i(TAG,(getClass().getName()+' '+m),t);
    }
    protected void debug(String m){
        Log.d(TAG,(getClass().getName()+' '+m));
    }
    protected void debug(String m, Throwable t){
        Log.d(TAG,(getClass().getName()+' '+m),t);
    }
    protected void info(String m){
        Log.i(TAG,(getClass().getName()+' '+m));
    }
    protected void info(String m, Throwable t){
        Log.i(TAG,(getClass().getName()+' '+m),t);
    }
    protected void warn(String m){
        Log.w(TAG,(getClass().getName()+' '+m));
    }
    protected void warn(String m, Throwable t){
        Log.w(TAG,(getClass().getName()+' '+m),t);
    }
    protected void error(String m){
        Log.e(TAG,(getClass().getName()+' '+m));
    }
    protected void error(String m, Throwable t){
        Log.e(TAG,(getClass().getName()+' '+m),t);
    }
    protected void wtf(String m){
        Log.wtf(TAG,(getClass().getName()+' '+m));
    }
    protected void wtf(String m, Throwable t){
        Log.wtf(TAG,(getClass().getName()+' '+m),t);
    }
    protected final static String Basename(String cn){
        int idx = cn.lastIndexOf('.');
        if (0 < idx)
            return cn.substring(idx+1);
        else
            return cn;
    }
}
