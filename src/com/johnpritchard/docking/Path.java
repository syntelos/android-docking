/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import path.Op;
import path.Operand;
import path.Parser;
import path.Winding;

import android.graphics.RectF;

/**
 * Extended path feature set for reproducible construction and layout
 * algorithms: rounding and margin can be set to create derived shapes
 * from a generic call to "set".
 */
public class Path
    extends android.graphics.Path
    implements path.Path
{

    protected boolean rounding;

    protected float margin = 0.0f;


    public Path(){
        super();
    }


    public float margin(){

        return this.margin;
    }
    public Path margin(float margin){

        if (0.0f <= margin){

            this.margin = margin;
        }
        else {
            throw new IllegalArgumentException(String.valueOf(margin));
        }
        return this;
    }
    public boolean rounding(){

        return this.rounding;
    }
    public Path rounding(boolean rounding){

        this.rounding = rounding;

        return this;
    }
    /**
     * Use margin and rounding to define path
     */
    public Path set(RectF s){

        if (this.rounding){

            return this.rrect(s,this.margin);
        }
        else {

            return this.rect(s,this.margin);
        }
    }
    /**
     * Use margin to define rect
     */
    public Path rect(RectF s){

        return this.rect(s,this.margin);
    }
    /**
     * Define rect with margin from the argument
     */
    public Path rect(RectF s, float margin){
        this.reset();

        super.addRect((s.left-margin),(s.top-margin),
                      (s.right+margin),(s.bottom+margin),
                      Direction.CCW);
        return this;
    }
    /**
     * Define round rect with default margin from the argument
     */
    public Path rrect(RectF r){

        return this.rrect(r,this.margin);
    }
    /**
     * Define round rect with margin from the argument
     */
    public Path rrect(RectF r, float margin){

        this.reset();

        final float x0 = r.left-margin;
        final float y0 = r.top-margin;
        final float x1 = r.right+margin;
        final float y1 = r.bottom+margin;

        final RectF rr = new RectF(x0,y0,x1,y1);

        super.addRoundRect(rr,margin,margin,Direction.CCW);

        return this;
    }
    /**
     * Define clip with default margin
     */
    public Path circle(RectF r){

        return this.circle(r,this.margin);
    }
    public Path circle(RectF r, float margin){

        final float x0 = r.left-margin;
        final float y0 = r.top-margin;
        final float x1 = r.right+margin;
        final float y1 = r.bottom+margin;
        final float cx = (x0+x1)/2.0f;
        final float cy = (y0+y1)/2.0f;
        
        final float diam = Math.min((x1-x0),(y1-y0));
        final float radius = (diam/2.0f);

        return this.circle(cx,cy,radius);
    }
    /**
     * Define clip literal
     */
    public Path circle(float cx, float cy, float r){
        this.reset();

        super.addCircle(cx,cy,r,Direction.CCW);
        return this;
    }
    public Path incCapacity(int delta){
        super.incReserve(delta);
        return this;
    }
    public Path plainFillType(){
        switch(super.getFillType()){
        case INVERSE_WINDING:
            super.setFillType(Path.FillType.WINDING);
            break;
        case INVERSE_EVEN_ODD:
            super.setFillType(Path.FillType.EVEN_ODD);
            break;
        default:
            break;
        }
        return this;
    }
    public Path inverseFillType(){
        switch(super.getFillType()){
        case WINDING:
            super.setFillType(Path.FillType.INVERSE_WINDING);
            break;
        case EVEN_ODD:
            super.setFillType(Path.FillType.INVERSE_EVEN_ODD);
            break;
        default:
            break;
        }
        return this;
    }
    public Path rotateFillType(){
        switch(super.getFillType()){
        case WINDING:
            super.setFillType(Path.FillType.EVEN_ODD);
            break;
        case EVEN_ODD:
            super.setFillType(Path.FillType.INVERSE_WINDING);
            break;
        case INVERSE_WINDING:
            super.setFillType(Path.FillType.INVERSE_EVEN_ODD);
            break;
        default:
            super.setFillType(Path.FillType.WINDING);
            break;
        }
        return this;
    }
    public java.lang.Iterable<Operand> toPathIterable(){
        throw new UnsupportedOperationException();
    }
    public java.util.Iterator<Operand> toPathIterator(){
        throw new UnsupportedOperationException();
    }
    public path.Path setWinding(Winding winding){
        switch(winding){
        case NonZero:
            super.setFillType(Path.FillType.WINDING);
            break;
        case EvenOdd:
            super.setFillType(Path.FillType.EVEN_ODD);
            break;
        default:
            break;
        }
        return this;
    }
    public Winding getWinding(){
        Path.FillType ft = super.getFillType();
        switch(ft){
        case WINDING:
        case INVERSE_WINDING:
            return Winding.NonZero;
        case EVEN_ODD:
        case INVERSE_EVEN_ODD:
            return Winding.EvenOdd;
        default:
            return Winding.Future;
        }
    }
    public boolean isWindingNonZero(){
        return (Winding.NonZero == this.getWinding());
    }
    public boolean isWindingEvenOdd(){
        return (Winding.EvenOdd == this.getWinding());
    }
    public path.Path setWindingNonZero(){
        return this.setWinding(Winding.NonZero);
    }
    public path.Path setWindingEvenOdd(){
        return this.setWinding(Winding.EvenOdd);
    }
    public float[] getVerticesPath(int index, Op op, float[] vertices){
        throw new UnsupportedOperationException();
    }
    public void add(Op op, float[] operands){
        switch(op){
        case MoveTo:
            this.moveTo(operands);
            break;
        case LineTo:
            this.lineTo(operands);
            break;
        case QuadTo:
            this.quadTo(operands);
            break;
        case CubicTo:
            this.cubicTo(operands);
            break;
        case Close:
            this.close();
            break;
        }
    }
    public void moveTo(float[] operands){

        this.moveTo(operands[0],operands[1]);
    }
    public void lineTo(float[] operands){

        this.lineTo(operands[0],operands[1]);
    }
    public void quadTo(float[] operands){
        switch(operands.length){
        case 4:
            this.quadTo(operands[0],operands[1],operands[2],operands[3]);
            break;
        case 6:
            this.quadTo(operands[0],operands[1],operands[3],operands[4]);
            break;
        default:
            throw new IllegalArgumentException(String.valueOf(operands.length));
        }
    }
    public void cubicTo(float[] operands){
        switch(operands.length){
        case 6:
            this.cubicTo(operands[0],operands[1],operands[2],operands[3],operands[4],operands[5]);
            break;
        case 9:
            this.cubicTo(operands[0],operands[1],operands[3],operands[4],operands[6],operands[7]);
            break;
        default:
            throw new IllegalArgumentException(String.valueOf(operands.length));
        }
    }
    public path.Path apply(String pexpr){

        return this.apply(new Parser(pexpr));
    }
    public path.Path apply(Parser p){

        this.reset();

        return Parser.Apply(this,p);
    }
    public Path apply(Operand[] list){
        if (null != list){
            this.reset();
            this.incCapacity(list.length);

            for (Operand op: list){
                this.add(op.op,op.vertices);
            }
        }
        return this;
    }
    public void set(Path path){
        this.reset();
        this.margin = path.margin;
        this.rounding = path.rounding;
    }
    public void set(path.Path path){
        this.reset();
        this.add(path);
    }
    public void add(Path path){

        super.addPath(path);
    }
    public void add(path.Path path){

        for (Operand operand: path.toPathIterable()){
            switch(operand.op){
            case MoveTo:
                this.moveTo(operand.vertices);
                break;
            case LineTo:
                this.lineTo(operand.vertices);
                break;
            case QuadTo:
                this.quadTo(operand.vertices);
                break;
            case CubicTo:
                this.cubicTo(operand.vertices);
                break;
            case Close:
                this.close();
                break;
            }
        }
    }
    /**
     * @return In format of attribute 'd' of SVG Element 'Path'.
     */
    public String toString(){
        throw new UnsupportedOperationException();
    }
}
