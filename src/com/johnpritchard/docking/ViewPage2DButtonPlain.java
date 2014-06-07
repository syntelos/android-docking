/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import path.Operand;

/**
 * 
 */
public class ViewPage2DButtonPlain
    extends ViewPage2DComponentPath
{


    public ViewPage2DButtonPlain(){
        super();
    }
    public ViewPage2DButtonPlain(String text){
        super(text);
    }
    public ViewPage2DButtonPlain(Operand[] path){
        super(path);
    }
    public ViewPage2DButtonPlain(Operand[] path, Operand[] group){
        super(path,group);
    }
    public ViewPage2DButtonPlain(Operand[] path, Operand[] group, Operand[] clip){
        super(path,group,clip);
    }


    @Override
    public void setCurrent(){

        super.setCurrent();

        this.inverseFillType();
    }
    @Override
    public void clearCurrent(){

        super.clearCurrent();

        this.plainFillType();
    }
}
