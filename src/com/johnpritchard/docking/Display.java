/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Matrix;
import android.graphics.RectF;

/**
 * The display matrix adds translation and transformation convenience
 * features to the Android 2D (3x3) matrix.
 */
public final class Display
    extends android.graphics.Matrix
{
    private final static float[] clear = {
        0.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 0.0f
    };


    private boolean plumb;

    private final float[] cache = new float[9];


    public Display(){
        super();
    }


    private void clear(){
        System.arraycopy(clear,0,cache,0,9);
        plumb = false;
    }
    private float[] cache(){
        if (!plumb){
            super.getValues(cache);
            plumb = true;
        }
        return this.cache;
    }
    public float getSX(){

        return cache()[MSCALE_X];
    }
    public float getTX(){

        return cache()[MTRANS_X];
    }
    public float getSY(){

        return cache()[MSCALE_Y];
    }
    public float getTY(){

        return cache()[MTRANS_Y];
    }
    public Display identity(){
        super.reset();
        return this;
    }
    public Display transform(RectF src){

        return this.transform(src,src);
    }
    public Display transform(RectF src, RectF dst){

        float[] fv_src = {
            src.left, src.top,
            src.right, src.bottom
        };

        float[] fv_dst = new float[4];

        super.mapPoints(fv_dst,0,fv_src,0,2);

        dst.left = fv_dst[0];
        dst.top = fv_dst[1];
        dst.right = fv_dst[2];
        dst.bottom = fv_dst[3];

        return this;
    }
    @Override
    public void set(Matrix src){
        clear();
        super.set(src);
    }
    @Override
    public void reset(){
        clear();
        super.reset();
    }
    @Override
    public void setTranslate(float dx, float dy){
        clear();
        super.setTranslate(dx,dy);
    }
    @Override
    public void setScale(float sx, float sy, float px, float py){
        clear();
        super.setScale(sx,sy,px,py);
    }
    @Override
    public void setScale(float sx, float sy){
        clear();
        super.setScale(sx,sy);
    }
    @Override
    public void setRotate(float degrees, float px, float py){
        clear();
        super.setRotate(degrees,px,py);
    }
    @Override
    public void setRotate(float degrees){
        clear();
        super.setRotate(degrees);
    }
    @Override
    public void setSinCos(float sinValue, float cosValue, float px, float py){
        clear();
        super.setSinCos(sinValue,cosValue,px,py);
    }
    @Override
    public void setSinCos(float sinValue, float cosValue){
        clear();
        super.setSinCos(sinValue,cosValue);
    }
    @Override
    public void setSkew(float kx, float ky, float px, float py){
        clear();
        super.setSkew(kx,ky,px,py);
    }
    @Override
    public void setSkew(float kx, float ky){
        clear();
        super.setSkew(kx,ky);
    }
    @Override
    public boolean setConcat(Matrix a, Matrix b){
        clear();
        return super.setConcat(a, b);
    }
    @Override
    public boolean preTranslate(float dx, float dy){
        clear();
        return super.preTranslate(dx, dy);
    }
    @Override
    public boolean preScale(float sx, float sy, float px, float py){
        clear();
        return super.preScale(sx, sy, px, py);
    }
    @Override
    public boolean preScale(float sx, float sy){
        clear();
        return super.preScale(sx, sy);
    }
    @Override
    public boolean preRotate(float degrees, float px, float py){
        clear();
        return super.preRotate(degrees, px, py);
    }
    @Override
    public boolean preRotate(float degrees){
        clear();
        return super.preRotate(degrees);
    }
    @Override
    public boolean preSkew(float kx, float ky, float px, float py){
        clear();
        return super.preSkew(kx, ky, px, py);
    }
    @Override
    public boolean preSkew(float kx, float ky){
        clear();
        return super.preSkew(kx, ky);
    }
    @Override
    public boolean preConcat(Matrix other){
        clear();
        return super.preConcat(other);
    }
    @Override
    public boolean postTranslate(float dx, float dy){
        clear();
        return super.postTranslate(dx, dy);
    }
    @Override
    public boolean postScale(float sx, float sy, float px, float py){
        clear();
        return super.postScale(sx, sy, px, py);
    }
    @Override
    public boolean postScale(float sx, float sy){
        clear();
        return super.postScale(sx, sy);
    }
    @Override
    public boolean postRotate(float degrees, float px, float py){
        clear();
        return super.postRotate(degrees, px, py);
    }
    @Override
    public boolean postRotate(float degrees){
        clear();
        return super.postRotate(degrees);
    }
    @Override
    public boolean postSkew(float kx, float ky, float px, float py){
        clear();
        return super.postSkew(kx, ky, px, py);
    }
    @Override
    public boolean postSkew(float kx, float ky){
        clear();
        return super.postSkew(kx, ky);
    }
    @Override
    public boolean postConcat(Matrix other){
        clear();
        return super.postConcat(other);
    }
    @Override
    public boolean setRectToRect(RectF src, RectF dst, ScaleToFit stf){
        clear();
        return super.setRectToRect(src, dst, stf);
    }
    @Override
    public boolean setPolyToPoly(float[] src, int srcIndex, float[] dst, int dstIndex,
                                 int pointCount){
        clear();
        return super.setPolyToPoly(src, srcIndex, dst, dstIndex, pointCount);
    }
    @Override
    public void setValues(float[] values){
        clear();
        setValues(values);
    }
}
