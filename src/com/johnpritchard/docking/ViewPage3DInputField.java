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
    protected void input_edit(Input in){
    }
    protected void input_edit(InputScript.Key in){
    }
    protected void input_value(Input in){
    }
    protected void input_io(Input in){
        if (this.interactive)
            this.interactive = false;
        else
            this.interactive = true;
    }
    protected void input_inter(InputScript in){
    }
    public final boolean input(InputScript in){
        Input type = in.type();
        switch(type){
        case Left:
        case Right:
            input_edit(type);
            break;
        case Key:
            input_edit((InputScript.Key)in);
            break;
        case Up:
        case Down:
            input_value(type);
            break;
        case Enter:
            input_io(type);
            break;
        default:
            input_inter(in);
            break;
        }
        return this.interactive;
    }
    @Override
    public final void clearCurrent(){
        super.clearCurrent();

        this.interactive = false;
    }
}
