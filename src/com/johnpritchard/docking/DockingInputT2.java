/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import static android.opengl.GLES10.*;

/**
 * 
 */
public class DockingInputT2
    extends ViewPage3DInputField
{
    private final static String FMT = "T X- %03d";


    public DockingInputT2(double x, double y, double z, double h){
        super(x,y,z,h);

        format(0);
    }


    protected final void format(int t){

        format(FMT,t);
    }
    @Override
    public void draw(){

        glColor4f(0.0f,0.0f,0.0f,1.0f);

        super.draw();
    }
}
