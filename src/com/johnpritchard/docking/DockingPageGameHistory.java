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
 * Score view available via Page Start History.
 */
public final class DockingPageGameHistory
    extends DockingPageGameView
{

    public final static DockingPageGameHistory Instance = new DockingPageGameHistory();


    private DockingPageGameHistory(){
        super(new ViewPage3DComponent[]{
                in_xp0, in_xm0, in_xp1, in_xm1
            });
    }


    @Override
    public String name(){
        return Page.gamehistory.name();
    }
    @Override
    public Page value(){
        return Page.gamehistory;
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
    public final void input(InputScript in){

        Input type = in.type();
        switch(type){
        case Up:
            break;
        case Left:
            /*
             */
            Docking.Post3D(new DockingPostPrevHistory());

            break;
        case Down:
            break;
        case Right:
            /*
             */
            Docking.Post3D(new DockingPostNextHistory());

            break;
        case Enter:
            view.script(Page.start);
            break;
        }
    }
}
