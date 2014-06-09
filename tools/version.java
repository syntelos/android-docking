
import java.io.*;
import java.util.Properties;

import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * 
 */
public class version {

    private final static String AMF_NS = "http://schemas.android.com/apk/res/android";

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
    private final static File AMF()
        throws FileNotFoundException
    {
        File check = new File("AndroidManifest.xml");
        if (check.isFile())
            return check;
        else {
            check = new File("../AndroidManifest.xml");
            if (check.isDirectory())
                return check;
            else
                throw new FileNotFoundException("AndroidManifest.xml");
        }
    }

    public static void main(String[] argv){
        try {
            final DocumentBuilderFactory XF = DocumentBuilderFactory.newInstance();
            {
                XF.setNamespaceAware(true);
                XF.setValidating(false);
            }
            final DocumentBuilder X = XF.newDocumentBuilder();

            final File amff = AMF();
            final File srcd = SRCD();
            final File tgtf = new File(srcd,"com/johnpritchard/docking/DockingVersion.java");


            Document amf_doc = null;
            {
                InputStream in = new FileInputStream(amff);
                try {
                    amf_doc = X.parse(in,amff.toURI().toString());
                }
                finally {
                    in.close();
                }
            }
            Element amf_el = amf_doc.getDocumentElement();

            final int version_code = Integer.parseInt(amf_el.getAttributeNS(AMF_NS,"versionCode"));
            final String version_name = amf_el.getAttributeNS(AMF_NS,"versionName");

            final PrintStream out = new PrintStream(tgtf);
            try {
                out.println("/*");
                out.println(" * Copyright (C) 2014 John Pritchard.  All rights reserved.");
                out.println(" */");
                out.println("package com.johnpritchard.docking;");
                out.println();
                out.println("/**");
                out.println(" * Information derived from the android manifest.");
                out.println(" */");
                out.println("public final class DockingVersion {");
                out.println();
                out.printf ("    public final static int Code = %d;%n",version_code);
                out.println();
                out.printf ("    public final static String Name = \"%s\";%n",version_name);
                out.println();
                out.println("    private DockingVersion(){");
                out.println("        super();");
                out.println("    }");
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
