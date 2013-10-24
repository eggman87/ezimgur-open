package com.ezimgur.persistance.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/17/12
 * Time: 3:56 PM
 */
public class DatabaseTable {

    private String mTableName;
    private List<DatabaseField> mTableFields = new ArrayList<DatabaseField>();

    public DatabaseTable(String tableName) {
          mTableName = tableName;
    }

    public void addTableField(DatabaseField field) {
        mTableFields.add(field);
    }

    public String getTableName() {
        return mTableName;
    }

    public List<DatabaseField> getTableFields(){
        return mTableFields;
    }

    public String getCreateSqlText() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("CREATE TABLE %s (%s)", mTableName, getFieldCreateSqlText()));
        return builder.toString();
    }

    public String[] getTableColumnNames() {
        String[] names = new String[mTableFields.size()];
        for (int i=0; i<names.length; i++){
            names[i] = mTableFields.get(i).getName();
        }

        return names;
    }

    private String getFieldCreateSqlText() {
        StringBuilder builder = new StringBuilder();
        for (int i =0; i<mTableFields.size(); i++){
            builder.append(mTableFields.get(i).getSqlCreateText());
            if (i < (mTableFields.size()-1))
                builder.append(',');
        }
        return builder.toString();
    }
}
