/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.method.MetaKeyKeyListener;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

/**
 * 
 */
public class DockingKeyboard
    extends InputMethodService
{

    private Keyboard keyboard;

    private int width;

    private KeyboardView keyboardView;


    public DockingKeyboard(){
        super();
    }


    @Override
    public void onInitializeInterface(){

        Docking.KeyboardActivate(this);

        if (null != keyboard){
            int w = getMaxWidth();
            if (w != width){
                width = w;
            }
            else {
                return;
            }
        }
        keyboard = new Keyboard(this,R.xml.input);
    }
    @Override
    public View onCreateInputView(){

        keyboardView = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard,
                                                                 null);
        keyboardView.setKeyboard(keyboard);
        return keyboardView;
    }
    // @Override
    // public void onStartInput(EditorInfo attribute, boolean restarting){
    //     super.onStartInput(attribute,restarting);

    //     //keyboard.setImeOptions(getResources(), attribute.imeOptions);
    // }
    @Override
    public void onFinishInput(){
        super.onFinishInput();

        keyboardView.closing();
    }
    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting){
        super.onStartInputView(attribute, restarting);

        keyboardView.setKeyboard(keyboard);
        keyboardView.closing();
    }
}
