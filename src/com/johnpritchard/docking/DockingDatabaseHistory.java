/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;

/**
 * 
 * @see DockingDatabase
 */
public final class DockingDatabaseHistory {

    public final static String AUTHORITY = "com.johnpritchard.docking.provider.DockingDatabaseHistory";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/state");

    public static final String CONTENT_TYPE_LIST = "vnd.android.cursor.dir/vnd.syntelos.state";

    public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.syntelos.state";

    /**
     * Columns in table "state"
     */
    public final static class State
        extends Object
        implements android.provider.BaseColumns
    {

        protected final static String IDENTIFIER = "identifier";

        protected final static String LABEL = "label";

        public final static String VX = "vx";

        public final static String AX = "ax";

        public final static String RX = "rx";

        public final static String T_SOURCE = "t_source";

        public final static String T_XP0 = "t_xp0";

        public final static String T_XM0 = "t_xm0";

        public final static String T_XP1 = "t_xp1";

        public final static String T_XM1 = "t_xm1";

        public final static String CREATED = "created";

        public final static String COMPLETED = "completed";


        private final static String[] InternalList = {
            _ID,
            IDENTIFIER,
            LABEL,
            VX,
            AX,
            RX,
            T_SOURCE,
            T_XP0,
            T_XM0,
            T_XP1,
            T_XM1,
            CREATED,
            COMPLETED
        };
        public final static HashMap<String,String> Internal(){
            HashMap<String, String> internal = new HashMap();

            for (String column : InternalList){
                internal.put(column,column);
            }
            return internal;
        }
        private final static String[] ExportList = {
            _ID,
            VX,
            AX,
            RX,
            T_SOURCE,
            CREATED,
            COMPLETED
        };
        public final static HashMap<String,String> Export(){
            HashMap<String, String> export = new HashMap();

            for (String column : ExportList){
                export.put(column,column);
            }
            return export;
        }
        public final static String[] Export(String[] projection){
            if (null == projection){
                return ExportList.clone();
            }
            else {
                boolean excluded = false;

                for (String p : projection){

                    if (IDENTIFIER.equalsIgnoreCase(p)){
                        excluded = true;
                        break;
                    }
                    else if (LABEL.equalsIgnoreCase(p)){
                        excluded = true;
                        break;
                    }
                }

                if (excluded){
                    return ExportList.clone();
                }
                else {
                    return projection;
                }
            }
        }
        private State(){
            super();
        }
    }


    private DockingDatabaseHistory(){
        super();
    }
}
