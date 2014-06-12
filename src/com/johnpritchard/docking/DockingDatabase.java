/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    /**
     * Called from {@link DockingPageStart} "GAME" (enter) to setup
     * the {@link DockingCraftStateVector}
     * 
     * @return New game, otherwise continue an existing game
     */
    public static boolean Game(){

        SQLiteDatabase db = Readable();
        try {
            SQLiteQueryBuilder rQ = QueryStateInternal();

            rQ.appendWhere(DockingDatabaseHistory.State.COMPLETED + " = NULL");

            /*
             * Order by most recently created
             */
            Cursor cursor = rQ.query(db,null,null,null,null,null,DockingDatabaseHistory.State.CREATED+" desc","1");
            if (cursor.moveToFirst()){
                /*
                 * Existing game
                 */
                try {
                    DockingCraftStateVector.Instance.read(cursor);

                    Info("existing game");

                    return false;
                }
                finally {
                    cursor.close();
                }
            }
            else {
                /*
                 * New game
                 */
                DockingCraftStateVector.Instance.create();

                Info("new game");

                return true;
            }
        }
        finally {
            db.close();
        }
    }
    /**
     * Called from {@link DockingPhysics} to score and store the
     * {@link DockingCraftStateVector}.
     */
    public static void GameOver(){
        SQLiteDatabase db = Writable();
        try {
            /*
             * State
             */
            ContentValues state = DockingCraftStateVector.Instance.write();

            if (state.containsKey(DockingDatabaseHistory.State._ID)){

                String where = DockingDatabaseHistory.State._ID+" = "+state.getAsLong(DockingDatabaseHistory.State._ID);

                db.update(DockingDatabase.STATE,state,where,null);
            }
            else {

                long id = db.insert(DockingDatabase.STATE,DockingDatabaseHistory.State.LABEL,state);

                DockingCraftStateVector.Instance.cursor(id);
            }
        }
        finally {
            db.close();
        }
    }
    /**
     * Called from {@link DockingPageStart} "HISTORY" (enter) to setup
     * the {@link DockingCraftStateVector}
     */
    public static boolean History(){

        SQLiteDatabase db = Readable();
        try {
            SQLiteQueryBuilder rQ = QueryStateInternal();

            rQ.appendWhere(DockingDatabaseHistory.State.COMPLETED + " != NULL");

            /*
             * One record, most recently created
             */
            Cursor cursor = rQ.query(db,null,null,null,null,null,DockingDatabaseHistory.State.CREATED+" desc","1");
            if (cursor.moveToFirst()){
                /*
                 * Have history
                 */
                try {
                    DockingCraftStateVector.Instance.read(cursor);

                    Info("existing history");

                    return true;
                }
                finally {
                    cursor.close();
                }
            }
            else {
                /*
                 * No history
                 */
                DockingCraftStateVector.Instance.create();

                Info("no history");

                return false;
            }
        }
        finally {
            db.close();
        }
    }
    /**
     * Called from {@link DockingPageGameView} 
     */
    public static boolean HistoryPrev(){

        SQLiteDatabase db = Readable();
        try {
            SQLiteQueryBuilder rQ = QueryStateInternal();

            rQ.appendWhere(DockingDatabaseHistory.State.COMPLETED + " != NULL");

            final long id = DockingCraftStateVector.Instance.cursor();

            if (-1 < id){

                rQ.appendWhere(" AND " + DockingDatabaseHistory.State._ID + " = "+ id );
            }

            /*
             * Order by most recently created
             */
            Cursor cursor = rQ.query(db,null,null,null,null,null,DockingDatabaseHistory.State.CREATED+" asc","2");

            if (cursor.moveToFirst() && cursor.moveToNext()){

                try {
                    DockingCraftStateVector.Instance.read(cursor);

                    Info("history prev");

                    return true;
                }
                finally {
                    cursor.close();
                }
            }
            else {
                Info("history <end>");

                return false;
            }
        }
        finally {
            db.close();
        }
    }
    /**
     * Called from {@link DockingPageGameView} 
     */
    public static boolean HistoryNext(){

        SQLiteDatabase db = Readable();
        try {
            SQLiteQueryBuilder rQ = QueryStateInternal();

            rQ.appendWhere(DockingDatabaseHistory.State.COMPLETED + " != NULL");

            final long id = DockingCraftStateVector.Instance.cursor();

            if (-1 < id){

                rQ.appendWhere(" AND " + DockingDatabaseHistory.State._ID + " = "+ id );
            }

            /*
             * Order by most recently created
             */
            Cursor cursor = rQ.query(db,null,null,null,null,null,DockingDatabaseHistory.State.CREATED+" desc","2");

            if (cursor.moveToFirst() && cursor.moveToNext()){

                try {
                    DockingCraftStateVector.Instance.read(cursor);

                    Info("history next");

                    return true;
                }
                finally {
                    cursor.close();
                }
            }
            else {

                Info("history <end>");

                return false;
            }
        }
        finally {
            db.close();
        }
    }

    private static HashMap<String, String> STATE_MAP_INTERNAL = DockingDatabaseHistory.State.Internal();

    private static HashMap<String, String> STATE_MAP_EXPORT = DockingDatabaseHistory.State.Export();

    protected final static boolean Export(String name){

        return (null != STATE_MAP_EXPORT.get(name));
    }
    protected final static boolean Internal(String name){

        return (null != STATE_MAP_INTERNAL.get(name));
    }

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
                   + DockingDatabaseHistory.State.COMPLETED + " INTEGER, "
                   + DockingDatabaseHistory.State.SCORE + " REAL"
                   + ");");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }


    protected final static String TAG = ObjectLog.TAG;


    protected static void Verbose(String m){
        Log.i(TAG,"DockingDatabase "+m);
    }
    protected static void Verbose(String m, Throwable t){
        Log.i(TAG,"DockingDatabase "+m,t);
    }
    protected static void Debug(String m){
        Log.d(TAG,"DockingDatabase "+m);
    }
    protected static void Debug(String m, Throwable t){
        Log.d(TAG,"DockingDatabase "+m,t);
    }
    protected static void Info(String m){
        Log.i(TAG,"DockingDatabase "+m);
    }
    protected static void Info(String m, Throwable t){
        Log.i(TAG,"DockingDatabase "+m,t);
    }
    protected static void Warn(String m){
        Log.w(TAG,"DockingDatabase "+m);
    }
    protected static void Warn(String m, Throwable t){
        Log.w(TAG,"DockingDatabase "+m,t);
    }
    protected static void Error(String m){
        Log.e(TAG,"DockingDatabase "+m);
    }
    protected static void Error(String m, Throwable t){
        Log.e(TAG,"DockingDatabase "+m,t);
    }
    protected static void WTF(String m){
        Log.wtf(TAG,"DockingDatabase "+m);
    }
    protected static void WTF(String m, Throwable t){
        Log.wtf(TAG,"DockingDatabase "+m,t);
    }
}
