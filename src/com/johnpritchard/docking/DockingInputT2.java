/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import static android.opengl.GLES10.*;

/**
 * 
 */
public class DockingInputT2
    extends DockingInputT
{
    private final static String FMT = "T X- %03d";


    public DockingInputT2(double x, double y, double z, double h){
        super(x,y,z,h);
    }


    protected final void format(){

        format(FMT,value);
    }
    protected void input_edit(Input in){
        value = 0;
        format();
    }
    protected void input_value(Input in){
        if (Input.Up == in){

            if (999 > value){

                value += 1;
            }
        }
        else if (0 < value){

            value -= 1;
        }
        format();
    }
    protected void input_io(Input in){

        if (interactive && 0 < value){

            float svalue = -value;

            DockingPhysics.Script(new PhysicsScript(PhysicsOperator.TX,svalue));
        }
        super.input_io(in);
    }
}
