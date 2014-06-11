/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public enum Page {
    start     (DockingPageStart.Instance),
    intro     (DockingPageIntro.Instance),
    gameview  (DockingPageGameView.Instance),
    gamecrash (DockingPageGameCrash.Instance),
    history   (DockingPageHistory.Instance),
    about     (DockingPageAbout.Instance);


    public final ViewPage page;

    private Page(ViewPage page){
        this.page = page;
    }

}
