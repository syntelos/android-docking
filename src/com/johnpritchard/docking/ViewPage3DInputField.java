/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class ViewPage3DInputField
    extends View3DTextLabel
    implements ViewPageComponentInteractive
{

    private volatile boolean interactive;


    public ViewPage3DInputField(double x, double y, double z, double h){
        super(x,y,z,h);
    }


    public final boolean interactive(){
        return this.interactive;
    }
    protected void input_edit(InputScript in){
    }
    protected void input_value(InputScript in){
    }
    protected void input_close(InputScript in){
        this.interactive = false;
    }
    protected void input_inter(InputScript in){
    }
    public boolean input(InputScript in){
        Input type = in.type();
        switch(type){
        case Left:
        case Right:
        case Key:
            input_edit(in);
            break;
        case Up:
        case Down:
            input_value(in);
            break;
        case Enter:
            input_close(in);
            break;
        default:
            input_inter(in);
            break;
        }
        return this.interactive;
    }

}
