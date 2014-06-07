/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 
 */
public final class DockingPageGameInput
    extends ViewPage2D
{
    public final static DockingPageGameInput Instance = new DockingPageGameInput();


    private final static int INTRO     = 0;



    private DockingPageGameInput(){
        super(new ViewPage2DComponent[]{
                new ViewPage2DButtonGroup(""),
                new ViewPage2DButtonGroup("1"),
                new ViewPage2DButtonGroup("2"),
                new ViewPage2DButtonGroup("3"),
                new ViewPage2DButtonGroup("4"),
                new ViewPage2DButtonGroup("5"),
                new ViewPage2DButtonGroup("6"),
                new ViewPage2DButtonGroup("7"),
                new ViewPage2DButtonGroup("8"),
                new ViewPage2DButtonGroup("9"),
                new ViewPage2DButtonGroup("."),
                new ViewPage2DButtonGroup("Enter")
            });
    }

    @Override
    protected void init(){

        group_vertical();
    }
    @Override
    protected int first(){

        return 0;
    }
    @Override
    public String name(){
        return Page.gameInput.name();
    }
    @Override
    public Page value(){
        return Page.gameInput;
    }
    @Override
    public void input(Input in){

        if (Input.Enter == in){

            switch(enter()){
            case INTRO:

                break;
            }
        }
        else {
            super.input(in);
        }
    }
}
