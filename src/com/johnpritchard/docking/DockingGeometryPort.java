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

    private final static int Res = 96;

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
            final int table_1 = CircleTableSize(Res);

            final double[] sin_t1 = new double[table_1];
            final double[] cos_t1 = new double[table_1];
            {
                CircleTable(sin_t1,cos_t1,-(Res));
            }
            {
                /*
                 * Z- port center
                 */
                final float z0 = -0.400f;
                final double r = +3.600;

                final int count = (9*Res);
                final float[] hatch_v = new float[count];
                {
                    for (int a = 0, cv = 0; a < Res; a++){

                        final float x0 = (float)(cos_t1[a]*r);
                        final float y0 = (float)(sin_t1[a]*r);

                        final float x1 = (float)(cos_t1[a+1]*r);
                        final float y1 = (float)(sin_t1[a+1]*r);

                        hatch_v[cv++] = 0.0f;
                        hatch_v[cv++] = 0.0f;
                        hatch_v[cv++] = 0.0f;

                        hatch_v[cv++] = x0;
                        hatch_v[cv++] = y0;
                        hatch_v[cv++] = z0;

                        hatch_v[cv++] = x1;
                        hatch_v[cv++] = y1;
                        hatch_v[cv++] = z0;
                    }
                }

                normals = Copy(new float[count],0,count,N_Z_N);
                triangles = hatch_v;
            }
            {
                /*
                 * Z+ rim around port center
                 */
                final float z_0 = -0.400f;
                final float z_1 = +0.000f;
                final double r  = +3.600;

                final int count = (18*Res);
                final float[] rim_v = new float[count];
                final float[] rim_n = new float[count];
                {
                    for (int a = 0, cv = 0, cn = 0; a < Res; a++){

                        final float x0 = (float)(cos_t1[a]*r);
                        final float y0 = (float)(sin_t1[a]*r);

                        final float x1 = (float)(cos_t1[a+1]*r);
                        final float y1 = (float)(sin_t1[a+1]*r);

                        rim_v[cv++] = x0;
                        rim_v[cv++] = y0;
                        rim_v[cv++] = z_0;

                        rim_v[cv++] = x0;
                        rim_v[cv++] = y0;
                        rim_v[cv++] = z_1;

                        rim_v[cv++] = x1;
                        rim_v[cv++] = y1;
                        rim_v[cv++] = z_1;

                        rim_v[cv++] = x0;
                        rim_v[cv++] = y0;
                        rim_v[cv++] = z_0;

                        rim_v[cv++] = x1;
                        rim_v[cv++] = y1;
                        rim_v[cv++] = z_0;

                        rim_v[cv++] = x1;
                        rim_v[cv++] = y1;
                        rim_v[cv++] = z_1;
                        /*
                         */
                        rim_n[cn++] = -x0;
                        rim_n[cn++] = -y0;
                        rim_n[cn++] = 0.0f;

                        rim_n[cn++] = -x0;
                        rim_n[cn++] = -y0;
                        rim_n[cn++] = 0.0f;

                        rim_n[cn++] = -x1;
                        rim_n[cn++] = -y1;
                        rim_n[cn++] = 0.0f;

                        rim_n[cn++] = -x0;
                        rim_n[cn++] = -y0;
                        rim_n[cn++] = 0.0f;

                        rim_n[cn++] = -x1;
                        rim_n[cn++] = -y1;
                        rim_n[cn++] = 0.0f;

                        rim_n[cn++] = -x1;
                        rim_n[cn++] = -y1;
                        rim_n[cn++] = 0.0f;
                    }
                }

                normals = Add(normals,rim_n);
                triangles = Add(triangles,rim_v);
            }
            {
                /*
                 * Z+ port face
                 */
                final float z = +0.000f;
                final double r_i = +3.600;
                final double r_o = +4.600;

                final int count = (18*Res);
                final float[] face_v = new float[count];
                {
                    for (int a = 0, cv = 0; a < Res; a++){

                        final double x_0 = cos_t1[a];
                        final double y_0 = sin_t1[a];

                        final double x_1 = cos_t1[a+1];
                        final double y_1 = sin_t1[a+1];


                        final float x_id_0 = (float)(x_0*r_i);
                        final float y_id_0 = (float)(y_0*r_i);

                        final float x_id_1 = (float)(x_1*r_i);
                        final float y_id_1 = (float)(y_1*r_i);

                        final float x_od_0 = (float)(x_0*r_o);
                        final float y_od_0 = (float)(y_0*r_o);

                        final float x_od_1 = (float)(x_1*r_o);
                        final float y_od_1 = (float)(y_1*r_o);


                        face_v[cv++] = x_id_0;
                        face_v[cv++] = y_id_0;
                        face_v[cv++] = z;

                        face_v[cv++] = x_od_0;
                        face_v[cv++] = y_od_0;
                        face_v[cv++] = z;

                        face_v[cv++] = x_od_1;
                        face_v[cv++] = y_od_1;
                        face_v[cv++] = z;

                        face_v[cv++] = x_id_0;
                        face_v[cv++] = y_id_0;
                        face_v[cv++] = z;

                        face_v[cv++] = x_id_1;
                        face_v[cv++] = y_id_1;
                        face_v[cv++] = z;

                        face_v[cv++] = x_od_1;
                        face_v[cv++] = y_od_1;
                        face_v[cv++] = z;
                    }
                }
                final float[] face_n = Copy(new float[count],0,count,N_Z_N);

                normals = Add(normals,face_n);
                triangles = Add(triangles,face_v);
            }

            {
                /*
                 * Z- port face skirt
                 */
                final float z_i = +0.000f;
                final float z_o = -0.400f;
                final double r_i = +4.600;
                final double r_o = +5.400;

                final float z_n = (float)(1.0/3.0);

                final int count = (18*Res);
                final float[] skirt_v = new float[count];
                final float[] skirt_n = new float[count];
                {
                    for (int a = 0, cv = 0, cn = 0; a < Res; a++){

                        final double x_0 = cos_t1[a];
                        final double y_0 = sin_t1[a];

                        final double x_1 = cos_t1[a+1];
                        final double y_1 = sin_t1[a+1];


                        final float x_id_0 = (float)(x_0*r_i);
                        final float y_id_0 = (float)(y_0*r_i);

                        final float x_id_1 = (float)(x_1*r_i);
                        final float y_id_1 = (float)(y_1*r_i);

                        final float x_od_0 = (float)(x_0*r_o);
                        final float y_od_0 = (float)(y_0*r_o);

                        final float x_od_1 = (float)(x_1*r_o);
                        final float y_od_1 = (float)(y_1*r_o);


                        skirt_v[cv++] = x_id_0;
                        skirt_v[cv++] = y_id_0;
                        skirt_v[cv++] = z_i;

                        skirt_v[cv++] = x_od_0;
                        skirt_v[cv++] = y_od_0;
                        skirt_v[cv++] = z_o;

                        skirt_v[cv++] = x_od_1;
                        skirt_v[cv++] = y_od_1;
                        skirt_v[cv++] = z_o;

                        skirt_v[cv++] = x_id_0;
                        skirt_v[cv++] = y_id_0;
                        skirt_v[cv++] = z_i;

                        skirt_v[cv++] = x_id_1;
                        skirt_v[cv++] = y_id_1;
                        skirt_v[cv++] = z_i;

                        skirt_v[cv++] = x_od_1;
                        skirt_v[cv++] = y_od_1;
                        skirt_v[cv++] = z_o;

                        final float[] f_A_n = Vector.Normal(x_id_0, y_id_0, z_i,
                                                            x_od_0, y_od_0, z_o,
                                                            x_od_1, y_od_1, z_o);

                        final float[] f_B_n = Vector.Normal(x_od_1, y_od_1, z_o,
                                                            x_id_1, y_id_1, z_i,
                                                            x_id_0, y_id_0, z_i);

                        final float x_n_A = f_A_n[X];
                        final float y_n_A = f_A_n[Y];
                        final float z_n_A = f_A_n[Z];

                        final float x_n_B = f_B_n[X];
                        final float y_n_B = f_B_n[Y];
                        final float z_n_B = f_B_n[Z];

                        skirt_n[cn++] = x_n_A;
                        skirt_n[cn++] = y_n_A;
                        skirt_n[cn++] = z_n_A;

                        skirt_n[cn++] = x_n_A;
                        skirt_n[cn++] = y_n_A;
                        skirt_n[cn++] = z_n_A;

                        skirt_n[cn++] = x_n_A;
                        skirt_n[cn++] = y_n_A;
                        skirt_n[cn++] = z_n_A;

                        skirt_n[cn++] = x_n_B;
                        skirt_n[cn++] = y_n_B;
                        skirt_n[cn++] = z_n_B;

                        skirt_n[cn++] = x_n_B;
                        skirt_n[cn++] = y_n_B;
                        skirt_n[cn++] = z_n_B;

                        skirt_n[cn++] = x_n_B;
                        skirt_n[cn++] = y_n_B;
                        skirt_n[cn++] = z_n_B;
                    }
                }

                normals = Add(normals,skirt_n);
                triangles = Add(triangles,skirt_v);
            }

            {
                /*
                 * body end at +Z
                 */
                final float z_i = -0.400f;
                final float z_o = -0.800f;
                final double r_i = +5.400;
                final double r_o = +10.00;

                final int count = (18*Res);
                final float[] end_v = new float[count];
                final float[] end_n = new float[count];
                {
                    for (int a = 0, cv = 0, cn = 0; a < Res; a++){

                        final double x_0 = cos_t1[a];
                        final double y_0 = sin_t1[a];

                        final double x_1 = cos_t1[a+1];
                        final double y_1 = sin_t1[a+1];


                        final float x_id_0 = (float)(x_0*r_i);
                        final float y_id_0 = (float)(y_0*r_i);

                        final float x_id_1 = (float)(x_1*r_i);
                        final float y_id_1 = (float)(y_1*r_i);

                        final float x_od_0 = (float)(x_0*r_o);
                        final float y_od_0 = (float)(y_0*r_o);

                        final float x_od_1 = (float)(x_1*r_o);
                        final float y_od_1 = (float)(y_1*r_o);


                        end_v[cv++] = x_id_0;
                        end_v[cv++] = y_id_0;
                        end_v[cv++] = z_i;

                        end_v[cv++] = x_od_0;
                        end_v[cv++] = y_od_0;
                        end_v[cv++] = z_o;

                        end_v[cv++] = x_od_1;
                        end_v[cv++] = y_od_1;
                        end_v[cv++] = z_o;

                        end_v[cv++] = x_id_0;
                        end_v[cv++] = y_id_0;
                        end_v[cv++] = z_i;

                        end_v[cv++] = x_id_1;
                        end_v[cv++] = y_id_1;
                        end_v[cv++] = z_i;

                        end_v[cv++] = x_od_1;
                        end_v[cv++] = y_od_1;
                        end_v[cv++] = z_o;


                        final float[] f_A_n = Vector.Normal(x_id_0, y_id_0, z_i,
                                                            x_od_0, y_od_0, z_o,
                                                            x_od_1, y_od_1, z_o);

                        final float[] f_B_n = Vector.Normal(x_od_1, y_od_1, z_o,
                                                            x_id_1, y_id_1, z_i,
                                                            x_id_0, y_id_0, z_i);

                        final float x_n_A = f_A_n[X];
                        final float y_n_A = f_A_n[Y];
                        final float z_n_A = f_A_n[Z];

                        final float x_n_B = f_B_n[X];
                        final float y_n_B = f_B_n[Y];
                        final float z_n_B = f_B_n[Z];

                        end_n[cn++] = x_n_A;
                        end_n[cn++] = y_n_A;
                        end_n[cn++] = z_n_A;

                        end_n[cn++] = x_n_A;
                        end_n[cn++] = y_n_A;
                        end_n[cn++] = z_n_A;

                        end_n[cn++] = x_n_A;
                        end_n[cn++] = y_n_A;
                        end_n[cn++] = z_n_A;

                        end_n[cn++] = x_n_B;
                        end_n[cn++] = y_n_B;
                        end_n[cn++] = z_n_B;

                        end_n[cn++] = x_n_B;
                        end_n[cn++] = y_n_B;
                        end_n[cn++] = z_n_B;

                        end_n[cn++] = x_n_B;
                        end_n[cn++] = y_n_B;
                        end_n[cn++] = z_n_B;
                    }
                }

                normals = Add(normals,end_n);
                triangles = Add(triangles,end_v);
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
