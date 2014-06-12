/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.HashMap;

/**
 * 
 * @see DockingDatabaseHistory
 */
public final class DockingDatabase 
    extends android.database.sqlite.SQLiteOpenHelper
{
    private static DockingDatabase Instance;

    public static void Init(Context cx){

        if (null == Instance){
            Instance = new DockingDatabase(cx);
        }
    }
    public static SQLiteDatabase Readable(){

        if (null != Instance){
            return Instance.getReadableDatabase();
        }
        else {
            throw new IllegalStateException();
        }
    }
    public static SQLiteDatabase Writable(){

        if (null != Instance){
            return Instance.getWritableDatabase();
        }
        else {
            throw new IllegalStateException();
        }
    }
    public static SQLiteQueryBuilder QueryStateExport(){
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(STATE);
        qb.setProjectionMap(STATE_MAP_EXPORT);

        return qb;
    }
    public static SQLiteQueryBuilder QueryStateExport(String id){

        return QueryStateExport(Long.parseLong(id));
    }
    public static SQLiteQueryBuilder QueryStateExport(long id){
        SQLiteQueryBuilder qb = QueryStateExport();

        qb.appendWhere(DockingDatabaseHistory.State._ID + " = " + id);

        return qb;
    }
    public static SQLiteQueryBuilder QueryStateInternal(){
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(STATE);
        qb.setProjectionMap(STATE_MAP_INTERNAL);

        return qb;
    }
    public static SQLiteQueryBuilder QueryStateInternal(String id){

        return QueryStateInternal(Long.parseLong(id));
    }
    public static SQLiteQueryBuilder QueryStateInternal(long id){
        SQLiteQueryBuilder qb = QueryStateInternal();

        qb.appendWhere(DockingDatabaseHistory.State._ID + " = " + id);

        return qb;
    }

    private static HashMap<String, String> STATE_MAP_INTERNAL = DockingDatabaseHistory.State.Internal();

    private static HashMap<String, String> STATE_MAP_EXPORT = DockingDatabaseHistory.State.Export();


    protected final static String NAME = "docking.db";
    protected final static int VERSION = 1;
    protected final static String STATE = "state";


    private DockingDatabase(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + STATE + " ( "
                   + DockingDatabaseHistory.State._ID + " INTEGER PRIMARY KEY ASC AUTOINCREMENT, "
                   + DockingDatabaseHistory.State.IDENTIFIER + " TEXT UNIQUE NOT NULL, "
                   + DockingDatabaseHistory.State.LABEL + " TEXT, "
                   + DockingDatabaseHistory.State.VX + " REAL, "
                   + DockingDatabaseHistory.State.AX + " REAL, "
                   + DockingDatabaseHistory.State.RX + " REAL, "
                   + DockingDatabaseHistory.State.T_SOURCE + " INTEGER, "
                   + DockingDatabaseHistory.State.T_XP0 + " INTEGER, "
                   + DockingDatabaseHistory.State.T_XM0 + " INTEGER, "
                   + DockingDatabaseHistory.State.T_XP1 + " INTEGER, "
                   + DockingDatabaseHistory.State.T_XM1 + " INTEGER, "
                   + DockingDatabaseHistory.State.CREATED + " INTEGER, "
                   + DockingDatabaseHistory.State.COMPLETED + " INTEGER"
                   + ");");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }
}
