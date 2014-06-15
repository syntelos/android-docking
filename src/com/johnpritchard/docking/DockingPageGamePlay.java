/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.opengl.GLES10;
import static android.opengl.GLES10.*;
import android.opengl.GLU;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 
 */
public final class DockingPageGamePlay
    extends DockingPageGameAbstract
{

    public final static DockingPageGamePlay Instance = new DockingPageGamePlay();


    private boolean stale = true;


    private DockingPageGamePlay(){
        super(new ViewPage3DComponent[]{
                in_xp0, in_xm0, in_xp1, in_xm1
            });
    }


    @Override
    public String name(){
        return Page.gameplay.name();
    }
    @Override
    public Page value(){
        return Page.gameplay;
    }
    public void draw(GL10 gl){

        if (stale){
            stale = false;

            glClearColor(1.0f,1.0f,1.0f,1.0f);

            glEnable(GL_LIGHT0);
            glEnable(GL_LIGHT1);
            glEnable(GL_LIGHTING);
            glEnable(GL_COLOR_MATERIAL);
        }
        else {

            glClear(CLR);

            model();

            glColor4f(0.0f,0.0f,0.0f,1.0f);

            draw();

            glFlush();
        }
    }
    @Override
    protected void draw(){

        out_vx.draw();

        out_ax.draw();

        out_t.draw();

        out_rx.draw();

        out_tr.draw();

        out_lv.draw();

        out_m.draw();

        out_t10.draw();

        out_t01.draw();

        super.draw();
    }
    @Override
    public ViewPage up(View view, int w, int h){
        super.up(view,w,h);

        stale = true;

        DockingPhysics.Start();

        return this;
    }
    @Override
    public void down(){
        super.down();

        DockingPhysics.Stop();
    }
    @Override
    protected void focus(){

        // stale = true;

        for (ViewPage3DComponent c: this.components){

            if (c instanceof DockingFieldIO){

                c.clearCurrent();
            }
        }
    }
}
