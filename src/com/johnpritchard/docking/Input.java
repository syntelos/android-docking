/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * Serial input events represent DPAD navigation in contrast to random
 * input as from a pointer device like a trackball or mouse.  The
 * cardinal directions are also employed for geometric associations in
 * the context of a two dimensional space in which geometric shapes
 * are not overlapping.
 */
public enum Input {
    Up        (0,true),
    Down      (1,true),
    Left      (2,true), 
    Right     (3,true),
    /**
     * Represents input "enter" and geometric "contains"
     */
    Enter     (4,true),
    /**
     * Represents input "back" (without a geometric application)
     */
    Back      (5,false),
    /**
     * Visual operator to suspend repainting for the next operation,
     * typically not visual
     */
    Skip      (6,false),
    /**
     * Visual operator for animated event sequence to raise the visual
     * presentation from its ground state.
     */
    Emphasis  (7,false),
    /**
     * Visual operator to invert the effect of {@link #Emphasis},
     * restoring the visual presentation to its ground state.
     */
    Deemphasis(8,false);


    public final static Input[] List = Input.values();

    public final static int ListCount = List.length;

    public final static int NonGeometricCount = 4;

    public final static Input[] Geometric = Head(Input.List,(ListCount-NonGeometricCount));

    public final static int GeometricCount = Geometric.length;


    public final int index;

    public final boolean geometric;


    private Input(int index, boolean geometric){
        this.index = index;
        this.geometric = geometric;
    }


    public final static Input[] Head(Input[] list, final int count){

        final Input[] head = new Input[count];

        System.arraycopy(list,0,head,0,count);

        return head;
    }
    public final static Input[] Tail(Input[] list, final int count){

        final Input[] head = new Input[count];

        System.arraycopy(list,(list.length-count),head,0,count);

        return head;
    }
    public final static Input[] Add(Input[] a, Input[] b){
        if (null == a)
            return b;
        else if (null == b)
            return a;
        else {
            int a_len = a.length;
            int b_len = b.length;
            int len = (a_len+b_len);
            Input[] copier = new Input[len];
            System.arraycopy(a,0,copier,0,a_len);
            System.arraycopy(b,0,copier,a_len,b_len);
            return copier;
        }
    }
    public final static Input[] Add(Input[] a, Input[] b, int skip){
        if (null == a)
            return b;
        else if (null == b)
            return a;

        else {
            final int a_len = a.length;
            final int b_len = b.length;
            final int len = (a_len+b_len)-1;

            if (0 > skip || skip > len){

                final Input[] copier = new Input[len+1];

                System.arraycopy(a,0,copier,0,a_len);
                System.arraycopy(b,0,copier,a_len,b_len);

                return copier;
            }
            else {
                final Input[] copier = new Input[len];

                if (0 == skip){

                    System.arraycopy(a,1,copier,0,(a_len-1));
                    System.arraycopy(b,0,copier,(a_len-1),b_len);
                }
                else if (skip < a_len){

                    System.arraycopy(a,0,copier,0,skip);
                    System.arraycopy(a,(skip+1),copier,skip,(a_len-skip-1));
                    System.arraycopy(b,0,copier,(a_len-1),b_len);
                }
                else {
                    System.arraycopy(a,0,copier,0,a_len);

                    final int b_skip = (skip-a_len);

                    if (0 == b_skip){
                        System.arraycopy(b,1,copier,a_len,(b_len-1));
                    }
                    else { // (b_skip < b_len)
                        System.arraycopy(b,0,copier,a_len,b_skip);
                        System.arraycopy(b,(b_skip+1),copier,(a_len+b_skip),(b_len-b_skip-1));
                    }
                }
                return copier;
            }
        }
    }
    public final static int IndexOf(Input[] list, Input item){
        if (null == list || null == item)
            return -1;
        else {
            final int count = list.length;
            for (int cc = 0; cc < count; cc++){
                if (item == list[cc]){
                    return cc;
                }
            }
            return -1;
        }
    }
    public final static int LastIndexOf(Input[] list, Input item){
        if (null == list || null == item)
            return -1;
        else {
            final int count = list.length;
            for (int cc = (count-1); -1 < cc; cc--){
                if (item == list[cc]){
                    return cc;
                }
            }
            return -1;
        }
    }
}
