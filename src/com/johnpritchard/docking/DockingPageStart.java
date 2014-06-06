/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.llg;

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
        super(new ViewPageComponent[]{
                new DockingButtonIntro(),
                new DockingButtonGame(),
                new DockingButtonHistory(),
                new DockingButtonAbout()
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
    public void input(Input in){

        if (Input.Enter == in){

            switch(enter()){
            case INTRO:
                ((View2D)view).script(Page.intro);
                break;
            case GAME:
                Docking.StartView();
                break;
            case HISTORY:
                ((View2D)view).script(Page.history);
                break;
            case ABOUT:
                ((View2D)view).script(Page.about);
                break;
            }
        }
        else {
            super.input(in);
        }
    }
}
