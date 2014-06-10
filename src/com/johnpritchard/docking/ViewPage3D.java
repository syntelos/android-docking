/*
 * Copyright (C) 2014, John Pritchard
 */
package com.johnpritchard.docking;

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


    private float lx, ly;


    protected ViewPage3D(){
        super();
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
