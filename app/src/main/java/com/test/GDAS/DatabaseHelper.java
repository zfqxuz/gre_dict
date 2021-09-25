package com.test.GDAS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper  {


    public DatabaseHelper(@Nullable Context context) {
        super(context, Constants.WORD_DBNAME, null, Constants.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper","Database create");
        String sql="create table "+Constants.TABLE_NAME+"(word varchar not null,exp varchar not null, mark int default 0,aid varchar default '',primary key(word,exp))";
        db.execSQL(sql);
        //发布时候需要还原
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.d("DatabaseHelper","Database upgrade");

    }
}
