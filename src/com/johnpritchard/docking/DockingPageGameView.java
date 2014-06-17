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
public final class DockingPageGameView
    extends DockingPageGameAbstract
{

    public final static DockingPageGameView Instance = new DockingPageGameView();


    private boolean stale = true;


    private DockingPageGameView(){
        super(new ViewPage3DComponent[]{
                in_xp0, in_xm0, in_xp1, in_xm1
            });
    }


    @Override
    public String name(){
        return Page.gameview.name();
    }
    @Override
    public Page value(){
        return Page.gameview;
    }
    public void draw(GL10 gl){

        if (stale){
            stale = false;

            glClearColor(1.0f,1.0f,1.0f,1.0f);
        }
        else {

            glClear(CLR);

            glColor4f(0.0f,0.0f,0.0f,1.0f);

            draw();

            glFlush();
        }
    }
    @Override
    protected void draw(){

        out_score.draw();
        out_identifier.draw();
        out_created.draw();
        out_completed.draw();

        super.draw();
    }
    @Override
    protected void focus(){

        stale = true;

        for (ViewPage3DComponent c: this.components){

            if (c instanceof DockingFieldIO){

                c.clearCurrent();
                c.setCurrent();
            }
        }
    }
    @Override
    protected void navigation(){
    }
    @Override
    public void input(InputScript in){

        Input type = in.type();
        switch(type){
        case Up:
            break;
        case Left:
            /*
             * pull from right: ascend history order
             */
            Docking.Post3D(new DockingPostNextHistory());

            break;
        case Down:
            break;
        case Right:
            /*
             * pull from left: descend history order
             */
            Docking.Post3D(new DockingPostPrevHistory());

            break;
        case Enter:
            view.script(Page.start);
            break;
        }
    }
}
