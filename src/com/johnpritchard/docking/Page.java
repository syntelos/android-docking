/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public enum Page {
    start        (DockingPageStart.Instance,        false),
    intro        (DockingPageIntro.Instance,        true),
    intro1       (DockingPageIntro1.Instance,       true),
    intro2       (DockingPageIntro2.Instance,       true),
    intro3       (DockingPageIntro3.Instance,       true),
    intro4       (DockingPageIntro4.Instance,       true),
    intro5       (DockingPageIntro5.Instance,       true),
    intro6       (DockingPageIntro6.Instance,       true),
    gameplay     (DockingPageGamePlay.Instance,     true),
    gamecrash    (DockingPageGameCrash.Instance,    true),
    gameview     (DockingPageGameView.Instance,     true),
    gamemodel    (DockingPageGameModel.Instance,    true),
    gamehardware (DockingPageGameHardware.Instance, true),
    nohistory    (DockingPageNoHistory.Instance,    true),
    about        (DockingPageAbout.Instance,        true);


    public final ViewPage page;

    public final boolean simpleInput;


    private Page(ViewPage page, boolean simpleInput){
        this.page = page;
        this.simpleInput = simpleInput;
    }

}
