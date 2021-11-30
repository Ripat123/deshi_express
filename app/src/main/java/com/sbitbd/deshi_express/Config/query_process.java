package com.sbitbd.deshi_express.Config;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class query_process {
    protected Boolean insert(SQLiteDatabase DB, ContentValues contentValues, String table){
        long result = DB.insert(table,null,contentValues);
        return result != -1;
    }
    protected Boolean update(SQLiteDatabase DB, ContentValues contentValues,String table,String where){
        long result = DB.update(table,contentValues,where,null);
        return result != -1;
    }
    protected Boolean delete(SQLiteDatabase DB,String table,String where){
        long result = DB.delete(table,where,null);
        return result != -1;
    }
    protected Cursor getData(SQLiteDatabase db, String sql){
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }
}
