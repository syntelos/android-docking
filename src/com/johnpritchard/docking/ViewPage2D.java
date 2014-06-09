/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.util.Log;

/**
 * <h3>Layout</h3>
 * 
 * The "layout" method is called with visual display dimensions to
 * transform the abstract geometry into practical visual display
 * geometry.  When this process has completed, the minimal and maximal
 * X and Y coordinates of the set of (page) components are expected to
 * fit within the dimensions of the display (surface).
 * 
 * This phase occurs each time the page is entered (via {@link View}
 * pageTo), and may transform existing geometry for device orientation
 * and display surface dimensions.
 * 
 * @see ViewPage
 * @see ViewPage2DComponent
 */
public abstract class ViewPage2D
    extends ViewPage
{


    protected final ViewPage2DComponent[] components;

    protected ViewPage2DComponent current;


    protected ViewPage2D(ViewPage2DComponent[] components){
        super();
        this.components = components;
        info("init");
        init();
    }



    /**
     * Bounding box with independence from screen location
     */
    protected RectF measure(){

        return measure(0,components.length);
    }
    protected RectF measure(int offset, int count){
        final ViewPage2DComponent[] components = this.components;

        RectF g = new RectF();

        for (int cc = offset, end = (offset+count); cc < end; cc++){
            ViewPage2DComponent c = components[cc];

            g.union(c.bounds());
        }
        return g;
    }
    protected RectF scale(float s){
        return scale(0,components.length,s);
    }
    protected RectF scale(int offset, int count, float s){
        final ViewPage2DComponent[] components = this.components;

        RectF g = new RectF();

        for (int cc = offset, end = (offset+count); cc < end; cc++){
            ViewPage2DComponent c = components[cc];

            c.scale(s);

            g.union(c.bounds());
        }
        return g;
    }
    protected void scale(RectF dst){
        scale(0,components.length,dst);
    }
    protected void scale(int offset, int count, RectF dst){

        final Matrix m = new Matrix();
        {
            final RectF src = measure(offset,count);

            m.setRectToRect(src,dst,Matrix.ScaleToFit.CENTER);
        }
        final ViewPage2DComponent[] components = this.components;

        for (int cc = offset, end = (offset+count); cc < end; cc++){

            ViewPage2DComponent c = components[cc];

            c.transform(m);
        }
    }
    /**
     * scale to viewport (layout)
     */
    protected void scale(){
        final ViewPage2DComponent[] components = this.components;
        if (0 < width && 0 < height && null != components){

            final Matrix m = new Matrix();
            {
                final RectF src = measure();

                final RectF dst = new RectF(pad,pad,(width-pad),(height-pad));

                m.setRectToRect(src,dst,Matrix.ScaleToFit.CENTER);
            }

            for (ViewPage2DComponent c : components){

                c.transform(m);
            }
        }
        else {
            throw new IllegalStateException();
        }
    }
    protected float pad(RectF a, RectF b){
        return Math.max(pad(a),pad(b));
    }
    protected float pad(RectF group){
        return (Math.max((group.right-group.left),(group.bottom-group.top)) * PAD_RATIO);
    }
    /**
     * abstract or self center
     */
    protected RectF center(int offset, int count){
        final ViewPage2DComponent[] components = this.components;

        RectF re = new RectF();

        final RectF g = measure(offset,count);

        final float pad = pad(g);
        /*
         * group delta from origin (pad,pad)
         */
        final float gdx = Z(pad-g.left);
        final float gdy = Z(pad-g.top);
        /*
         * group centroid
         */
        final float gw2 = ((g.right-g.left)/2.0f);
        final float gh2 = ((g.bottom-g.top)/2.0f);

        for (int cc = offset, end = (offset+count); cc < end; cc++){
            ViewPage2DComponent c = components[cc];

            final RectF cb = c.bounds();
            /*
             * component centroid
             */
            final float cw2 = (cb.right-cb.left)/2.0f;
            final float ch2 = (cb.bottom-cb.top)/2.0f;
            /*
             * component objective
             */
            final float ox = (gw2-cw2);
            final float oy = (gh2-ch2);
            /*
             * component translation
             */
            final float dx = Z((ox-cb.left)+gdx);
            final float dy = Z((oy-cb.top)+gdy);

            c.translate(dx,dy);

            re.union(c.bounds());
        }
        return re;
    }
    protected void center(RectF g){
        center(0,components.length,g);
    }
    protected void center(int offset, int count, RectF g){
        final ViewPage2DComponent[] components = this.components;

        final float gw2 = ((g.right-g.left)/2.0f);
        final float gh2 = ((g.bottom-g.top)/2.0f);

        for (int cc = offset, end = (offset+count); cc < end; cc++){
            ViewPage2DComponent c = components[cc];

            final RectF cb = c.bounds();

            final float cw2 = (cb.right-cb.left)/2.0f;
            final float ch2 = (cb.bottom-cb.top)/2.0f;

            final float cx = (gw2-cw2);
            final float cy = (gh2-ch2);

            final float dx = Z(cx-cb.left);
            final float dy = Z(cy-cb.top);

            c.translate(dx,dy);
        }
    }
    protected RectF group(int offset, int count, RectF g){

        return group(offset,count,g,pad(g));
    }
    protected RectF group(int offset, int count, RectF g, float p){
        final ViewPage2DComponent[] components = this.components;

        RectF re = new RectF();

        for (int cc = offset, end = (offset+count); cc < end; cc++){
            ViewPage2DComponent c = components[cc];

            if (c instanceof ViewPage2DComponentGroup){

                ((ViewPage2DComponentGroup)c).group(g,p);
            }
            re.union(c.bounds());
        }

        final float margin = Math.max(p,Math.max(re.left,re.top));
        /*
         * Correction for location
         */
        if (0.0f != DE(margin,re.left) || 0.0f != DE(margin,re.top)){

            return location(offset,count,re,margin,margin);
        }
        else {
            return re;
        }
    }
    protected void vertical(){
        final ViewPage2DComponent[] components = this.components;
        if (null != components){

            RectF group = center(0,components.length);

            float pad = pad(group);

            col(0,components.length,pad,pad,(group.right-group.left),pad);
        }
        else {
            throw new IllegalStateException();
        }
    }
    protected void group_vertical(){

        final ViewPage2DComponent[] components = this.components;
        if (null != components){

            RectF group = center(0,components.length);

            float pad = pad(group);

            group = group(0,components.length,group,pad);

            col(0,components.length,pad,pad,(group.right-group.left),pad);
        }
        else {
            throw new IllegalStateException();
        }
    }
    protected RectF location(int offset, int count, RectF g, float x, float y){
        final ViewPage2DComponent[] components = this.components;

        RectF re = new RectF();

        final float dx = (x - g.left);
        final float dy = (y - g.top);

        for (int cc = offset, end = (offset+count); cc < end; cc++){
            ViewPage2DComponent c = components[cc];

            c.translate(dx,dy);

            re.union(c.bounds());
        }
        return re;
    }
    protected RectF row(int offset, int count, float row_x, float row_y, float row_h, float pad){

        final float gh2 = (row_h/2.0f);

        RectF re = new RectF();

        float tx = row_x;

        for (int cc = offset, end = (offset+count); cc < end; cc++){
            ViewPage2DComponent c = components[cc];

            final RectF cb = c.bounds();

            final float ch2 = (cb.bottom-cb.top)/2.0f;

            final float ty = ((gh2-ch2)+row_y);

            final float ox = (tx-cb.left);
            final float oy = (ty-cb.top);

            c.translate(ox,oy);

            re.union(c.bounds());

            tx += ((cb.right-cb.left) + pad);
        }
        return re;
    }
    protected void row(int offset, int count, float row_x, float row_y, float row_w, float row_h, float pad){

        final float gh2 = (row_h/2.0f);

        final float gwN = (row_w/(((float)count)+1.0f));
        final float gwN_d2 = (gwN/2.0f);

        for (int cc = offset, end = (offset+count); cc < end; cc++){

            ViewPage2DComponent c = components[cc];

            final RectF cb = c.bounds();

            final float cw2 = (cb.right-cb.left)/2.0f;
            final float ch2 = (cb.bottom-cb.top)/2.0f;

            final float tx = ((gwN_d2-cw2)+((gwN*((cc-offset)+1.0f))-gwN_d2));
            final float ty = ((gh2-ch2)+row_y);

            final float ox = (tx-cb.left);
            final float oy = (ty-cb.top);

            c.translate(ox,oy);
        }
    }
    protected RectF col(int offset, int count, float col_x, float col_y, float col_w, float pad){

        final float gw2 = (col_w/2.0f);

        RectF re = new RectF();

        float ty = col_y;

        for (int cc = offset, end = (offset+count); cc < end; cc++){
            ViewPage2DComponent c = components[cc];

            final RectF cb = c.bounds();

            final float cw2 = ((cb.right-cb.left)/2.0f);

            final float tx = ((gw2-cw2)+col_x);

            final float ox = (tx-cb.left);
            final float oy = (ty-cb.top);

            c.translate(ox,oy);

            re.union(c.bounds());

            ty += ((cb.bottom-cb.top) + pad);
        }
        return re;
    }

    @Override
    protected void init(){
    }
    @Override
    protected void layout(){
        scale();
    }
    @Override
    public void down(SharedPreferences.Editor preferences){

        this.down();

        preferences.putInt(name()+".focus",this.current());
    }

    /**
     * @see #first()
     */
    protected boolean navigationInclude(int index, ViewPage2DComponent c){
        return true;
    }
    /**
     * Initialize navigation
     */
    @Override
    protected final void navigation(){
        for (int cc = 0, count = components.length; cc < count; cc++){
            ViewPage2DComponent c = components[cc];

            c.clearCardinals();

            if (navigationInclude(cc,c)){

                final float cx = c.getX();
                final float cy = c.getY();

                for (int bb = 0; bb < count; bb++){
                    if (bb != cc){
                        ViewPage2DComponent b = components[bb];

                        if (navigationInclude(bb,b)){

                            final Input dir = c.direction(b.getX(),b.getY());

                            if (Input.Enter != dir){

                                c.setCardinal(dir,b);
                            }
                        }
                    }
                    else {
                        c.setCardinal(Input.Enter,c);
                    }
                }
            }
        }
    }
    /**
     * Initialize focus
     */
    @Override
    protected void focus(){
        SharedPreferences preferences = preferences();
        int first = first();
        int focus = preferences.getInt(name()+".focus",first);

        if (focus < components.length){

            current = components[focus];
        }
        else {
            current = components[first];
        }
        for (ViewPage2DComponent c : components){

            if (c == current){
                c.setCurrent();
            }
            else {
                c.clearCurrent();
            }
        }
    }
    /**
     * Called from {@link ViewAnimation} to convert pointer activity
     * to navigation activity for subsequent delivery to the input
     * method.
     */
    @Override
    public final InputScript[] script(MotionEvent event){
        if (null != event){

            if (0 != (event.getSource() & InputDevice.SOURCE_CLASS_POINTER)){
                /*
                 *  Absolute coordinate space
                 */
                switch(event.getActionMasked()){
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_MOVE:
                    {
                        final float[] xy = Convert(event);
                        if (null != xy){

                            //info("script motion <absolute> xy");

                            return script(null,xy[0],xy[1],Float.MAX_VALUE,current);
                        }
                        else {
                            //info("script motion <absolute> xy <null>");

                            return null;
                        }
                    }
                default:
                    //info("script motion <absolute unknown>");
                    break;
                }
            }
            else {
                //info("script motion <relative>");
                /*
                 * Relative coordinate space
                 */
                int px = event.getActionIndex();
                float x = event.getX(px);
                float y = event.getY(px);
                if (0.0f != x || 0.0f != y){

                    if (Math.abs(x) > Math.abs(y)){

                        if (0.0f < x){

                            return new InputScript[]{Input.Left};
                        }
                        else {
                            return new InputScript[]{Input.Right};
                        }
                    }
                    else if (0.0f < y){

                        return new InputScript[]{Input.Down}; // like a dpad?
                    }
                    else {
                        return new InputScript[]{Input.Up};
                    }
                }
            }
        }
        // else {
        //     info("script event <null>");
        // }
        return null;
    }
    /**
     * Append to list while "distance" is decreasing or direction is "enter".
     */
    protected InputScript[] script(InputScript[] list, float x, float y, float distance, ViewPage2DComponent current){

        if (null != current){

            final Input dir = current.direction(x,y);

            if (null == dir){

                //info("script current "+current.getName()+" direction <null>");

                return list;
            }
            else {
                //info("script current "+current.getName()+" direction "+dir.name());

                if (Input.Enter == dir){
                    /*
                     * Separate ENTER from selection process
                     */
                    if (null == list){

                        return View.Script.Enter();
                    }
                    else {
                        return list;
                    }
                }
                else {
                    final float dis = current.distance(x,y);

                    if (dis < distance){
                        /*
                         * Visual code generation to not repeat {Deemphasis}
                         */
                        final InputScript[] add = View.Script.Direction(dir);

                        if (null == list){

                            list = add;
                        }
                        else {
                            list = Input.Add(list,add);
                        }

                        return script(list,x,y,dis,current.getCardinal(dir));
                    }
                }
            }
        }
        // else {
        //     info("script current <null>" );
        // }
        return list;
    }
    @Override
    protected void input_emphasis(){

        if (current instanceof ViewPage2DComponentPath){

            ((ViewPage2DComponentPath)current).emphasis(true);
        }
    }
    @Override
    protected void input_deemphasis(){

        if (current instanceof ViewPage2DComponentPath){

            ((ViewPage2DComponentPath)current).emphasis(false);
        }
    }
    /**
     * Convert navigation activity to navigational focus status.
     */
    @Override
    public void input(InputScript event){

        Input in = event.type();

        if (in.geometric){

            ViewPage2DComponent next = current.getCardinal(in);
            if (null != next && next != current){

                current.clearCurrent();
                current = next;
                current.setCurrent();
            }
        }
        else {
            super.input(event);
        }
    }
    /**
     * Called by {@link View} after filling the background.
     */
    @Override
    public void draw(Canvas g){

        for (ViewPage2DComponent c : components){

            c.draw(g);
        }
    }
    /**
     * This may return negative one when a subclass is filtering
     * (excluding) enter events.
     */
    protected int enter(){

        return this.current();
    }
    /**
     * According to the typical implementation of {@link #focus()},
     * this will not return negative one.
     */
    protected int current(){
        ViewPage2DComponent[] components = this.components;

        final int count = components.length;
        for (int cc = 0; cc < count; cc++){

            if (current == components[cc]){

                return cc;
            }
        }
        return -1;
    }
    /**
     * Initialize focus with a central component
     * 
     * @see #navigationInclude
     */
    protected int first(){
        int first = 0;

        if (1 < components.length){

            int first_score = -1;

            final int count = (int)Math.ceil((float)components.length/2.0f);

            for (int cc = 0; cc < count; cc++){

                ViewPage2DComponent c = components[cc];

                int c_score = c.countCardinals();

                if (c_score >= first_score){

                    first = cc;
                    first_score = c_score;
                }
            }
        }
        return first;
    }
    protected ViewPage2DPath.FillType getFillType(){
        for (ViewPage2DComponent c : components){
            if (c instanceof ViewPage2DComponentPath){
                return ((ViewPage2DComponentPath)c).getFillType();
            }
        }
        return ViewPage2DPath.FillType.WINDING;
    }
    protected void setFillType(ViewPage2DPath.FillType ft){
        for (ViewPage2DComponent c : components){
            if (c instanceof ViewPage2DComponentPath){
                ViewPage2DComponentPath p = (ViewPage2DComponentPath)c;
                p.setFillType(ft);
            }
        }
    }
    protected void rotateFillType(){
        for (ViewPage2DComponent c : components){
            if (c instanceof ViewPage2DComponentPath){
                ViewPage2DComponentPath p = (ViewPage2DComponentPath)c;
                p.rotateFillType();
            }
        }
    }
    protected void logFillType(){
        for (ViewPage2DComponent c : components){
            if (c instanceof ViewPage2DComponentPath){
                ViewPage2DComponentPath p = (ViewPage2DComponentPath)c;
                p.logFillType();
            }
        }
    }
}
