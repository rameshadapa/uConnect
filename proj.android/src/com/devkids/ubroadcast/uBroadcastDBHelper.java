package com.devkids.ubroadcast;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class uBroadcastDBHelper extends SQLiteOpenHelper
{
    public uBroadcastDBHelper(Context context, String name, CursorFactory factory, int version)
    {
	super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    @Override
    public synchronized void close()
    {
	super.close();
    }
}
