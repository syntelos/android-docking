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
    gameplay  (DockingPageGamePlay.Instance),
    gamecrash (DockingPageGameCrash.Instance),
    gameview  (DockingPageGameView.Instance),
    nohistory (DockingPageNoHistory.Instance),
    about     (DockingPageAbout.Instance);


    public final ViewPage page;

    private Page(ViewPage page){
        this.page = page;
    }

}
