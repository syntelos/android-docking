/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import static android.opengl.GLES10.*;

/**
 * 
 */
public class DockingInputT1
    extends ViewPage3DInputField
{
    private final static String FMT = "T X+ %03d";


    public DockingInputT1(double x, double y, double z, double h){
        super(x,y,z,h);

        format(0);
    }


    protected final void format(int t){

        format(FMT,t);
    }
    @Override
    public void draw(){

        if (interactive){

            glColor4f(0.5f,0.0f,0.0f,1.0f);
        }
        else if (current){

            glColor4f(0.5f,0.5f,0.0f,1.0f);
        }
        else {

            glColor4f(0.0f,0.0f,0.0f,1.0f);
        }
        super.draw();
    }
}
