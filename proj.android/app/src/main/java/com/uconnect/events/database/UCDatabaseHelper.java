/**
 * Created by jaggu on 4/18/2015.
 */
package com.uconnect.events.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * UCDatabaseHelper helps to create the local sqlite database.
 */
public class UCDatabaseHelper extends SQLiteOpenHelper {

    /**
     *
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public UCDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
