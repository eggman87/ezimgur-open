package com.ezimgur.persistance.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.ezimgur.datacontract.Image;
import com.ezimgur.persistance.database.DatabaseField;
import com.ezimgur.persistance.database.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/29/12
 * Time: 7:52 PM
 */
public class ImageDataSource extends DataSource {

    private static ImageDataSource dataSource;

    private static DatabaseField mId = new DatabaseField(DatabaseField.FieldType.TEXT, "id", false, true);
    private static DatabaseField mTitle = new DatabaseField(DatabaseField.FieldType.TEXT, "title", true, false);
    private static DatabaseField mDescription = new DatabaseField(DatabaseField.FieldType.TEXT, "description", true, false);
    private static DatabaseField mDateCreated = new DatabaseField(DatabaseField.FieldType.INTEGER, "date_created", false, false);
    private static DatabaseField mMimeType = new DatabaseField(DatabaseField.FieldType.TEXT, "mime_type", false, false);
    private static DatabaseField mAnimated = new DatabaseField(DatabaseField.FieldType.INTEGER, "animated", false, false);
    private static DatabaseField mWidth = new DatabaseField(DatabaseField.FieldType.INTEGER, "width", true, false);
    private static DatabaseField mHeight = new DatabaseField(DatabaseField.FieldType.INTEGER, "height", true, false);
    private static DatabaseField mSize = new DatabaseField(DatabaseField.FieldType.INTEGER, "size", true, false);
    private static DatabaseField mViews = new DatabaseField(DatabaseField.FieldType.INTEGER, "views", true, false);
    private static DatabaseField mBandwidth = new DatabaseField(DatabaseField.FieldType.TEXT, "bandwidth", true, false);
    private static DatabaseField mDeleteHash = new DatabaseField(DatabaseField.FieldType.TEXT, "delete_hash", true, false);
    private static DatabaseField mAccountUrl = new DatabaseField(DatabaseField.FieldType.TEXT, "account_url", true, false);

    public static ImageDataSource newInstance(Context context) {
        return new ImageDataSource(context);
    }

    public DatabaseTable initTable() {
        mTable = new DatabaseTable("images");
        mTable.addTableField(mId);
        mTable.addTableField(mTitle);
        mTable.addTableField(mDescription);
        mTable.addTableField(mDateCreated);
        mTable.addTableField(mMimeType);
        mTable.addTableField(mAnimated);
        mTable.addTableField(mWidth);
        mTable.addTableField(mHeight);
        mTable.addTableField(mSize);
        mTable.addTableField(mViews);
        mTable.addTableField(mBandwidth);
        mTable.addTableField(mDeleteHash);
        mTable.addTableField(mAccountUrl);
        return mTable;
    }

    private ImageDataSource(Context context) {
        super(context);
        if (mTable == null)
            initTable();
    }

    public Image createImage(Image image) {
        ContentValues values = new ContentValues();
        values.put(mId.getName(), image.id);
        values.put(mTitle.getName(), image.title);
        values.put(mDescription.getName(), image.description);
        values.put(mDateCreated.getName(), image.dateCreated.getTime());
        values.put(mMimeType.getName(), image.mimeType);
        values.put(mAnimated.getName(), image.animated);
        values.put(mWidth.getName(), image.width);
        values.put(mHeight.getName(), image.height);
        values.put(mSize.getName(), image.size);
        values.put(mViews.getName(), image.views);
        values.put(mBandwidth.getName(), image.bandwidth);
        values.put(mDeleteHash.getName(), image.deleteHash);
        values.put(mAccountUrl.getName(), image.accountUrl);

        Image savedImage = getImageById(image.id);
        if (savedImage == null) {
            insertNewRecord(values);
        } else {
            return savedImage;
        }

        return getImageById(image.id);
    }

    public Image getImageById(String imageId)  {
        this.open();
        Cursor cursor = mSqlDatabase.query(
                mTable.getTableName(),
                mTable.getTableColumnNames(),
                mId.getName() + " = '" + imageId+"'",
                null, null, null, null);

        try {
            if (cursor.getCount() > 0){
                cursor.moveToFirst();

                return  getImageFromCursor(cursor);
            }

            return null;
        } finally {
            cursor.close();
            this.close();
        }
    }

    public List<Image> getImagesByAlbumId(String albumId) {
        this.open();
        String sql = String.format("SELECT * FROM images I JOIN album_images AI ON I.id = AI.image_id WHERE AI.album_id ='%s'", albumId);
        Cursor cursor = mSqlDatabase.rawQuery(sql, null);
        try {
            List<Image> images = new ArrayList<Image>();
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()){
                  Image image = getImageFromCursor(cursor);
                  images.add(image);
                }
            }

            return images;
        } finally {
            cursor.close();
            this.close();
        }
    }

    private Image getImageFromCursor(Cursor cursor){
        Image image = new Image();
        image.id = cursor.getString(0);
        image.title = cursor.getString(1);
        image.description = cursor.getString(2);
        image.dateCreated = new Date(cursor.getLong(3));
        image.mimeType = cursor.getString(4);
        image.animated = cursor.getInt(5) == 1 ? true:false;
        image.width = cursor.getInt(6);
        image.height = cursor.getInt(7);
        image.size = cursor.getInt(8);
        image.views = cursor.getInt(9);
        image.bandwidth = cursor.getString(10);
        image.deleteHash = cursor.getString(11);
        image.accountUrl = cursor.getString(12);
        return image;
    }
}
