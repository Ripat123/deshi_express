package com.sbitbd.deshi_express.Config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class database extends SQLiteOpenHelper {

    SQLiteDatabase DB = this.getWritableDatabase();
    query_process query_process = new query_process();
    private final static String DBNAME = "courierDB";
    private final static int VERSION = 1;
    private final static String user = "create Table user(id VARCHAR,user_name VARCHAR,user_id VARCHAR,agent_id VARCHAR)";
    private final static String session = "create Table session(id INTEGER,session_data VARCHAR NOT NULL)";
    private final static String user_upgrade = "drop Table if exists user";
    private final static String session_upgrade = "drop Table if exists session";

    public database(@Nullable Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(user);
        db.execSQL(session);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(user_upgrade);
        db.execSQL(session_upgrade);
    }

    public Boolean DataOperation(ContentValues contentValues, String choose, String table, String where){
        switch (choose) {
            case "insert":
                Log.d("dddd","insert");
                return query_process.insert(DB, contentValues, table);
            case "update":
                Log.d("dddd","update");
                return query_process.update(DB, contentValues, table, where);
            case "delete":
                Log.d("dddd","delete");
                return query_process.delete(DB, table, where);
            default:
                Log.d("dddd","default");
                return false;
        }
    }
    public Cursor getUerData(String sql){
        return query_process.getData(DB,sql);
    }
}
