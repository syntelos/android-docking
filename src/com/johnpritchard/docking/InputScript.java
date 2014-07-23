/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public interface InputScript {
    /**
     * @return True iff extends {@link java.lang.Enum Enum}
     */
    boolean isEnum();
    /**
     * @return True to skip animation frame paint (tail paint and
     * sleep) following this and the subsequent input script
     * instruction: explicit-skip, implicit-skip,
     * repaint-after-execution.
     */
    boolean isSkipping();
    /**
     * @return True iff implements {@link InputScript$Eval Eval}
     */
    boolean isEval();

    int ordinal();

    String name();

    Input type();

    String toString();

    /**
     * 
     */
    public interface Eval
        extends InputScript
    {
        /**
         * @return Null for no effect, or page for subsequent effect
         */
        public Page eval();
    }

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
        public boolean isEval(){
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
        @Override
        public String toString(){
            return type.name()+"[0x"+Integer.toHexString(key)+",'"+key+"']";
        }
    }
    /**
     * 
     */
    public final class PageTo
        extends ObjectLog
        implements InputScript
    {

        public final Input type = Input.PageTo;

        public final Page page;


        public PageTo(Page page){
            super();
            this.page = page;
        }


        public boolean isEnum(){
            return false;
        }
        public boolean isSkipping(){
            return false;
        }
        public boolean isEval(){
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
        @Override
        public String toString(){
            return type.name()+' '+page.name();
        }
    }
    /**
     * 
     */
    public final class Database
        extends ObjectLog
        implements InputScript.Eval
    {
        /**
         * 
         */
        public enum Op {
            Init,
            Game,
            GameOver,
            GameEndPrev,
            GameEndNext,
            History,
            HistoryPrev,
            HistoryNext,
            Model,
            Hardware;
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
        public boolean isEval(){
            return true;
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
        @Override
        public String toString(){
            return type.name()+' '+op.name();
        }
        public Page eval(){
            switch(this.op){
            case Init:
                {
                    DockingDatabase.Init(context);
                    return null;
                }
            case Game:
                {
                    DockingDatabase.Game();
                    return Page.gameplay;
                }
            case GameOver:
                {
                    DockingDatabase.GameOver();
                    return DockingCraftStateVector.PageToEnd();
                }
            case GameEndPrev:
                {
                    DockingDatabase.HistoryPrev();
                    return DockingCraftStateVector.PageToEnd();
                }
            case GameEndNext:
                {
                    DockingDatabase.HistoryNext();
                    return DockingCraftStateVector.PageToEnd();
                }
            case History:
                {
                    if (DockingDatabase.History()){

                        return Page.gamehistory;
                    }
                    else {

                        return Page.nohistory;
                    }
                }
            case HistoryPrev:
                {
                    DockingDatabase.HistoryPrev();
                    return null;
                }
            case HistoryNext:
                {
                    DockingDatabase.HistoryNext();
                    return null;
                }
            case Model:
                {
                    DockingDatabase.Model();
                    return Page.gamemodel;
                }
            case Hardware:
                {
                    DockingDatabase.Hardware();
                    return Page.gamehardware;
                }
            default:
                throw new IllegalStateException(this.op.name());
            }
        }
    }
}
