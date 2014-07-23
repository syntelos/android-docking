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
    private final static Object StaticMonitor = new Object();

    private static DockingDatabase Instance;

    public static void Init(Context cx){

        synchronized(StaticMonitor){

            if (null == Instance || cx != Instance.context){

                Instance = new DockingDatabase(cx);
            }
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
        /*
         * New game
         */
        DockingCraftStateVector.Instance.game();

        DockingPageGameAbstract.View();

        return true;
    }
    public static boolean Model(){
        /*
         * Show model
         */
        DockingCraftStateVector.Instance.model();

        DockingPageGameAbstract.Range();

        return true;
    }
    public static boolean Hardware(){
        /*
         * Show hardware
         */
        DockingCraftStateVector.Instance.hardware();

        return true;
    }
    /**
     * Called from {@link DockingPhysics} to score and store the
     * {@link DockingCraftStateVector}.
     */
    public static void GameOver(){
        final DockingCraftStateVector vector = DockingCraftStateVector.Instance;

        SQLiteDatabase db = Writable();
        try {
            /*
             * State
             */
            ContentValues state = vector.write();

            long id = db.insert(DockingDatabase.STATE,DockingDatabaseHistory.State.LABEL,state);

            if (-1L < id){

                vector.cursor(id);
            }
        }
        finally {
            db.close();
        }

        DockingGameLevel.Review(vector.score);

        DockingPageGameAbstract.View();
    }
    /**
     * Called from {@link DockingPageStart} "HISTORY" (enter) to setup
     * the {@link DockingCraftStateVector}
     */
    public static boolean History(){

        SQLiteDatabase db = Readable();
        try {
            SQLiteQueryBuilder rQ = QueryStateInternal();

            final String[] projection = DockingDatabaseHistory.State.ProjectionInternal();

            final String where = null;

            final String[] whereargs = null;

            final String groupby = null;

            final String having = null;

            final String orderby = DockingDatabaseHistory.State.CREATED+" desc";

            final String limit = "1";

            Cursor cursor = rQ.query(db,projection,where,whereargs,groupby,having,orderby,limit);
            if (cursor.moveToFirst()){
                /*
                 * Have history
                 */
                try {
                    DockingCraftStateVector.Instance.read(cursor);

                    DockingPageGameAbstract.View();

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
                return false;
            }
        }
        finally {
            db.close();
        }
    }
    /**
     * Called from {@link DockingPageGameView}  to setup
     * the {@link DockingCraftStateVector}
     */
    public static boolean HistoryPrev(){
        final long id = DockingCraftStateVector.Instance.cursor();

        if (0L < id){

            SQLiteDatabase db = Readable();
            try {
                SQLiteQueryBuilder rQ = QueryStateInternal(id-1);

                final String[] projection = DockingDatabaseHistory.State.ProjectionInternal();

                final String where = null;

                final String[] whereargs = null;

                final String groupby = null;

                final String having = null;

                final String orderby = null;

                final String limit = null;

                Cursor cursor = rQ.query(db,projection,where,whereargs,groupby,having,orderby,limit);

                if (cursor.moveToFirst()){

                    try {

                        DockingCraftStateVector.Instance.read(cursor);

                        DockingPageGameAbstract.View();

                        return true;
                    }
                    finally {
                        cursor.close();
                    }
                }
                else {

                    return false;
                }
            }
            finally {
                db.close();
            }
        }
        else {

            return false;
        }
    }
    /**
     * Called from {@link DockingPageGameView}  to setup
     * the {@link DockingCraftStateVector}
     */
    public static boolean HistoryNext(){
        final long id = DockingCraftStateVector.Instance.cursor();

        if (-1 < id){

            SQLiteDatabase db = Readable();
            try {
                SQLiteQueryBuilder rQ = QueryStateInternal(id+1);

                final String[] projection = DockingDatabaseHistory.State.ProjectionInternal();

                final String where = null;

                final String[] whereargs = null;

                final String groupby = null;

                final String having = null;

                final String orderby = null;

                final String limit = null;

                Cursor cursor = rQ.query(db,projection,where,whereargs,groupby,having,orderby,limit);
                if (cursor.moveToFirst()){

                    try {

                        DockingCraftStateVector.Instance.read(cursor);

                        DockingPageGameAbstract.View();

                        return true;
                    }
                    finally {
                        cursor.close();
                    }
                }
                else {

                    return false;
                }
            }
            finally {
                db.close();
            }
        }
        else {

            return false;
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


    protected final Context context;


    private DockingDatabase(Context context) {
        super(context, NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + STATE + " ( "
                   + DockingDatabaseHistory.State._ID + " INTEGER PRIMARY KEY ASC AUTOINCREMENT, "
                   + DockingDatabaseHistory.State.IDENTIFIER + " TEXT UNIQUE NOT NULL, "
                   + DockingDatabaseHistory.State.LABEL + " TEXT, "
                   + DockingDatabaseHistory.State.LEVEL + " TEXT NOT NULL, "
                   + DockingDatabaseHistory.State.VX + " REAL, "
                   + DockingDatabaseHistory.State.AX + " REAL, "
                   + DockingDatabaseHistory.State.RX + " REAL, "
                   + DockingDatabaseHistory.State.T_SOURCE + " INTEGER, "
                   + DockingDatabaseHistory.State.T_CLOCK + " INTEGER, "
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
