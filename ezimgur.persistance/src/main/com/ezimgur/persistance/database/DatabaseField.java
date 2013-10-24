package com.ezimgur.persistance.database;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/17/12
 * Time: 3:57 PM
 */
public class DatabaseField {

    private FieldType mType;
    private String mFieldName;
    private boolean mNullable;
    private boolean mIsPrimary;
    private boolean mAutoIncrement;

    public  DatabaseField(FieldType type, String fieldName, boolean nullable) {
         this(type, fieldName, nullable, false);
    }

    public DatabaseField(FieldType type, String fieldName, boolean nullable, boolean isPrimary){
        mType = type;
        mFieldName = fieldName;
        mIsPrimary = isPrimary;
        mNullable = nullable;
    }

    public void setAutoIncrement(boolean autoIncrement){
        mAutoIncrement = autoIncrement;
    }

    public String getName() {
        return mFieldName;
    }

    public String getSqlCreateText() {
        //column name, type, primarykey, nullable
       return String.format("%s %s %s %s ", mFieldName, mType, getFieldPrimaryKey(), getFieldIsNullable());
    }

    private String getFieldPrimaryKey() {
        StringBuilder builder = new StringBuilder();
        if (mIsPrimary)
            builder.append("primary key");
        if (mAutoIncrement)
            builder.append("autoincrement");
        return builder.toString();
    }

    private String getFieldIsNullable() {
        return mNullable ? "":"not null";
    }

    public enum FieldType {
        TEXT,
        INTEGER,
        REAL,
        BLOB;
    }
}
