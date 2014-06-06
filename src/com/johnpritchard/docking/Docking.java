/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.llg;

import android.content.Intent;

/**
 * 
 */
public class Docking
    extends android.app.Application
{
    private static DockingActivityMain Main;
    private static DockingActivityView View;

    /**
     * Called from main to start view.
     */
    public final static void StartView(){
        Intent intent = new Intent();
        intent.setClass(Main, DockingActivityView.class);

        Main.startActivity(intent);
    }
    /**
     * Called from view to start main.
     */
    public final static void StartMain(){
        Intent intent = new Intent();
        intent.setClass(View, DockingActivityMain.class);

        View.startActivity(intent);
        View.finish();
    }
    /**
     * Called from main
     */
    protected final static void MainActivate(DockingActivityMain instance){
        Main = instance;
    }
    /**
     * Called from view
     */
    protected final static void ViewActivate(DockingActivityView instance){
        View = instance;
    }


    public Docking(){
        super();
    }

}
