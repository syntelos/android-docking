/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import path.Operand;

/**
 * 
 */
public class DockingButtonGroup
    extends DockingButtonPlain
    implements ViewPageComponentGroup
{


    public DockingButtonGroup(){
        super();
    }
    public DockingButtonGroup(String text){
        super(text);
    }
    public DockingButtonGroup(Operand[] path){
        super(path);
    }
    public DockingButtonGroup(Operand[] path, Operand[] group){
        super(path,group);
    }
    public DockingButtonGroup(Operand[] path, Operand[] group, Operand[] clip){
        super(path,group,clip);
    }

}
