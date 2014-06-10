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

    protected boolean focus;

    protected final RectF bounds = new RectF();


    public ViewPageComponentAbstract(){
        super();
        this.name = Basename(getClass().getName());
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
    public RectF bounds(){
        return bounds;
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
    public final Input direction(float cx, float cy){

        final float cx0 = getX();
        final float cy0 = getY();
        final float cx1 = (cx0+getWidth());
        final float cy1 = (cy0+getHeight());

        if (cx0 <= cx && cx1 >= cx &&
            cy0 <= cy && cy1 >= cy)
        {
            return Input.Enter;
        }
        else {

            if (cy < cy0){

                return Input.Up;
            }
            else if (cy > cy1){

                return Input.Down;
            }
            else if (cx < cx0){

                return Input.Left;
            }
            else {
                return Input.Right;
            }
        }
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
