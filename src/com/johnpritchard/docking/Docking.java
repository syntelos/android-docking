/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.Intent;

/**
 * 
 */
public class Docking
    extends android.app.Application
{
    private static DockingActivityMain Main;
    private static DockingActivityView View;
    private static DockingKeyboard Keyboard;

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
     * Called from view to raise keyboard
     */
    public final static void RaiseKeyboard(){
        Intent intent = new Intent();
        intent.setClass(View, DockingKeyboard.class);

        View.startActivity(intent);
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
    /**
     * Called from keyboard
     */
    protected final static void KeyboardActivate(DockingKeyboard instance){
        Keyboard = instance;
    }


    public Docking(){
        super();
    }

}
