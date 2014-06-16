/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import static android.opengl.GLES10.*;

import fv3.math.Vector;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 
 */
public final class DockingGeometryPort
    extends View3DGeometryCircle
{

    private final static int Res1 = 32;
    private final static int Res2 = 48;
    private final static int Res3 = 96;

    private final static double R = 100.0;
    private final static double R_SQ = (R*R);
    private final static double S = (R-(R*Math.sin(Math.PI/3.0)));


    private final static float RZ(double x, double y){
        return (float)(Math.sqrt(R_SQ - (x*x) - (y*y))-R);
    }

    private final static float[] N_Z_P = {0f,0f,+1f};
    private final static float[] N_Z_N = {0f,0f,-1f};

    private final static int X = 0;
    private final static int Y = 1;
    private final static int Z = 2;

    public final static DockingGeometryPort Instance = new DockingGeometryPort();

    protected static void Init(){
    }



    private final int count_lines;
    private final int count_triangles;

    private final FloatBuffer b_lines;
    private final FloatBuffer b_triangles;
    private final FloatBuffer b_normals;


    private DockingGeometryPort(){
        super();

        float[] normals;
        float[] triangles;
        {
            final int table1 = CircleTableSize(Res1);

            final double[] sin_t1 = new double[table1];
            final double[] cos_t1 = new double[table1];
            {
                CircleTable(sin_t1,cos_t1,-(Res1));
            }

            final int table2 = CircleTableSize(Res2);

            final double[] sin_t2 = new double[table2];
            final double[] cos_t2 = new double[table2];
            {
                CircleTable(sin_t2,cos_t2,-(Res2));
            }

            final int table3 = CircleTableSize(Res3);

            final double[] sin_t3 = new double[table3];
            final double[] cos_t3 = new double[table3];
            {
                CircleTable(sin_t3,cos_t3,-(Res3));
            }


            {
                final double r = +0.400;

                final int count = (9*Res1);
                final float[] v = new float[count];
                final float[] n = new float[count];
                {
                    final float x0 = 0.0f;
                    final float y0 = 0.0f;
                    final float z0 = 0.0f;

                    for (int a = 0, cv = 0, cn = 0; a < Res1; a++){

                        final float x1 = (float)(cos_t1[a]*r);
                        final float y1 = (float)(sin_t1[a]*r);
                        final float z1 = RZ(x1,y1);

                        final float x2 = (float)(cos_t1[a+1]*r);
                        final float y2 = (float)(sin_t1[a+1]*r);
                        final float z2 = RZ(x2,y2);

                        v[cv++] = x0;
                        v[cv++] = y0;
                        v[cv++] = z0;

                        v[cv++] = x1;
                        v[cv++] = y1;
                        v[cv++] = z1;

                        v[cv++] = x2;
                        v[cv++] = y2;
                        v[cv++] = z2;

                        final float[] f_n = Vector.Normal(x0, y0, z0,
                                                          x1, y1, z1,
                                                          x2, y2, z2);

                        final float x_n = f_n[X];
                        final float y_n = f_n[Y];
                        final float z_n = f_n[Z];

                        n[cn++] = x_n;
                        n[cn++] = y_n;
                        n[cn++] = z_n;

                        n[cn++] = x_n;
                        n[cn++] = y_n;
                        n[cn++] = z_n;

                        n[cn++] = x_n;
                        n[cn++] = y_n;
                        n[cn++] = z_n;

                    }
                }
                normals = n;
                triangles = v;
            }

            double r_i = +0.400;
            double r_o = +0.800;

            {
                while (r_o <= 1.2){

                    final int count = (18*Res1);
                    final float[] v = new float[count];
                    final float[] n = new float[count];
                    {
                        for (int a = 0, cv = 0, cn = 0; a < Res1; a++){

                            final double x_0 = cos_t1[a];
                            final double y_0 = sin_t1[a];

                            final double x_1 = cos_t1[a+1];
                            final double y_1 = sin_t1[a+1];


                            final float x_id_0 = (float)(x_0*r_i);
                            final float y_id_0 = (float)(y_0*r_i);
                            final float z_id_0 = RZ(x_id_0, y_id_0);

                            final float x_id_1 = (float)(x_1*r_i);
                            final float y_id_1 = (float)(y_1*r_i);
                            final float z_id_1 = RZ(x_id_1, y_id_1);

                            final float x_od_0 = (float)(x_0*r_o);
                            final float y_od_0 = (float)(y_0*r_o);
                            final float z_od_0 = RZ(x_od_0, y_od_0);

                            final float x_od_1 = (float)(x_1*r_o);
                            final float y_od_1 = (float)(y_1*r_o);

                            float z_od_1;
                            if (3.6 <= r_o && 4.6 >= r_o){

                                z_od_1 = 0.0f;
                            }
                            else {
                                z_od_1 = RZ(x_od_1, y_od_1);
                            }

                            v[cv++] = x_id_0;
                            v[cv++] = y_id_0;
                            v[cv++] = z_id_0;

                            v[cv++] = x_od_0;
                            v[cv++] = y_od_0;
                            v[cv++] = z_od_0;

                            v[cv++] = x_od_1;
                            v[cv++] = y_od_1;
                            v[cv++] = z_od_1;

                            v[cv++] = x_id_0;
                            v[cv++] = y_id_0;
                            v[cv++] = z_id_0;

                            v[cv++] = x_id_1;
                            v[cv++] = y_id_1;
                            v[cv++] = z_id_1;

                            v[cv++] = x_od_1;
                            v[cv++] = y_od_1;
                            v[cv++] = z_od_1;

                            final float[] f_A_n = Vector.Normal(x_id_0, y_id_0, z_id_0,
                                                                x_od_0, y_od_0, z_od_0,
                                                                x_od_1, y_od_1, z_od_1);

                            final float[] f_B_n = Vector.Normal(x_od_1, y_od_1, z_od_1,
                                                                x_id_1, y_id_1, z_id_1,
                                                                x_id_0, y_id_0, z_id_0);

                            final float x_n_A = f_A_n[X];
                            final float y_n_A = f_A_n[Y];
                            final float z_n_A = f_A_n[Z];

                            final float x_n_B = f_B_n[X];
                            final float y_n_B = f_B_n[Y];
                            final float z_n_B = f_B_n[Z];

                            n[cn++] = x_n_A;
                            n[cn++] = y_n_A;
                            n[cn++] = z_n_A;

                            n[cn++] = x_n_A;
                            n[cn++] = y_n_A;
                            n[cn++] = z_n_A;

                            n[cn++] = x_n_A;
                            n[cn++] = y_n_A;
                            n[cn++] = z_n_A;

                            n[cn++] = x_n_B;
                            n[cn++] = y_n_B;
                            n[cn++] = z_n_B;

                            n[cn++] = x_n_B;
                            n[cn++] = y_n_B;
                            n[cn++] = z_n_B;

                            n[cn++] = x_n_B;
                            n[cn++] = y_n_B;
                            n[cn++] = z_n_B;
                        }
                    }

                    normals = Add(normals,n);
                    triangles = Add(triangles,v);

                    r_i = r_o;
                    r_o += 0.4;
                }
            }

            {
                while (r_o <= 1.6){

                    final int count = (18*Res2);
                    final float[] v = new float[count];
                    final float[] n = new float[count];
                    {
                        for (int a = 0, cv = 0, cn = 0; a < Res2; a++){

                            final double x_0 = cos_t2[a];
                            final double y_0 = sin_t2[a];

                            final double x_1 = cos_t2[a+1];
                            final double y_1 = sin_t2[a+1];


                            final float x_id_0 = (float)(x_0*r_i);
                            final float y_id_0 = (float)(y_0*r_i);
                            final float z_id_0 = RZ(x_id_0, y_id_0);


                            final float x_id_1 = (float)(x_1*r_i);
                            final float y_id_1 = (float)(y_1*r_i);
                            final float z_id_1 = RZ(x_id_1, y_id_1);

                            final float x_od_0 = (float)(x_0*r_o);
                            final float y_od_0 = (float)(y_0*r_o);
                            final float z_od_0 = RZ(x_od_0, y_od_0);

                            final float x_od_1 = (float)(x_1*r_o);
                            final float y_od_1 = (float)(y_1*r_o);
                            final float z_od_1 = RZ(x_od_1, y_od_1);


                            v[cv++] = x_id_0;
                            v[cv++] = y_id_0;
                            v[cv++] = z_id_0;

                            v[cv++] = x_od_0;
                            v[cv++] = y_od_0;
                            v[cv++] = z_od_0;

                            v[cv++] = x_od_1;
                            v[cv++] = y_od_1;
                            v[cv++] = z_od_1;

                            v[cv++] = x_id_0;
                            v[cv++] = y_id_0;
                            v[cv++] = z_id_0;

                            v[cv++] = x_id_1;
                            v[cv++] = y_id_1;
                            v[cv++] = z_id_1;

                            v[cv++] = x_od_1;
                            v[cv++] = y_od_1;
                            v[cv++] = z_od_1;

                            final float[] f_A_n = Vector.Normal(x_id_0, y_id_0, z_id_0,
                                                                x_od_0, y_od_0, z_od_0,
                                                                x_od_1, y_od_1, z_od_1);

                            final float[] f_B_n = Vector.Normal(x_od_1, y_od_1, z_od_1,
                                                                x_id_1, y_id_1, z_id_1,
                                                                x_id_0, y_id_0, z_id_0);

                            final float x_n_A = f_A_n[X];
                            final float y_n_A = f_A_n[Y];
                            final float z_n_A = f_A_n[Z];

                            final float x_n_B = f_B_n[X];
                            final float y_n_B = f_B_n[Y];
                            final float z_n_B = f_B_n[Z];

                            n[cn++] = x_n_A;
                            n[cn++] = y_n_A;
                            n[cn++] = z_n_A;

                            n[cn++] = x_n_A;
                            n[cn++] = y_n_A;
                            n[cn++] = z_n_A;

                            n[cn++] = x_n_A;
                            n[cn++] = y_n_A;
                            n[cn++] = z_n_A;

                            n[cn++] = x_n_B;
                            n[cn++] = y_n_B;
                            n[cn++] = z_n_B;

                            n[cn++] = x_n_B;
                            n[cn++] = y_n_B;
                            n[cn++] = z_n_B;

                            n[cn++] = x_n_B;
                            n[cn++] = y_n_B;
                            n[cn++] = z_n_B;
                        }
                    }

                    normals = Add(normals,n);
                    triangles = Add(triangles,v);

                    r_i = r_o;
                    r_o += 0.4;
                }
            }

            {
                while (r_o <= 10.0){

                    final int count = (18*Res3);
                    final float[] v = new float[count];
                    final float[] n = new float[count];
                    {
                        for (int a = 0, cv = 0, cn = 0; a < Res3; a++){

                            final double x_0 = cos_t3[a];
                            final double y_0 = sin_t3[a];

                            final double x_1 = cos_t3[a+1];
                            final double y_1 = sin_t3[a+1];


                            final float x_id_0 = (float)(x_0*r_i);
                            final float y_id_0 = (float)(y_0*r_i);

                            float z_id_0;
                            if (2.0 <= r_i && 3.0 >= r_i){

                                z_id_0 = 0.0f;
                            }
                            else if (3.6 <= r_i && 4.6 >= r_i){

                                z_id_0 = 0.0f;
                            }
                            else {
                                z_id_0 = RZ(x_id_0, y_id_0);
                            }

                            final float x_id_1 = (float)(x_1*r_i);
                            final float y_id_1 = (float)(y_1*r_i);
                            float z_id_1;
                            if (2.0 <= r_i && 3.0 >= r_i){

                                z_id_1 = 0.0f;
                            }
                            else if (3.6 <= r_i && 4.6 >= r_i){

                                z_id_1 = 0.0f;
                            }
                            else {
                                z_id_1 = RZ(x_id_1, y_id_1);
                            }

                            final float x_od_0 = (float)(x_0*r_o);
                            final float y_od_0 = (float)(y_0*r_o);
                            float z_od_0;
                            if (2.0 <= r_o && 3.0 >= r_o){

                                z_od_0 = 0.0f;
                            }
                            else if (3.6 <= r_o && 4.6 >= r_o){

                                z_od_0 = 0.0f;
                            }
                            else {
                                z_od_0 = RZ(x_od_0, y_od_0);
                            }

                            final float x_od_1 = (float)(x_1*r_o);
                            final float y_od_1 = (float)(y_1*r_o);

                            float z_od_1;
                            if (2.0 <= r_o && 3.0 >= r_o){

                                z_od_1 = 0.0f;
                            }
                            else if (3.6 <= r_o && 4.6 >= r_o){

                                z_od_1 = 0.0f;
                            }
                            else {
                                z_od_1 = RZ(x_od_1, y_od_1);
                            }

                            v[cv++] = x_id_0;
                            v[cv++] = y_id_0;
                            v[cv++] = z_id_0;

                            v[cv++] = x_od_0;
                            v[cv++] = y_od_0;
                            v[cv++] = z_od_0;

                            v[cv++] = x_od_1;
                            v[cv++] = y_od_1;
                            v[cv++] = z_od_1;

                            v[cv++] = x_id_0;
                            v[cv++] = y_id_0;
                            v[cv++] = z_id_0;

                            v[cv++] = x_id_1;
                            v[cv++] = y_id_1;
                            v[cv++] = z_id_1;

                            v[cv++] = x_od_1;
                            v[cv++] = y_od_1;
                            v[cv++] = z_od_1;

                            final float[] f_A_n = Vector.Normal(x_id_0, y_id_0, z_id_0,
                                                                x_od_0, y_od_0, z_od_0,
                                                                x_od_1, y_od_1, z_od_1);

                            final float[] f_B_n = Vector.Normal(x_od_1, y_od_1, z_od_1,
                                                                x_id_1, y_id_1, z_id_1,
                                                                x_id_0, y_id_0, z_id_0);

                            final float x_n_A = f_A_n[X];
                            final float y_n_A = f_A_n[Y];
                            final float z_n_A = f_A_n[Z];

                            final float x_n_B = f_B_n[X];
                            final float y_n_B = f_B_n[Y];
                            final float z_n_B = f_B_n[Z];

                            n[cn++] = x_n_A;
                            n[cn++] = y_n_A;
                            n[cn++] = z_n_A;

                            n[cn++] = x_n_A;
                            n[cn++] = y_n_A;
                            n[cn++] = z_n_A;

                            n[cn++] = x_n_A;
                            n[cn++] = y_n_A;
                            n[cn++] = z_n_A;

                            n[cn++] = x_n_B;
                            n[cn++] = y_n_B;
                            n[cn++] = z_n_B;

                            n[cn++] = x_n_B;
                            n[cn++] = y_n_B;
                            n[cn++] = z_n_B;

                            n[cn++] = x_n_B;
                            n[cn++] = y_n_B;
                            n[cn++] = z_n_B;
                        }
                    }

                    normals = Add(normals,n);
                    triangles = Add(triangles,v);

                    r_i = r_o;
                    r_o += 0.4;
                }
            }


        }

        this.count_triangles = (triangles.length/3);
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(normals.length * bpf);
            ib.order(nativeOrder);
            this.b_normals = ib.asFloatBuffer();
            this.b_normals.put(normals);
            this.b_normals.position(0);
        }
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(triangles.length * bpf);
            ib.order(nativeOrder);
            this.b_triangles = ib.asFloatBuffer();
            this.b_triangles.put(triangles);
            this.b_triangles.position(0);
        }

        float[] lines = Lines(triangles);
        this.count_lines = (lines.length/3);
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(lines.length * bpf);
            ib.order(nativeOrder);
            this.b_lines = ib.asFloatBuffer();
            this.b_lines.put(lines);
            this.b_lines.position(0);
        }
    }

    public void triangles(){

        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_VERTEX_ARRAY);

        glNormalPointer(GL_FLOAT,stride,this.b_normals);
        glVertexPointer(3,GL_FLOAT,stride,this.b_triangles);

        glDrawArrays(GL_TRIANGLES,0,this.count_triangles);
    }
    public void lines(){

        glEnableClientState(GL_VERTEX_ARRAY);

        glVertexPointer(3,GL_FLOAT,stride,this.b_lines);

        glDrawArrays(GL_LINES,0,this.count_lines);
    }
}
