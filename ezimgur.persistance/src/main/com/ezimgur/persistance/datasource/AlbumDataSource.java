package com.ezimgur.persistance.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.ezimgur.datacontract.Album;
import com.ezimgur.persistance.database.DatabaseField;
import com.ezimgur.persistance.database.DatabaseTable;

import java.util.Date;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/29/12
 * Time: 7:36 PM
 */
public class AlbumDataSource extends DataSource {

    private static AlbumDataSource dataSource;

    private static DatabaseField mId = new DatabaseField(DatabaseField.FieldType.TEXT, "id", false, true);
    private static DatabaseField mTitle = new DatabaseField(DatabaseField.FieldType.TEXT, "title", true, false);
    private static DatabaseField mDescription = new DatabaseField(DatabaseField.FieldType.TEXT, "description", true, false);
    private static DatabaseField mPrivacy = new DatabaseField(DatabaseField.FieldType.TEXT, "privacy", true, false);
    private static DatabaseField mCover = new DatabaseField(DatabaseField.FieldType.TEXT, "cover", false, false);
    private static DatabaseField mOrder = new DatabaseField(DatabaseField.FieldType.INTEGER, "album_order", true, false);
    private static DatabaseField mLayout = new DatabaseField(DatabaseField.FieldType.TEXT, "layout", true, false);
    private static DatabaseField mDateCreated = new DatabaseField(DatabaseField.FieldType.INTEGER, "date_created", false, false);
    private static DatabaseField mLink = new DatabaseField(DatabaseField.FieldType.TEXT, "link", true, false);

    public static AlbumDataSource newInstance(Context context) {
        return new AlbumDataSource(context);
    }

    public DatabaseTable initTable() {
        mTable = new DatabaseTable("albums");
        mTable.addTableField(mId);
        mTable.addTableField(mTitle);
        mTable.addTableField(mDescription);
        mTable.addTableField(mPrivacy);
        mTable.addTableField(mCover);
        mTable.addTableField(mOrder);
        mTable.addTableField(mLayout);
        mTable.addTableField(mDateCreated);
        mTable.addTableField(mLink);
        return mTable;
    }

    private AlbumDataSource(Context context) {
        super(context);
        if (mTable == null)
            initTable();
    }

    public Album createAlbum(Album album) {
        ContentValues values = new ContentValues();
        values.put(mId.getName(), album.id);
        values.put(mTitle.getName(), album.title);
        values.put(mDescription.getName(), album.description);
        values.put(mPrivacy.getName(), album.privacy);
        values.put(mCover.getName(), album.cover);
        values.put(mOrder.getName(), album.order);
        values.put(mLayout.getName(), album.layout);
        values.put(mDateCreated.getName(), album.dateCreated.getTime());
        values.put(mLink.getName(), album.link);

        Album savedAlbum = getAlbumById(album.id);
        if (savedAlbum == null){
            insertNewRecord(values);
        } else {
            return savedAlbum;
        }

        return getAlbumById(album.id);

    }

    public Album getAlbumById(String albumId) {
        this.open();
        Cursor cursor = mSqlDatabase.query(
                mTable.getTableName(),
                mTable.getTableColumnNames(),
                mId.getName() + " = '" + albumId + "'",
                null, null, null, null);

        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                Album album = new Album();
                album.id = cursor.getString(0);
                album.title = cursor.getString(1);
                album.description = cursor.getString(2);
                album.privacy = cursor.getString(3);
                album.cover = cursor.getString(4);
                album.order = cursor.getInt(5);
                album.layout = cursor.getString(6);
                album.dateCreated = new Date(cursor.getLong(7));
                album.link = cursor.getString(7);

                return album;
            }

            return null;
        } finally {
            cursor.close();
            this.close();
        }
    }

    public boolean albumExists(String albumId) {
        this.open();
        Cursor cursor = mSqlDatabase.query(
                mTable.getTableName(),
                mTable.getTableColumnNames(),
                mId.getName() + " = '" + albumId + "'",
                null, null, null, null);

        try {
            if (cursor.getCount() > 0)
                return true;
            else
                return false;
        } finally {
            cursor.close();
            this.close();
        }
    }

    /*
        public String id;
    public String title;
    public String description ;
    public String privacy;
    public String cover;
    public int order;
    public String layout;
    public Date dateCreated;
    public String link;
    public List<Image> images;
     */
}
