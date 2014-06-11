/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import static android.opengl.GLES10.*;

/**
 * 
 */
public final class DockingFieldIO
    extends ViewPage3DInputField
{

    public final PhysicsOperator op;

    public final PhysicsDOF dof;

    private final String fmt;

    protected volatile int editable;

    protected volatile boolean sent;


    public DockingFieldIO(PhysicsOperator op, PhysicsDOF dof,
                          double x, double y, double z, double h)
    {
        super(x,y,z,h);

        this.op = op;
        this.dof = dof;

        this.fmt = op.label+' '+dof.label+' '+op.format;

        this.selection = new ViewPage3DTextSelection((op.llen+dof.llen+2),z);

        format();
    }


    protected final void format(){

        format(fmt,editable);
    }
    protected final void format(float seconds){

        if (sent){

            editable = (int)seconds;

            format();
        }
        else if (current || interactive){

            return;
        }
        else {

            editable = (int)seconds;

            format();
        }
    }
    protected void input_edit(Input in){
        editable = 0;
        format();
    }
    protected void input_edit(InputScript.Key in){
        editable = 0;
        format();
    }
    protected void input_value(Input in){
        if (Input.Up == in){

            if (999 > editable){

                editable += 1;
            }
        }
        else if (0 < editable){

            editable -= 1;
        }
        format();
    }
    protected void input_io(Input in){

        if (interactive){

            if (0 < editable){

                DockingPhysics.Script(new PhysicsScript(op,dof,editable));

                sent = true;
            }
            else {
                sent = false;
            }
        }
        else {
            editable = 0;
            sent = false;
            format();
        }
        super.input_io(in);
    }
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
