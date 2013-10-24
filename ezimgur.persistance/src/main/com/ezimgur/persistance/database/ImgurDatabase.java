package com.ezimgur.persistance.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ezimgur.persistance.datasource.AlbumDataSource;
import com.ezimgur.persistance.datasource.AlbumImagesDataSource;
import com.ezimgur.persistance.datasource.GalleryDataSource;
import com.ezimgur.persistance.datasource.ImageDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/17/12
 * Time: 3:20 PM
 */
public class ImgurDatabase extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "ezimgur.db";
    private static final int DATABASE_VERSION = 1;

    public ImgurDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for (DatabaseTable table : getDatabaseTables()){
            //Log.d("EzImgur.database", table.getCreateSqlText());
            sqLiteDatabase.execSQL(table.getCreateSqlText());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        for (DatabaseTable table : getDatabaseTables()) {
            sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS "+table.getTableName());
        }
        onCreate(sqLiteDatabase);
    }

    private List<DatabaseTable> getDatabaseTables() {
        List<DatabaseTable> tables = new ArrayList<DatabaseTable>();

        //add all tables from any datasources we have...
        tables.add(GalleryDataSource.newInstance(context).initTable());
        tables.add(AlbumImagesDataSource.newInstance(context).initTable());
        tables.add(AlbumDataSource.newInstance(context).initTable());
        tables.add(ImageDataSource.newInstance(context).initTable());

        return tables;
    }
}
