
import java.io.*;
import java.util.Properties;

/**
 * 
 */
public class version {

    private final static File SRCD()
        throws FileNotFoundException
    {
        File check = new File("src");
        if (check.isDirectory())
            return check;
        else {
            check = new File("../src");
            if (check.isDirectory())
                return check;
            else
                throw new FileNotFoundException("src");
        }
    }
    private final static File PROPS()
        throws FileNotFoundException
    {
        File check = new File("version.properties");
        if (check.isFile())
            return check;
        else {
            check = new File("../version.properties");
            if (check.isDirectory())
                return check;
            else
                throw new FileNotFoundException("version.properties");
        }
    }

    public static void main(String[] argv){
        try {
            final File propsf = PROPS();
            final File srcd = SRCD();
            final File tgtf = new File(srcd,"com/johnpritchard/docking/DockingVersion.java");

            final Properties properties = new Properties();
            {
                InputStream in = new FileInputStream(propsf);
                try {
                    properties.load(in);
                }
                finally {
                    in.close();
                }
            }
            final int version_major = Integer.parseInt(properties.getProperty("version.major"));
            final int version_minor = Integer.parseInt(properties.getProperty("version.minor"));
            final int version_build = Integer.parseInt(properties.getProperty("version.build"));

            final PrintStream out = new PrintStream(tgtf);
            try {
                out.println("package com.johnpritchard.docking;");
                out.println();
                out.println("/**");
                out.println(" * ");
                out.println(" */");
                out.println("public final class DockingVersion {");
                out.println();
                out.printf ("    public final static int Major = %d;%n",version_major);
                out.println();
                out.printf ("    public final static int Minor = %d;%n",version_minor);
                out.println();
                out.printf ("    public final static int Build = %d;%n",version_build);
                out.println();
                out.println("    public final static java.lang.String String = java.lang.String.valueOf(Major)+'.'+java.lang.String.valueOf(Minor)+'.'+java.lang.String.valueOf(Build);");
                out.println();
                out.printf ("    private DockingVersion(){%n");
                out.printf ("        super();%n");
                out.printf ("    }%n");
                out.println("}");
            }
            finally {
                out.flush();
                out.close();
            }
        }
        catch (Exception exc){
            exc.printStackTrace();
            System.exit(1);
        }
    }
}
