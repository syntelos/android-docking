/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import com.johnpritchard.docking.View2DFontDPad.Char;
import static com.johnpritchard.docking.View2DFontDPad.Char.*;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * Navigational indicator reflects navigation but is never a member of
 * the set of view page navigational components.
 */
public final class ViewPage2DNavigator
    extends RectF
    implements ViewPage2DComponent
{

    private final static double SP4 = Math.sin(Math.PI/4.0);


    private final Path inside = new Path();

    //private final Path outside = new Path();

    protected final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public ViewPage2DNavigator(){
        super();

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);

        update(Left,Top,Right,Bottom,Circle,480,240);
    }


    public void update(Char left, Char top, Char right, Char bottom, Char center, 
                       float screen_w, float screen_h)
    {
        final float screen_dim = Math.max(screen_w,screen_h);
        final float dim = (screen_dim/10.0f);
        final float pos = (screen_dim/20.0f);
        final float s = (dim/5.0f);
        final float s_d2 = (s/2.0f);
        final float in_s = (float)(SP4*(double)s_d2);
        {
            this.left = (screen_w-dim-pos);
            this.top = (pos);
            this.right = (this.left+dim);
            this.bottom = (this.top+dim);
        }
        final float mid_x = (this.left+this.right)/2.0f;
        final float mid_y = (this.top+this.bottom)/2.0f;
        // {
        //     outside.reset();
        //     outside.moveTo(this.left,mid_y);
        //     outside.lineTo(mid_x,this.bottom);
        //     outside.lineTo(this.right,mid_y);
        //     outside.lineTo(mid_x,this.top);
        //     outside.close();
        // }

        inside.reset();

        final Path path = new Path();
        final RectF src = new RectF();
        final RectF dst = new RectF();
        final Matrix m = new Matrix();
        {
            final float x = (this.left+in_s);
            final float y = (mid_y-s_d2);

            dst.set(x,y,(x+s),(y+s));

            View2DFontDPad.Apply(left,path);

            path.computeBounds(src,true);

            m.setRectToRect(src,dst,Matrix.ScaleToFit.CENTER);

            path.transform(m);

            inside.addPath(path);
        }
        path.reset();
        m.set(null);
        {
            final float x = (mid_x-s_d2);
            final float y = (this.top+in_s);

            dst.set(x,y,(x+s),(y+s));

            View2DFontDPad.Apply(top,path);

            path.computeBounds(src,true);

            m.setRectToRect(src,dst,Matrix.ScaleToFit.CENTER);

            path.transform(m);

            inside.addPath(path);
        }
        path.reset();
        m.set(null);
        {
            final float x = (this.right-in_s-s);
            final float y = (mid_y-s_d2);

            dst.set(x,y,(x+s),(y+s));

            View2DFontDPad.Apply(right,path);

            path.computeBounds(src,true);

            m.setRectToRect(src,dst,Matrix.ScaleToFit.CENTER);

            path.transform(m);

            inside.addPath(path);
        }
        path.reset();
        m.set(null);
        {
            final float x = (mid_x-s_d2);
            final float y = (this.bottom-in_s-s);

            dst.set(x,y,(x+s),(y+s));

            View2DFontDPad.Apply(bottom,path);

            path.computeBounds(src,true);

            m.setRectToRect(src,dst,Matrix.ScaleToFit.CENTER);

            path.transform(m);

            inside.addPath(path);
        }
        path.reset();
        m.set(null);
        {
            final float x = (mid_x-s_d2);
            final float y = (mid_y-s_d2);

            dst.set(x,y,(x+s),(y+s));

            View2DFontDPad.Apply(center,path);

            path.computeBounds(src,true);

            m.setRectToRect(src,dst,Matrix.ScaleToFit.CENTER);

            path.transform(m);

            inside.addPath(path);
        }
    }
    public String getName(){
        return "ViewPage2DNavigator";
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

        inside.transform(m);
        // outside.transform(m);

        inside.computeBounds(this,true);
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
        return false;
    }
    public void setCurrent(){
    }
    public void clearCurrent(){
    }
    public ViewPage2DClip clip(){
        throw new UnsupportedOperationException();
    }
    public final void clearCardinals(){
    }
    public final int countCardinals(){
        return 0;
    }
    public final void setCardinal(Input direction, ViewPage2DComponent component){
        throw new UnsupportedOperationException();
    }
    public final ViewPage2DComponent getCardinal(Input direction){
        return null;
    }
    public final Input direction(float x, float y){
        throw new UnsupportedOperationException();
    }
    public void draw(Canvas c){

        // c.drawPath(outside,paint);
        c.drawPath(inside,paint);
    }
}
