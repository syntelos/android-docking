/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.llg;

/**
 * 
 */
public enum Page {
    start    (DockingPageStart.Instance),
    intro    (DockingPageIntro.Instance),
    game     (DockingPageGame.Instance),
    history  (DockingPageHistory.Instance),
    about    (DockingPageAbout.Instance);


    public final ViewPage page;

    private Page(ViewPage page){
        this.page = page;
    }

}
