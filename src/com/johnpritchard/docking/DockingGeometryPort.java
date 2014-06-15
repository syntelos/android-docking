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

    private final static float[] VN_X1 = {0f,0f,1f};

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
                 * Port center
                 */
                final float z0 = -0.040f;
                final float r0 = +0.940f;

                final int count = (9*Res);
                final float[] pc_v = new float[count];
                {
                    for (int a = 0, cc = 0; a < Res; a++){

                        pc_v[cc++] = 0.0f;
                        pc_v[cc++] = 0.0f;
                        pc_v[cc++] = 0.0f;

                        pc_v[cc++] = (float)(cos_t1[a]*r0);
                        pc_v[cc++] = (float)(sin_t1[a]*r0);
                        pc_v[cc++] = z0;

                        pc_v[cc++] = (float)(cos_t1[a+1]*r0);
                        pc_v[cc++] = (float)(sin_t1[a+1]*r0);
                        pc_v[cc++] = z0;
                    }
                }

                ts_count = (count/3);
                ts_n = Copy(new float[count],0,count,VN_X1);
                ts_v = pc_v;
            }
            {
                /*
                 * Z+ rim around port center
                 */
                final float z_0 = -0.040f;
                final float z_1 = +0.000f;
                final float r0 = +0.940f;

                final int count = (9*Res);
                final float[] rim_v = new float[count];
                {
                    for (int a = 0, cc = 0; a < Res; a++){

                        final float x0 = (float)(cos_t1[a]*r0);
                        final float y0 = (float)(sin_t1[a]*r0);

                        final float x1 = (float)(cos_t1[a+1]*r0);
                        final float y1 = (float)(sin_t1[a+1]*r0);

                        rim_v[cc++] = x0;
                        rim_v[cc++] = y0;
                        rim_v[cc++] = z_0;

                        rim_v[cc++] = x0;
                        rim_v[cc++] = y0;
                        rim_v[cc++] = z_1;

                        rim_v[cc++] = x1;
                        rim_v[cc++] = y1;
                        rim_v[cc++] = z_1;
                    }
                }
                final float[] rim_n = new float[count];
                {
                    for (int a = 0, cv = 0, cn = 0; a < Res; a++){

                        final float x0 = rim_v[cv++];
                        final float y0 = rim_v[cv++];
                        final float z0 = rim_v[cv++];

                        final float x1 = rim_v[cv++];
                        final float y1 = rim_v[cv++];
                        final float z1 = rim_v[cv++];

                        final float x2 = rim_v[cv++];
                        final float y2 = rim_v[cv++];
                        final float z2 = rim_v[cv++];

                        float[] n0 = Vector.Cross(x0,y0,z0,x1,y1,z1);
                        float[] n1 = Vector.Cross(x1,y1,z1,x2,y2,z2);

                        rim_n[cn++] = n0[X];
                        rim_n[cn++] = n0[Y];
                        rim_n[cn++] = n0[Z];

                        rim_n[cn++] = n0[Z];
                        rim_n[cn++] = n0[Y];
                        rim_n[cn++] = n0[Z];

                        rim_n[cn++] = n1[Z];
                        rim_n[cn++] = n1[Z];
                        rim_n[cn++] = n1[Z];
                    }
                }

                ts_count += (count/3);
                ts_n = Add(ts_n,rim_n);
                ts_v = Add(ts_v,rim_v);
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
