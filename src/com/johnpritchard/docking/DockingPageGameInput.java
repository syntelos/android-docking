/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

import android.opengl.GLES10;
import static android.opengl.GLES10.*;
import android.opengl.GLU;
import android.opengl.Matrix;

import javax.microedition.khronos.opengles.GL10;

/**
 * 
 */
public final class DockingPageGameInput
    extends DockingPageGameAbstract
{
    public final static DockingPageGameInput Instance = new DockingPageGameInput();



    private DockingPageGameInput(){
        super();
    }


    @Override
    public String name(){
        return Page.gameInput.name();
    }
    @Override
    public Page value(){
        return Page.gameInput;
    }
    public void draw(GL10 gl){

        if (stale){
            init(gl);
        }
        else {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glColor4f(0.0f,0.0f,0.0f,1.0f);

            sv0.draw();
            sv1.draw();
            sv2.draw();

            glFlush();
        }
    }
    @Override
    public void input(InputScript in){

        info(in.toString());

        Input type = in.type();

        if (Input.Key == type){

            InputScript.Key kin = (InputScript.Key)in;

            sv1.format("Fx %s",View3DInputEditable.Instance.toString());
        }
        else if (Input.Enter == type){

            String string = View3DInputEditable.Instance.toString();
            try {
                float opd = Float.parseFloat(string);

                sv1.format("Fx %s",Format7(opd));

                DockingPhysics.Script(new PhysicsScript(PhysicsOperator.FX,opd));
            }
            catch (NumberFormatException exc){

                sv1.setText("Fx");
            }
            View3DInputEditable.Instance.clear();

            Docking.LowerKeyboard();

            view.script(Page.gameView);
        }
        else {
            sv1.setText("Fx");

            View3DInputEditable.Instance.clear();

            super.input(in);
        }
    }
    @Override
    protected void focus(){

        info("focus");

        View3DInputEditable.Instance.clear();

        Docking.RaiseKeyboard();
    }
}
