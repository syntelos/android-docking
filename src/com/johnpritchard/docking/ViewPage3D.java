/*
 * Copyright (C) 2014, John Pritchard
 */
package com.johnpritchard.docking;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import static android.opengl.GLES10.*;
import android.view.InputDevice;
import android.view.MotionEvent;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * GL program (lifecycle: up, init, draw many, down).
 * 
 * The GL program should employ the camera viewport from the IP/IR
 * feature set.  The GL viewport employs the camera width and height
 * when the camera display area fills the GL display area.
 * 
 * This feature set may be employed for the alignment of layers over
 * an actual camera preview, or in any application to the registration
 * of layers of surface views.
 */
public abstract class ViewPage3D
    extends ViewPage
{
    protected final static int bpf = 4;

    protected final static java.nio.ByteOrder nativeOrder = java.nio.ByteOrder.nativeOrder();


    /**
     * Relative navigation and visualization for {@link InputScript}
     * events via {@link ViewAnimation}.
     */
    protected final ViewPage3DComponent[] components;

    protected volatile ViewPage3DComponent current;

    protected volatile ViewPageComponentInteractive interactive;

    private float lx, ly;


    protected ViewPage3D(){
        this(new ViewPage3DComponent[0]);
    }
    protected ViewPage3D(ViewPage3DComponent[] components){
        super();
        if (null == components){

            this.components = new ViewPage3DComponent[0];
        }
        else {
            this.components = components;
        }
        info("init");
        init();
    }


    /**
     * Set stale to false, and then perform GL init process.
     */
    public abstract void init(GL10 gl);
    /**
     * Process if ready: if stale call init otherwise the normal
     * rendering process.
     */
    public abstract void draw(GL10 gl);

    public abstract void physicsUpdate();

    public final void draw(Canvas g){
    }
    @Override
    public void down(SharedPreferences.Editor preferences){

        this.down();

        preferences.putInt(name()+".focus",this.current());
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

                            final float x = xy[0];
                            final float y = xy[1];

                            if (0.0f != this.lx || 0.0f != this.ly){

                                final float dx = (lx-x)/10f;
                                final float dy = (ly-y)/10f;

                                lx = x;
                                ly = y;
                                /*
                                 * Relative coordinate space [LANDSCAPE]
                                 */
                                if ( 0.0f != Z(dx) || 0.0f != Z(dy)){

                                    if (Math.abs(dx) > Math.abs(dy)){

                                        if (0.0f < dx){

                                            return new InputScript[]{Input.Left};
                                        }
                                        else {
                                            return new InputScript[]{Input.Right};
                                        }
                                    }
                                    else if (0.0f < dy){

                                        return new InputScript[]{Input.Up};
                                    }
                                    else {
                                        return new InputScript[]{Input.Down};
                                    }
                                }
                                else {
                                    return new InputScript[]{Input.Enter};
                                }
                            }
                            else {
                                lx = x;
                                ly = y;
                            }
                        }
                        return null;
                    }
                default:
                    break;
                }
            }
            else {
                /*
                 */
                final int px = event.getActionIndex();
                final float dx = event.getX(px);
                final float dy = event.getY(px);
                /*
                 * Relative coordinate space [LANDSCAPE]
                 */
                if ( 0.0f != Z(dx) || 0.0f != Z(dy)){

                    if (Math.abs(dx) > Math.abs(dy)){

                        if (0.0f < dx){

                            return new InputScript[]{Input.Left};
                        }
                        else {
                            return new InputScript[]{Input.Right};
                        }
                    }
                    else if (0.0f < dy){

                        return new InputScript[]{Input.Up};
                    }
                    else {
                        return new InputScript[]{Input.Down};
                    }
                }
                else {
                    return new InputScript[]{Input.Enter};
                }
            }
        }
        return null;
    }
    /**
     * Convert navigation activity to navigational focus status.
     * 
     * The back button or input script should always have the same
     * effect as on devices where the back button operates directly on
     * the activity stack (without passing through the View key event
     * process).
     */
    @Override
    public void input(InputScript event){

        Input in = event.type();

        if (Input.Back == in){
            /*
             * Special case: the "view.script(page)" would return here
             * without preserving the back button requirement.
             */
            view.pageTo(Page.start);
        }
        else if (in.geometric){

            final ViewPageComponentInteractive interactive = this.interactive;

            if (null != interactive && interactive.interactive()){

                info("interactive "+event);

                interactive.input(event);
            }
            else {

                ViewPage3DComponent current = this.current;

                if (null != current){

                    info("navigation "+event);

                    current(current.getCardinal(in));
                }
                else {
                    current = this.current(in);

                    if (null != current){

                        info("navigation "+event);

                        current(current);
                    }
                }
            }
        }
        else {
            super.input(event);
        }
    }
    /**
     * @see #first()
     */
    protected boolean navigationInclude(int index, ViewPage3DComponent c){
        return true;
    }
    /**
     * Initialize navigation
     */
    @Override
    protected final void navigation(){
        for (int cc = 0, count = components.length; cc < count; cc++){
            ViewPage3DComponent c = components[cc];

            c.clearCardinals();

            if (navigationInclude(cc,c)){

                final float cx = c.getX();
                final float cy = c.getY();

                for (int bb = 0; bb < count; bb++){
                    if (bb != cc){
                        ViewPage3DComponent b = components[bb];

                        if (navigationInclude(bb,b)){

                            final Input dir = c.direction(b);

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
        final int count = components.length;

        if (0 != count){
            final SharedPreferences preferences = preferences();
            final int first = first();
            final int focus = preferences.getInt(name()+".focus",first);

            if (-1 < focus && focus < count){

                current(components[focus]);
            }
            else if (-1 < first && first < count){

                current(components[first]);
            }

            final ViewPage3DComponent current = this.current;

            for (ViewPage3DComponent c : components){

                if (c == current){
                    c.setCurrent();
                }
                else {
                    c.clearCurrent();
                }
            }
        }
    }
    /**
     * Call draw on each component
     */
    protected void draw(){

        for (ViewPage3DComponent c: this.components){

            c.draw();
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
        ViewPage3DComponent current = this.current;
        if (null != current){
            ViewPage3DComponent[] components = this.components;

            final int count = components.length;
            for (int cc = 0; cc < count; cc++){

                if (current == components[cc]){

                    return cc;
                }
            }
        }
        return -1;
    }
    protected ViewPage3DComponent current(Input in){

        final ViewPage3DComponent current = this.current;

        if (null != current){

            return current;
        }
        else {
            final int count = this.components.length;
            if (0 < count){
                if (Input.Down == in){
                    return this.components[0];
                }
                else {
                    return this.components[count-1];
                }
            }
            else {
                return null;
            }
        }
    }
    protected void current(ViewPage3DComponent next){

        final ViewPage3DComponent prev = this.current;

        if (null != next && next != prev){

            if (null != prev){
                prev.clearCurrent();
            }

            this.current = next;

            next.setCurrent();

            if (next instanceof ViewPageComponentInteractive){

                this.interactive = (ViewPageComponentInteractive)next;
            }
            else {
                this.interactive = null;
            }
        }
        else {
            if (null != prev){
                prev.clearCurrent();
            }
            this.current = null;
            this.interactive = null;
        }
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

                ViewPage3DComponent c = components[cc];

                int c_score = c.countCardinals();

                if (c_score >= first_score){

                    first = cc;
                    first_score = c_score;
                }
            }
        }
        return first;
    }
    /*
     * glu method for static GL interface (GLES10)
     */
    protected static void gluPerspective(float fovy, float aspect, float zNear, float zFar){

        float top = zNear * (float) Math.tan(fovy * (Math.PI / 360.0));
        float bottom = -top;
        float left = bottom * aspect;
        float right = top * aspect;
        glFrustumf(left, right, bottom, top, zNear, zFar);
    }
    /*
     * Absolute put for coherency in the race condition: conflicts
     * with the GPU are limited to the changes in values within the
     * buffer.
     */
    protected final static void MatrixCopy(float[] m, FloatBuffer b){

        for (int i = 0; i < 16; i++){

            b.put(i,m[i]);
        }
    }
    protected final static void MatrixTranspose(float[] m, FloatBuffer b){

        for (int i = 0, i4 = 0; i < 4; i++, i4 += 4){

            b.put( i     , m[i4]);
            b.put( i +  4, m[i4 + 1]);
            b.put( i +  8, m[i4 + 2]);
            b.put( i + 12, m[i4 + 3]);
        }
    }
}
