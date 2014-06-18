
import fv3.math.Matrix;
import fv3.math.Vector;

public class star {

    private final static float[] C = {
        4.05703f, 3.07953f, -99.8702f
    };
    static {
        double[] m = Matrix.IVIdentity();
        Matrix.Scale(m,1.0e3);
        Matrix.Transform(m,C);
    }

    public static void main(String[] argv){
        final float cx = C[0];
        final float cy = C[1];
        final float cz = C[2];

        System.out.printf("star { %f, %f, %f}%n",cx,cy,cz);

        final float[] N = Vector.Normalize(C);

        final float nx = N[0];
        final float ny = N[1];
        final float nz = N[2];

        System.out.printf("norm { %f, %f, %f}%n",nx,ny,nz);

        final float ayz = (float)Math.atan((double)ny/nz);
        final float ayx = (float)Math.atan((double)ny/nx);
        final float azx = (float)Math.atan((double)nz/nx);

        System.out.printf("orthographic angle in Y-Z %f%n",ayz);
        System.out.printf("orthographic angle in Y-X %f%n",ayx);
        System.out.printf("orthographic angle in Z-X %f%n",azx);
    }
}
