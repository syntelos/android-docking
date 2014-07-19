/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public interface InputScript {

    boolean isEnum();

    boolean isSkipping();

    int ordinal();

    String name();

    Input type();

    String toString();

    /**
     * 
     */
    public final class Key
        extends ObjectLog
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
        public boolean isSkipping(){
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
    /**
     * 
     */
    public final class Database
        extends ObjectLog
        implements InputScript
    {
        /**
         * 
         */
        public enum Op {
            Init;
        }


        public final Input type = Input.Database;

        public final Database.Op op;

        public final ObjectActivity context;


        public Database(Op op){
            this(op,null);
        }
        public Database(Op op, ObjectActivity context){
            super();
            if (null != op){
                this.op = op;


                if (Op.Init == op){

                    if (null != context){
                        this.context = context;
                    }
                    else {
                        throw new IllegalArgumentException();
                    }
                }
                else {
                    this.context = null;
                }
            }
            else {
                throw new IllegalArgumentException();
            }
        }


        public boolean isEnum(){
            return false;
        }
        public boolean isSkipping(){
            return (Op.Init == op);
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
            return type.name()+' '+op.name();
        }
        public void eval(){
            switch(this.op){
            case Init:
                {
                    DockingDatabase.Init(context);
                }
                break;

            default:
                throw new IllegalStateException(this.op.name());
            }
        }
    }
}
