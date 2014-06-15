/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 
 */
public class DockingPageAbout
    extends ViewPage2D
{
    public final static String AboutString = "Docking for Android version "+DockingVersion.Name+'\n'+
                                             "Copyright (C) 2014 John Pritchard."+'\n'+
                                             "All rights reserved.";

    protected final static ViewPage2DTextMultiline textA = new ViewPage2DTextMultiline(AboutString);

    protected final static ViewPage2DTextLabel textH = new ViewPage2DTextLabel("Record not found");

    protected final static ViewPage2DTextMultiline text0 = new ViewPage2DTextMultiline("Rendezvous in zero gravity using thrusters T10 and T01.");

    protected final static ViewPage2DTextMultiline text1 = new ViewPage2DTextMultiline("Use up, down, left, right and enter actions to select, modify, and execute thruster control.");

    protected final static ViewPage2DTextMultiline text2 = new ViewPage2DTextMultiline("Use thrusters designated X- to reduce velocity.");

    protected final static ViewPage2DTextMultiline text3 = new ViewPage2DTextMultiline("Velocity is meters per second.");

    protected final static ViewPage2DTextMultiline text4 = new ViewPage2DTextMultiline("Distance to docking is meters.");

    protected final static ViewPage2DTextMultiline text5 = new ViewPage2DTextMultiline("Thruster control is represented in time seconds.");

    protected final static ViewPage2DTextMultiline text6 = new ViewPage2DTextMultiline("Final docking velocity should be no more than one centimeter per second: 0.010 m/s.");

    static {
        RectF g = new RectF();
        {
            g.union(textA.bounds());
            g.union(textH.bounds());
            g.union(text0.bounds());
            g.union(text1.bounds());
            g.union(text2.bounds());
            g.union(text3.bounds());
            g.union(text4.bounds());
            g.union(text5.bounds());
            g.union(text6.bounds());
        }
        float p = pad(g);
        {
            textA.group(g,p);
            textH.group(g,p);
            text0.group(g,p);
            text1.group(g,p);
            text2.group(g,p);
            text3.group(g,p);
            text4.group(g,p);
            text5.group(g,p);
            text6.group(g,p);
        }
    }


    public final static DockingPageAbout Instance = new DockingPageAbout();



    private DockingPageAbout(){
        super(new ViewPage2DComponent[]{
                textA
            });
    }
    protected DockingPageAbout(ViewPage2DComponent[] c){
        super(c);
    }


    @Override
    public String name(){
        return Page.about.name();
    }
    @Override
    public Page value(){
        return Page.about;
    }
    @Override
    public void input(InputScript in){

        switch(in.type()){
        case Left:
        case Right:
        case Enter:
            view.script(Page.start);
            break;
        }
    }
}
