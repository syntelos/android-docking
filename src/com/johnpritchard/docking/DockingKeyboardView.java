/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.Keyboard.Key;
import android.util.AttributeSet;

/**
 * 
 */
public class DockingKeyboardView
    extends KeyboardView
{


    public DockingKeyboardView(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    public DockingKeyboardView(Context context, AttributeSet attrs, int style){
        super(context,attrs,style);
    }

}
