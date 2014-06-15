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

    private final static int Res = 32;

    private final static float[] N_Z1 = {0f,0f,1f};

    private final static int X = 0;
    private final static int Y = 1;
    private final static int Z = 2;

    public final static DockingGeometryPort Instance = new DockingGeometryPort();

    protected static void Init(){
    }


    /**
     */
    private final int ts_count;

    private final FloatBuffer b_ts_n, b_ts_v;


    private DockingGeometryPort(){
        super();

        int ts_count;
        float[] ts_n;
        float[] ts_v;
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
                final float z0 = -0.040f;
                final double r = +0.940;

                final int count = (9*Res);
                final float[] hatch_v = new float[count];
                {
                    for (int a = 0, cc = 0; a < Res; a++){

                        hatch_v[cc++] = 0.0f;
                        hatch_v[cc++] = 0.0f;
                        hatch_v[cc++] = 0.0f;

                        hatch_v[cc++] = (float)(cos_t1[a]*r);
                        hatch_v[cc++] = (float)(sin_t1[a]*r);
                        hatch_v[cc++] = z0;

                        hatch_v[cc++] = (float)(cos_t1[a+1]*r);
                        hatch_v[cc++] = (float)(sin_t1[a+1]*r);
                        hatch_v[cc++] = z0;
                    }
                }

                ts_count = (count/3);
                ts_n = Copy(new float[count],0,count,N_Z1);
                ts_v = hatch_v;
            }
            {
                /*
                 * Z+ rim around port center
                 */
                final float z_0 = -0.040f;
                final float z_1 = +0.000f;
                final double r  = +0.940;

                final int count = (18*Res);
                final float[] rim_v = new float[count];
                {
                    for (int a = 0, cc = 0; a < Res; a++){

                        final float x0 = (float)(cos_t1[a]*r);
                        final float y0 = (float)(sin_t1[a]*r);

                        final float x1 = (float)(cos_t1[a+1]*r);
                        final float y1 = (float)(sin_t1[a+1]*r);

                        rim_v[cc++] = x0;
                        rim_v[cc++] = y0;
                        rim_v[cc++] = z_0;

                        rim_v[cc++] = x0;
                        rim_v[cc++] = y0;
                        rim_v[cc++] = z_1;

                        rim_v[cc++] = x1;
                        rim_v[cc++] = y1;
                        rim_v[cc++] = z_1;

                        rim_v[cc++] = x0;
                        rim_v[cc++] = y0;
                        rim_v[cc++] = z_0;

                        rim_v[cc++] = x1;
                        rim_v[cc++] = y1;
                        rim_v[cc++] = z_0;

                        rim_v[cc++] = x1;
                        rim_v[cc++] = y1;
                        rim_v[cc++] = z_1;
                    }
                }
                final float[] rim_n = new float[count];
                {
                    for (int cv = 0, cn = 0; cv < count; ){

                        final float x0 = rim_v[cv++];
                        final float y0 = rim_v[cv++];
                        final float z0 = rim_v[cv++];

                        final float x1 = rim_v[cv++];
                        final float y1 = rim_v[cv++];
                        final float z1 = rim_v[cv++];

                        final float x2 = rim_v[cv++];
                        final float y2 = rim_v[cv++];
                        final float z2 = rim_v[cv++];

                        rim_n[cn++] = -x0;
                        rim_n[cn++] = -y0;
                        rim_n[cn++] = 0.0f;

                        rim_n[cn++] = -x1;
                        rim_n[cn++] = -y1;
                        rim_n[cn++] = 0.0f;

                        rim_n[cn++] = -x2;
                        rim_n[cn++] = -y2;
                        rim_n[cn++] = 0.0f;
                    }
                }

                ts_count += (count/3);
                ts_n = Add(ts_n,rim_n);
                ts_v = Add(ts_v,rim_v);
            }
            {
                /*
                 * Z+ port face
                 */
                final float z = +0.000f;
                final double r0 = +0.940;
                final double r1 = +1.000;

                final int count = (18*Res);
                final float[] face_v = new float[count];
                {
                    for (int a = 0, cc = 0; a < Res; a++){

                        final double x_0 = cos_t1[a];
                        final double y_0 = sin_t1[a];

                        final double x_1 = cos_t1[a+1];
                        final double y_1 = sin_t1[a+1];


                        final float x_id_0 = (float)(x_0*r0);
                        final float y_id_0 = (float)(y_0*r0);

                        final float x_id_1 = (float)(x_1*r0);
                        final float y_id_1 = (float)(y_1*r0);

                        final float x_od_0 = (float)(x_0*r1);
                        final float y_od_0 = (float)(y_0*r1);

                        final float x_od_1 = (float)(x_1*r1);
                        final float y_od_1 = (float)(y_1*r1);


                        face_v[cc++] = x_id_0;
                        face_v[cc++] = y_id_0;
                        face_v[cc++] = z;

                        face_v[cc++] = x_od_0;
                        face_v[cc++] = y_od_0;
                        face_v[cc++] = z;

                        face_v[cc++] = x_od_1;
                        face_v[cc++] = y_od_1;
                        face_v[cc++] = z;

                        face_v[cc++] = x_id_0;
                        face_v[cc++] = y_id_0;
                        face_v[cc++] = z;

                        face_v[cc++] = x_id_1;
                        face_v[cc++] = y_id_1;
                        face_v[cc++] = z;

                        face_v[cc++] = x_od_1;
                        face_v[cc++] = y_od_1;
                        face_v[cc++] = z;
                    }
                }
                final float[] face_n = Copy(new float[count],0,count,N_Z1);

                ts_count += (count/3);
                ts_n = Add(ts_n,face_n);
                ts_v = Add(ts_v,face_v);
            }


        }
        {
            final ByteBuffer ib_ts = ByteBuffer.allocateDirect(ts_n.length * bpf);
            ib_ts.order(nativeOrder);
            this.b_ts_n = ib_ts.asFloatBuffer();
            this.b_ts_n.put(ts_n);
            this.b_ts_n.position(0);
        }
        {
            final ByteBuffer ib_ts = ByteBuffer.allocateDirect(ts_v.length * bpf);
            ib_ts.order(nativeOrder);
            this.b_ts_v = ib_ts.asFloatBuffer();
            this.b_ts_v.put(ts_v);
            this.b_ts_v.position(0);
        }
        this.ts_count = ts_count;
    }

    public void draw(){

        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_VERTEX_ARRAY);

        glNormalPointer(GL_FLOAT,stride,this.b_ts_n);
        glVertexPointer(3,GL_FLOAT,stride,this.b_ts_v);
        glDrawArrays(GL_TRIANGLE_STRIP,0,this.ts_count);

    }
}
