/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.llg;

import path.Operand;

/**
 * 
 */
public class DockingButtonPlain
    extends ViewPageComponentPath
{


    public DockingButtonPlain(){
        super();
    }
    public DockingButtonPlain(String text){
        super(text);
    }
    public DockingButtonPlain(Operand[] path){
        super(path);
    }
    public DockingButtonPlain(Operand[] path, Operand[] group){
        super(path,group);
    }
    public DockingButtonPlain(Operand[] path, Operand[] group, Operand[] clip){
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
