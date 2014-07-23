/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public enum PhysicsDOF {
    XP("X+"),
    XM("X-"),
    YP("Y+"),
    YM("Y-"),
    ZP("Z+"),
    ZM("Z-");


    public final String label;
    public final int llen;


    private PhysicsDOF(String label){
        this.label = label;
        this.llen = label.length();
    }


    @Override
    public String toString(){
        return label;
    }
}
