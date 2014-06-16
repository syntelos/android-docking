
import fv3.math.Vector;

public class port {

    private final static int Res = 6;

    private final static float[] N_Z_P = {0f,0f,1f};

    private final static int X = 0;
    private final static int Y = 1;
    private final static int Z = 2;


    public static void main(String[] argv){

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
            final float z = -0.040f;
            final double r = +0.940;

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
                    hatch_v[cv++] = z;

                    hatch_v[cv++] = x1;
                    hatch_v[cv++] = y1;
                    hatch_v[cv++] = z;

                    System.out.printf("hatch[%d] V {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f}%n",
                                      a, 0f, 0f, 0f, x0, y0, z, x1, y1, z);

                    System.out.printf("hatch[%d] N {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f}%n",
                                      a, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f);
                }
            }
            System.out.println();
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

                    System.out.printf("rim[%d] V {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f}%n",
                                      a, x0, y0, z_0, x0, y0, z_1, x1, y1, z_1, x0, y0, z_0, x1, y1, z_0, x1, y1, z_1);

                    System.out.printf("rim[%d] N {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f}%n",
                                      a, -x0, -y0, 0f, -x0, -y0, 0f, -x1, -y1, 0f, -x0, -y0, 0f, -x1, -y1, 0f, -x1, -y1, 0f);

                }
            }
            System.out.println();
        }
        {
            /*
             * Z+ port face
             */
            final float z = +0.000f;
            final double r_i = +0.940;
            final double r_o = +1.000;

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

                    System.out.printf("face[%d] V {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f}%n",
                                      a, x_id_0, y_id_0, z, x_od_0, y_od_0, z, x_od_1, y_od_1, z, x_id_0, y_id_0, z, x_id_1, y_id_1, z, x_od_1, y_od_1, z);

                    System.out.printf("face[%d] N {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f}%n",
                                      a, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f);
                }
            }
            System.out.println();
        }

        {
            /*
             * Z- port face skirt
             */
            final float z_i = +0.000f;
            final float z_o = -0.040f;
            final double r_i = +1.000;
            final double r_o = +1.040;

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
                    skirt_v[cv++] = y_od_0; // face A square corner vertex
                    skirt_v[cv++] = z_o;

                    skirt_v[cv++] = x_od_1;
                    skirt_v[cv++] = y_od_1;
                    skirt_v[cv++] = z_o;

                    skirt_v[cv++] = x_id_0;
                    skirt_v[cv++] = y_id_0;
                    skirt_v[cv++] = z_i;

                    skirt_v[cv++] = x_id_1;
                    skirt_v[cv++] = y_id_1; // face B square corner vertex
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


                    System.out.printf("skirt[%d] V {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f}%n",
                                      a, x_id_0, y_id_0, z_i, x_od_0, y_od_0, z_o, x_od_1, y_od_1, z_o, x_id_0, y_id_0, z_i, x_id_1, y_id_1, z_i, x_od_1, y_od_1, z_o);

                    System.out.printf("skirt[%d] N {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f}%n",
                                      a, x_n_A, y_n_A, z_n_A, x_n_A, y_n_A, z_n_A, x_n_A, y_n_A, z_n_A, x_n_B, y_n_B, z_n_B, x_n_B, y_n_B, z_n_B, x_n_B, y_n_B, z_n_B);

                }
            }
            System.out.println();
        }

        {
            /*
             * body end at +Z
             */
            final float z_i = -0.040f;
            final float z_o = -0.784f;
            final double r_i = +1.040;
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
                    end_v[cv++] = y_id_0; // face A [CCW V 1]
                    end_v[cv++] = z_i;

                    end_v[cv++] = x_od_0;
                    end_v[cv++] = y_od_0; // face A [CCW V 2] square corner vertex
                    end_v[cv++] = z_o;

                    end_v[cv++] = x_od_1;
                    end_v[cv++] = y_od_1; // face A [CCW V 3]
                    end_v[cv++] = z_o;

                    end_v[cv++] = x_id_0;
                    end_v[cv++] = y_id_0; // face B [CCW V 3]
                    end_v[cv++] = z_i;

                    end_v[cv++] = x_id_1;
                    end_v[cv++] = y_id_1; // face B [CCW V 2] square corner vertex
                    end_v[cv++] = z_i;

                    end_v[cv++] = x_od_1;
                    end_v[cv++] = y_od_1; // face B [CCW V 1]
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

                    System.out.printf("end[%d] V {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f}%n",
                                      a, x_id_0, y_id_0, z_i, x_od_0, y_od_0, z_o, x_od_1, y_od_1, z_o, x_id_0, y_id_0, z_i, x_id_1, y_id_1, z_i, x_od_1, y_od_1, z_o);

                    System.out.printf("end[%d] N {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f} {%1.1f,%1.1f,%1.1f}%n",
                                      a, x_n_A, y_n_A, z_n_A, x_n_A, y_n_A, z_n_A, x_n_A, y_n_A, z_n_A, x_n_B, y_n_B, z_n_B, x_n_B, y_n_B, z_n_B, x_n_B, y_n_B, z_n_B);


                }
            }

            System.out.println();
        }
    }


    protected final static double PI_M2 = (Math.PI * 2.0);


    protected final static int CircleTableSize(int n){
        return (n+1);
    }
    protected final static void CircleTable(double[] sin_t, double[] cos_t, final int n){

        final int size = Math.abs(n);

        final double angle = PI_M2/(double)n;

        cos_t[0] = 1.0;
        sin_t[0] = 0.0;

        for (int cc = 1; cc < size; cc++){

            cos_t[cc] = Math.cos(angle*cc);
            sin_t[cc] = Math.sin(angle*cc);
        }

        sin_t[size] = sin_t[0];
        cos_t[size] = cos_t[0];
    }


    protected final static float[] Add(float[] a, float[] b){

        int alen = a.length;
        int blen = b.length;
        int len = (alen+blen);
        float[] copier = new float[len];
        System.arraycopy(a,0,copier,0,alen);
        System.arraycopy(b,0,copier,alen,blen);
        return copier;

    }
    protected final static float[] Copy(float[] tgt, int ofs, int count, float[] v){

        int t = ofs;

        final int z = (ofs+count);

        while (t < z){

            System.arraycopy(v,0,tgt,t,3);

            t += 3;
        }

        return tgt;
    }
}
