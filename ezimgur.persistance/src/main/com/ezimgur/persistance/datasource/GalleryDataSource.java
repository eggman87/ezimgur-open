package com.ezimgur.persistance.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.ezimgur.persistance.database.DatabaseField;
import com.ezimgur.persistance.database.DatabaseTable;
import com.ezimgur.datacontract.SavedGallery;
import com.ezimgur.instrumentation.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/17/12
 * Time: 5:16 PM
 */
public class GalleryDataSource extends DataSource {

    private DatabaseField mNameField = new DatabaseField(DatabaseField.FieldType.TEXT, "name", false, true);

    private static GalleryDataSource dataSource;
    private static final String TAG = "EzImgur.Persistance.GalleryDataSource";

    public static GalleryDataSource newInstance(Context context) {
        if (dataSource == null)
            dataSource = new GalleryDataSource(context);
        return dataSource;
    }

    public DatabaseTable initTable() {
        mTable = new DatabaseTable("savedgalleries");
        mTable.addTableField(mNameField);
        return mTable;
    }

    private GalleryDataSource(Context context) {
        super(context);
        if (mTable == null)
            initTable();
    }

    public SavedGallery createSavedGallery(String galleryName) {
        ContentValues values = new ContentValues();
        values.put(mNameField.getName(), galleryName);

        SavedGallery gallery = getSavedGalleryByName(galleryName);
        if (gallery == null) {
            Log.d(TAG, "creating new record for request "+gallery);
            try {
                this.open();
                long rowId = mSqlDatabase.insert(mTable.getTableName(), null, values);
            } finally {
                this.close();
            }
        } else {
            return gallery;
        }

        return getSavedGalleryByName(galleryName);
    }

    public void deleteSavedGallery(String galleryName) {
        Log.d(TAG, "deleting request @ " + galleryName);
        this.open();
        try {
            mSqlDatabase.delete(mTable.getTableName(), mNameField.getName() + " =?", new String[]{galleryName});
        } finally {
            this.close();
        }
    }

    public SavedGallery getSavedGalleryByName(String galleryName) {
        this.open();
        Cursor cursor = mSqlDatabase.query(
                mTable.getTableName(),
                mTable.getTableColumnNames(),
                mNameField.getName() + "= '" + galleryName +"'",
                null, null, null, null);
        try {

            if (cursor.getCount() > 0){
                Log.d(TAG, galleryName + " found in db, returning.");
                cursor.moveToFirst();

                SavedGallery gallery = new SavedGallery();
                gallery.name = cursor.getString(0);
                return gallery;
            }

            Log.d(TAG, galleryName + " not found in database, returning null.");
            return null;
        }  finally {
            cursor.close();
            this.close();
        }
    }

    public List<SavedGallery> getAllGalleries() {
        List<SavedGallery> galleries = new ArrayList<SavedGallery>();

        Cursor cursor = null;
        try {
            this.open();
            cursor = mSqlDatabase.query(mTable.getTableName(), mTable.getTableColumnNames(),
                    null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                SavedGallery gallery = new SavedGallery();
                gallery.name = cursor.getString(0);
                galleries.add(gallery);

                cursor.moveToNext();
            }
            return galleries;
        }  finally {
            if (cursor != null)
                cursor.close();
            this.close();
        }
    }
}
