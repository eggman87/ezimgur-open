package com.ezimgur.persistance.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.ezimgur.persistance.database.DatabaseField;
import com.ezimgur.persistance.database.DatabaseTable;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/29/12
 * Time: 8:04 PM
 */
public class AlbumImagesDataSource extends DataSource {

    private static AlbumImagesDataSource dataSource;

    private static DatabaseField mAlbumImageIdField = new DatabaseField(DatabaseField.FieldType.TEXT, "album_image_id", false, true);
    private static DatabaseField mAlbumIdField = new DatabaseField(DatabaseField.FieldType.TEXT, "album_id", false, false);
    private static DatabaseField mImageIdField = new DatabaseField(DatabaseField.FieldType.TEXT, "image_id", false, false);

    public static AlbumImagesDataSource newInstance(Context context) {
        if (dataSource == null)
            dataSource = new AlbumImagesDataSource(context);
        return dataSource;
    }

    private AlbumImagesDataSource(Context context) {
        super(context);
        if (mTable == null)
            initTable();
    }

    public DatabaseTable initTable() {
        mTable = new DatabaseTable("album_images");
        mTable.addTableField(mAlbumImageIdField);
        mTable.addTableField(mAlbumIdField);
        mTable.addTableField(mImageIdField);
        return mTable;
    }

    public AlbumImage createAlbumImage(String albumId, String imageId) {

        AlbumImage albumImage = getAlbumImage(albumId, imageId);
        if (albumImage == null) {
            ContentValues values = new ContentValues();
            values.put(mAlbumImageIdField.getName(), albumId + imageId);
            values.put(mAlbumIdField.getName(), albumId);
            values.put(mImageIdField.getName(), imageId);

            try {
                this.open();
                long rowId = mSqlDatabase.insert(mTable.getTableName(), null, values);
            } finally {
                this.close();
            }
        } else {
            return albumImage;
        }

        return getAlbumImage(albumId, imageId);
    }

    public AlbumImage getAlbumImage(String albumId, String imageId){
        this.open();
        Cursor cursor = mSqlDatabase.query(
                mTable.getTableName(),
                mTable.getTableColumnNames(),
                mAlbumImageIdField.getName() + "= '" + albumId + imageId +"'",
                null,null,null,null);
        try {

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                AlbumImage albumImage = new AlbumImage();
                albumImage.albumImageId = cursor.getString(0);
                albumImage.albumId = cursor.getString(1);
                albumImage.imageId = cursor.getString(2);
                return albumImage;
            }
            return null;
        } finally {
            cursor.close();
            this.close();
        }
    }

    public void deleteAlbumImage(String albumId, String imageId) {
        mSqlDatabase.delete(mTable.getTableName(), mAlbumImageIdField.getName() + " =?", new String[]{albumId+imageId});
    }


    public class AlbumImage {
        public String albumImageId;
        public String albumId;
        public String imageId;
    }
}
