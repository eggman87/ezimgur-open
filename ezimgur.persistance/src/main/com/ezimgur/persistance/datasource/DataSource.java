package com.ezimgur.persistance.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.ezimgur.persistance.database.DatabaseTable;
import com.ezimgur.persistance.database.ImgurDatabase;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/17/12
 * Time: 5:47 PM
 */
public abstract class DataSource {

    protected DatabaseTable mTable;
    protected SQLiteDatabase mSqlDatabase;
    protected ImgurDatabase mImgurContext;

    public DataSource(Context context) {
        mImgurContext = new ImgurDatabase(context);
    }

    public void open() {
        mSqlDatabase = mImgurContext.getWritableDatabase();
    }

    public void close() {
        mSqlDatabase.close();
        mImgurContext.close();

    }

    protected void insertNewRecord(ContentValues values) {
        try {
            this.open();
            long rowId = mSqlDatabase.insert(mTable.getTableName(), null, values);
        } finally {
            this.close();
        }
    }
}
