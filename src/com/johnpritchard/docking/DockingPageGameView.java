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
 * @see DockingPageGameCrash
 * @see DockingPageGameDock
 * @see DockingPageGameLost
 * @see DockingPageGameStall
 * @see DockingPageGameHistory
 */
public abstract class DockingPageGameView
    extends DockingPageGameAbstract
{

    protected boolean stale = true;


    protected DockingPageGameView(ViewPage3DComponent[] c){
        super(c);
    }


    @Override
    protected final void focus(){

        stale = true;

        for (ViewPage3DComponent c: this.components){

            if (c instanceof DockingFieldIO){

                c.clearCurrent();
                c.setCurrent();
            }
        }
    }
    @Override
    protected final void navigation(){
    }
    /**
     * Input processor shared by {@link DockingPageGameCrash}, {@link
     * DockingPageGameDock}, {@link DockingPageGameLost}, and {@link
     * DockingPageGameStall} but not {@link DockingPageGameHistory}
     */
    @Override
    public void input(InputScript in){

        Input type = in.type();
        switch(type){
        case Up:
            break;
        case Left:

            view.script(new InputScript[]{
                    new InputScript.Database(InputScript.Database.Op.GameEndPrev)
                });

            break;
        case Down:
            break;
        case Right:

            view.script(new InputScript[]{
                    new InputScript.Database(InputScript.Database.Op.GameEndNext)
                });

            break;
        case Enter:
            view.script(Page.start);
            break;
        }
    }
}
