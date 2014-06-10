/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import path.Op;
import path.Operand;
import path.Parser;
import path.Winding;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;

/**
 * 
 */
public class ViewPage2DComponentPath
    extends ViewPage2DComponentAbstract
    implements path.Path
{
    protected final static float TextSize = 10.0f;


    protected final ViewPage2DPath path = new ViewPage2DPath();

    protected final ViewPage2DPath group = new ViewPage2DPath();

    protected final Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);

    protected final Paint stroke = new Paint(Paint.ANTI_ALIAS_FLAG);


    public ViewPage2DComponentPath(){
        super();
        fill.setColor(Color.BLACK);
        fill.setTextSize(TextSize);
        fill.setTypeface(Typeface.SANS_SERIF);
        fill.setStyle(Paint.Style.FILL);
        stroke.setColor(Color.BLACK);
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(3.0f);
    }
    public ViewPage2DComponentPath(String text){
        this();
        setText(text);
    }
    public ViewPage2DComponentPath(Operand[] path){
        this();
        this.path(path);
    }
    public ViewPage2DComponentPath(Operand[] path, Operand[] group){
        this();
        this.path(path);
        this.group(group);
    }
    public ViewPage2DComponentPath(Operand[] path, Operand[] group, Operand[] clip){
        this();
        this.path(path);
        this.group(group);
        this.clip(clip);
    }


    protected void emphasis(boolean on){

        if (on){

            fill.setColor(Color.RED);
        }
        else {

            fill.setColor(Color.BLACK);
        }
    }
    public final void format(String fmt, Object... args){

        this.setText(String.format(fmt,args));
    }
    public ViewPage2DComponentPath setText(String text){
        reset();
        this.fill.getTextPath(text,0,text.length(),0.0f,TextSize,this.path);
        this.appendName(text);
        return this;
    }
    public void group(final RectF dim, final float pad){

        this.clear();

        this.group.rounding(true);
        this.group.margin(pad);
        this.group.set(dim);

        this.clip.rounding(true);
        this.clip.margin(pad);
        this.clip.set(dim);

    }
    public RectF bounds(){

        if (this.bounds.isEmpty()){

            if (this.group.isEmpty()){

                this.path.computeBounds(this.bounds,true);
            }
            else {

                this.group.computeBounds(this.bounds,true);
            }
            this.clip.set(this.bounds);

            //info("bounds left: "+bounds.left+", right: "+bounds.right+", top: "+bounds.top+", bottom: "+bounds.bottom);
        }
        return this.bounds;
    }
    public boolean isEmpty(){
        return this.path.isEmpty();
    }
    public ViewPage2DComponentPath incCapacity(int delta){
        this.path.incReserve(delta);
        return this;
    }
    public boolean isInverseFillType(){
        return this.path.isInverseFillType();
    }
    public void toggleInverseFillType(){
        this.path.toggleInverseFillType();
    }
    public ViewPage2DComponentPath plainFillType(){

        this.path.plainFillType();
        return this;
    }
    public ViewPage2DComponentPath inverseFillType(){

        this.path.inverseFillType();
        return this;
    }
    public ViewPage2DPath.FillType getFillType(){
        return this.path.getFillType();
    }
    public void setFillType(ViewPage2DPath.FillType ft){
        this.path.setFillType(ft);
    }
    public ViewPage2DComponentPath rotateFillType(){

        this.path.rotateFillType();
        this.clip.rotateFillType();
        return this;
    }
    public ViewPage2DComponentPath logFillType(){
        switch(this.path.getFillType()){
        case WINDING:
            info(getName()+" path: WINDING");
            break;
        case EVEN_ODD:
            info(getName()+" path: EVEN_ODD");
            break;
        case INVERSE_WINDING:
            info(getName()+" path: INVERSE_WINDING");
            break;
        case INVERSE_EVEN_ODD:
            info(getName()+" path: INVERSE_EVEN_ODD");
            break;
        }
        switch(this.clip.getFillType()){
        case WINDING:
            info(getName()+" clip: WINDING");
            break;
        case EVEN_ODD:
            info(getName()+" clip: EVEN_ODD");
            break;
        case INVERSE_WINDING:
            info(getName()+" clip: INVERSE_WINDING");
            break;
        case INVERSE_EVEN_ODD:
            info(getName()+" clip: INVERSE_EVEN_ODD");
            break;
        }
        return this;
    }
    public void transform(Matrix m){

        this.clear();

        this.path.transform(m);

        if (!this.group.isEmpty()){

            this.group.transform(m);
        }
        this.bounds(); // temp-report
    }
    public void draw(Canvas c){
        c.save();

        c.clipPath(this.clip,Region.Op.REPLACE);

        if (!group.isEmpty()){

            c.drawPath(this.group,this.stroke);
        }
        c.drawPath(this.path,this.fill);

        c.restore();
    }
    public java.lang.Iterable<Operand> toPathIterable(){

        return this.path.toPathIterable();
    }
    public java.util.Iterator<Operand> toPathIterator(){

        return this.path.toPathIterator();
    }
    public path.Path setWinding(Winding winding){

        this.path.setWinding(winding);
        return this;
    }
    public Winding getWinding(){

        return this.path.getWinding();
    }
    public boolean isWindingNonZero(){

        return this.path.isWindingNonZero();
    }
    public boolean isWindingEvenOdd(){

        return this.path.isWindingEvenOdd();
    }
    public path.Path setWindingNonZero(){

        return this.path.setWindingNonZero();
    }
    public path.Path setWindingEvenOdd(){

        return this.path.setWindingEvenOdd();
    }
    public float[] getVerticesPath(int index, Op op, float[] vertices){

        return this.path.getVerticesPath(index,op,vertices);
    }
    public void add(Op op, float[] operands){

        this.path.add(op,operands);
    }
    public void moveTo(float[] operands){

        this.path.moveTo(operands);
    }
    public void moveTo(float x, float y){

        this.path.moveTo(x,y);
    }
    public void lineTo(float[] operands){

        this.path.lineTo(operands);
    }
    public void lineTo(float x, float y){

        this.path.lineTo(x,y);
    }
    public void quadTo(float[] operands){

        this.path.quadTo(operands);
    }
    public void quadTo(float x1, float y1,
                       float x2, float y2)
    {
        this.path.quadTo(x1,y1,x2,y2);
    }
    public void cubicTo(float[] operands){

        this.path.cubicTo(operands);
    }
    public void cubicTo(float x1, float y1,
                        float x2, float y2,
                        float x3, float y3)
    {
        this.clear();
        this.path.cubicTo(x1,y1,x2,y2,x3,y3);
    }
    public void close(){

        this.path.close();
    }
    public path.Path apply(String pexpr){

        return this.path.apply(pexpr);
    }
    public path.Path apply(Parser p){

        this.clear();

        return this.path.apply(p);
    }
    public ViewPage2DComponentPath path(Operand[] list){
        if (null != list){
            this.clear();

            this.path.apply(list);
        }
        return this;
    }
    public ViewPage2DComponentPath group(Operand[] list){
        if (null != list){
            this.clear();

            this.group.apply(list);
        }
        return this;
    }
    public void reset(){

        this.clear();
        this.path.reset();
        //this.group.reset();
    }
    public void set(path.Path path){

        this.clear();
        this.path.set(path);
    }
    public void add(path.Path path){

        this.clear();
        this.path.add(path);
    }
    /**
     * @return In format of attribute 'd' of SVG Element 'Path'.
     */
    public String toString(){
        return this.path.toString();
    }
}
