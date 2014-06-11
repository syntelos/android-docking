/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class ViewPage3DInputField
    extends ViewPage3DTextLabel
    implements ViewPageComponentInteractive
{

    protected volatile boolean interactive;


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
    protected void input_io(InputScript in){
        if (this.interactive)
            this.interactive = false;
        else
            this.interactive = true;
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
            input_io(in);
            break;
        default:
            input_inter(in);
            break;
        }
        return this.interactive;
    }

}
