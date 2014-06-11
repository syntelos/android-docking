/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import static android.opengl.GLES10.*;

/**
 * 
 */
public abstract class DockingInputT
    extends ViewPage3DInputField
{

    protected volatile int value;


    public DockingInputT(double x, double y, double z, double h){
        super(x,y,z,h);

        this.selection = new ViewPage3DTextSelection(5,z);

        format();
    }


    protected abstract void format();

    @Override
    public void draw(){

        glColor4f(0.0f,0.0f,0.0f,1.0f);

        if (interactive){

            this.selection.blink(500L);
        }
        else if (current){

            this.selection.unblink();
        }

        super.draw();
    }
}
