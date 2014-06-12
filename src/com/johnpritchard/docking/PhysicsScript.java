/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class PhysicsScript {

    public final PhysicsOperator operator;

    public final PhysicsDOF dof;

    public final float operand;

    private PhysicsScript next;


    public PhysicsScript(PhysicsOperator operator, PhysicsDOF dof, float operand){
        super();
        this.operator = operator;
        this.dof = dof;
        this.operand = operand;
    }


    public float seconds(){
        return operand;
    }
    public long millis(){
        long value = (long)operand;
        value *= 1000L;
        return value;
    }
    /*
     * Object
     */
    public String toString(){
        StringBuilder string = new StringBuilder();
        {
            string.append(operator.name());
            string.append(' ');
            string.append(dof.name());
            string.append(' ');
            string.append(operand);
        }
        return string.toString();
    }
    /*
     * Thread queue
     */
    protected PhysicsScript push(PhysicsScript head){
        if (null == head)
            return this;
        else {
            return head.append(this);
        }
    }
    private PhysicsScript append(PhysicsScript tail){

        if (null != this.next){

            this.next.append(tail);
        }
        else {
            this.next = tail;
        }
        return this;
    }
    protected PhysicsScript pop(){
        PhysicsScript re = next;
        {
            this.next = null;
        }
        return re;
    }
}
