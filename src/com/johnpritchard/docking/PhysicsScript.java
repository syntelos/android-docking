/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class PhysicsScript {

    public final PhysicsOperator operator;

    public final float operand;


    public PhysicsScript(PhysicsOperator operator, float operand){
        super();
        this.operator = operator;
        this.operand = operand;
    }


    public String toString(){
        StringBuilder string = new StringBuilder();
        {
            string.append(operator.name());
            string.append('=');
            string.append(operand);
        }
        return string.toString();
    }

}
