/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 
 */
public final class DockingPageStart
    extends ViewPage2D
{
    public final static DockingPageStart Instance = new DockingPageStart();


    private final static int INTRO     = 0;
    private final static int GAME      = 1;
    private final static int HISTORY   = 2;
    private final static int ABOUT     = 3;


    private DockingPageStart(){
        super(new ViewPage2DComponent[]{
                new ViewPage2DButtonGroup("Intro"),
                new ViewPage2DButtonGroup("Game"),
                new ViewPage2DButtonGroup("History"),
                new ViewPage2DButtonGroup("About")
            });
    }

    @Override
    protected void init(){

        group_vertical();
    }
    @Override
    protected int first(){

        return GAME;
    }
    @Override
    public String name(){
        return Page.start.name();
    }
    @Override
    public Page value(){
        return Page.start;
    }
    @Override
    public void input(InputScript in){

        if (Input.Enter == in){

            switch(enter()){
            case INTRO:
                view.script(Page.intro);
                break;
            case GAME:
                view.script(Page.game);
                break;
            case HISTORY:
                view.script(Page.history);
                break;
            case ABOUT:
                view.script(Page.about);
                break;
            }
        }
        else {
            super.input(in);
        }
    }
}
