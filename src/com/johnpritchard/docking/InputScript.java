/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public interface InputScript {

    boolean isEnum();

    int ordinal();

    String name();

    Input type();

    String toString();

    /**
     * 
     */
    public final class Key
        extends Object
        implements InputScript
    {

        public final Input type = Input.Key;

        public final char key;


        public Key(char key){
            super();
            this.key = key;
        }


        public boolean isEnum(){
            return false;
        }
        public int ordinal(){
            return type.ordinal();
        }
        public String name(){
            return type.name();
        }
        public Input type(){
            return type;
        }
        public String toString(){
            return type.name()+"[0x"+Integer.toHexString(key)+",'"+key+"']";
        }
    }

}
