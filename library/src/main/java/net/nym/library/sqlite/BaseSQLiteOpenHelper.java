/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.sqlite;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;


import net.nym.library.common.BaseApplication;
import net.nym.library.entity.Entities;
import net.nym.library.entity.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


/**
 * @author nym
 * @date 2014/10/27 0027.
 * @since 1.0
 */
public class BaseSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = BaseSQLiteOpenHelper.class.getSimpleName();
    private static final int VERSION = 1;
    private static BaseSQLiteOpenHelper my = null;
    public final static String NAME = "ddp.db";

    public static BaseSQLiteOpenHelper getInstance() {
        if (my == null) {
            my = new BaseSQLiteOpenHelper(BaseApplication.getAppContext());
        }

        return my;
    }

    public BaseSQLiteOpenHelper(Context context){
        this(context, NAME, null, VERSION);
    }
    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public BaseSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        this(context, name, factory, version, null);
    }

    /**
     * Create a helper object to create, open, and/or manage a database.
     * The database is not actually created or opened until one of
     * {@link #getWritableDatabase} or {@link #getReadableDatabase} is called.
     * <p/>
     * <p>Accepts input param: a concrete instance of {@link android.database.DatabaseErrorHandler} to be
     * used to handle corruption when sqlite reports database corruption.</p>
     *
     * @param context      to use to open or create the database
     * @param name         of the database file, or null for an in-memory database
     * @param factory      to use for creating cursor objects, or null for the default
     * @param version      number of the database (starting at 1); if the database is older,
     *                     {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                     newer, {@link #onDowngrade} will be used to downgrade the database
     * @param errorHandler the {@link android.database.DatabaseErrorHandler} to be used when sqlite reports database
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BaseSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
//        createTable(PushInfo.class,db);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Query the given table, returning a {@link android.database.Cursor} over the result set.
     *
     * @param table
     *            The table name to compile the query against.
     * @param columns
     *            A list of which columns to return. Passing null will return
     *            all columns, which is discouraged to prevent reading data from
     *            storage that isn't going to be used.
     * @param selection
     *            A filter declaring which rows to return, formatted as an SQL
     *            WHERE clause (excluding the WHERE itself). Passing null will
     *            return all rows for the given table.
     * @param selectionArgs
     *            You may include ?s in selection, which will be replaced by the
     *            values from selectionArgs, in order that they appear in the
     *            selection. The values will be bound as Strings.
     * @param groupBy
     *            A filter declaring how to group rows, formatted as an SQL
     *            GROUP BY clause (excluding the GROUP BY itself). Passing null
     *            will cause the rows to not be grouped.
     * @param having
     *            A filter declare which row groups to include in the cursor, if
     *            row grouping is being used, formatted as an SQL HAVING clause
     *            (excluding the HAVING itself). Passing null will cause all row
     *            groups to be included, and is required when row grouping is
     *            not being used.
     * @param orderBy
     *            How to order the rows, formatted as an SQL ORDER BY clause
     *            (excluding the ORDER BY itself). Passing null will use the
     *            default sort order, which may be unordered.
     * @return A {@link android.database.Cursor} object, which is positioned before the first
     *         entry. Note that {@link android.database.Cursor}s are not synchronized, see the
     *         documentation for more details.
     * @see android.database.Cursor
     */
    public Cursor select(String table, String[] columns, String selection,
                         String[] selectionArgs, String groupBy, String having,
                         String orderBy) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(table, columns, selection, selectionArgs,
                groupBy, having, orderBy);
        return cursor;
    }

    /**
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @return
     */
    public Cursor select(String table, String[] columns, String selection,
                         String[] selectionArgs) {
        return select(table, columns, selection, selectionArgs, null, null,
                null);
    }

    /**
     * Convenience method for inserting a row into the database.
     *
     * @param table
     *            the table to insert the row into
     * @param nullColumnHack
     *            optional; may be <code>null</code>. SQL doesn't allow
     *            inserting a completely empty row without naming at least one
     *            column name. If your provided <code>values</code> is empty, no
     *            column names are known and an empty row can't be inserted. If
     *            not set to null, the <code>nullColumnHack</code> parameter
     *            provides the name of nullable column name to explicitly insert
     *            a NULL into in the case where your <code>values</code> is
     *            empty.
     * @param values
     *            this map contains the initial column values for the row. The
     *            keys should be the column names and the values the column
     *            values
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public synchronized long insert(String table, String nullColumnHack,
                                    ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();

        long flag = db.replace(table, nullColumnHack, values);
        db.close();
        return flag;
    }

    /**
     * Convenience method for updating rows in the database.
     *
     * @param table
     *            the table to update in
     * @param values
     *            a map from column names to new column values. null is a valid
     *            value that will be translated to NULL.
     * @param whereClause
     *            the optional WHERE clause to apply when updating. Passing null
     *            will update all rows.
     * @return the number of rows affected
     */
    public synchronized int update(String table, ContentValues values,
                                   String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();
        int flag = db.update(table, values, whereClause, whereArgs);
        db.close();
        return flag;
    }

    /**
     * @param table
     * @param whereClause
     * @param whereArgs
     * @return the number of rows affected if a whereClause is passed in, 0
     *         otherwise. To remove all rows and get a count pass "1" as the
     *         whereClause.
     */
    public synchronized int delete(String table, String whereClause,
                                   String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();
        int flag = db.delete(table, whereClause, whereArgs);
        db.close();
        return flag;
    }


    /**
     * 以类名为表名，支持的数据类型（String,int/Integer,float/Float,double/Double,long/Long,boolean/Boolean）
     * 供外部调用
     * @param clazz
     *
     *
     */
    public <T extends Entity> void createTable(Class<T> clazz)
    {
        StringBuffer sb = new StringBuffer("CREATE TABLE ");
        sb.append(clazz.getSimpleName()).append("( ");
        string(sb,clazz);
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT )");
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL(sb.toString());
            db.setTransactionSuccessful();
            Log.i(TAG,"create table " + clazz.getName() + " successful.");
        } finally {
            db.endTransaction();
            db.close();
        }

    }

    /**
    * 供onCreate调用
    * */
    private <T extends Entity> void createTable(Class<T> clazz,SQLiteDatabase db)
    {
        StringBuffer sb = new StringBuffer("CREATE TABLE ");
        sb.append(clazz.getSimpleName()).append("( ");
        string(sb,clazz);
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT )");
        db.beginTransaction();
        try {
            db.execSQL(sb.toString());
            db.setTransactionSuccessful();
            Log.i(TAG,"create table " + clazz.getName() + " successful.");
        } finally {
            db.endTransaction();
        }

    }

    public synchronized <T extends Entity> long insertToTable(T object)
    {
        ContentValues values = mapContentValues(object);

        return insert(object.getClass().getSimpleName(),null,values);
    }

    public synchronized <T extends Entity> int delete(T object,String whereClause,
                                                      String[] whereArgs)
    {
        return delete(object.getClass().getSimpleName(),whereClause,whereArgs);
    }

    public  <T extends Entity> Entities<T> getEntities(Class<T> clazz)
    {
        Entities<T> entities = new Entities<T>();
        Cursor cursor = select(clazz.getSimpleName(), new String[] { "*" },
                null, null, null, null, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst()) {
                do {
                    T record = mapEntity(clazz,cursor);
                    if (record != null)
                    {
                        entities.add(record);
                    }

                } while (cursor.moveToNext());
            }
        }
        return  entities;
    }

    private  ContentValues mapContentValues(Object object)
    {
        ContentValues values = new ContentValues();
        try {
            for (Field item : object.getClass().getDeclaredFields()) {

                if (Modifier.toString(item.getModifiers()).contains("static"))
                {
                    //不要static修饰的属性
                    continue;
                }
                boolean accessFlag = item.isAccessible();
                /**
                 * 设置是否有权限访问反射类中的私有属性的
                 * */
                item.setAccessible(true);
                String type = item.getType().toString();
                if (type.equals("class java.lang.String"))
                {
                    values.put(item.getName(),(String)item.get(object));
                }
                else if ("class java.lang.Integer".equals(item.getType()) | "int".equals(type))
                {
                    values.put(item.getName(),item.getInt(object));
                }
                else if ("class java.lang.Long".equals(type) | "long".equals(type))
                {
                    values.put(item.getName(),item.getLong(object));
                }
                else if ("class java.lang.Double".equals(type) | "double".equals(type))
                {
                    values.put(item.getName(),item.getDouble(object));
                }
                else if ("class java.lang.Float".equals(type) | "float".equals(type))
                {
                    values.put(item.getName(),item.getFloat(object));
                }
                else if ("class java.lang.Boolean".equals(type) | "boolean".equals(type))
                {
                    values.put(item.getName(),item.getBoolean(object));
                }

                item.setAccessible(accessFlag);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return values;
    }
    private  <T > T mapEntity(Class<T> clazz,Cursor cursor)
    {
        T record = null;
        try {
            record = clazz.newInstance();
            for (Field item : clazz.getDeclaredFields()) {

                if (Modifier.toString(item.getModifiers()).contains("static"))
                {
                    //不要static修饰的属性
                    continue;
                }

                if("_id".equals(item.getName()))
                {
                    continue;
                }
                boolean accessFlag = item.isAccessible();
                /**
                 * 设置是否有权限访问反射类中的私有属性的
                 * */
                item.setAccessible(true);
                String type = item.getType().toString();
                if (type.equals("class java.lang.String"))
                {
                    item.set(record,cursor.getString(cursor.getColumnIndex(item.getName())));
                }
                else if ("class java.lang.Integer".equals(type) | "int".equals(type))
                {
                    item.setInt(record, cursor.getInt(cursor.getColumnIndex(item.getName())));
                }
                else if ("class java.lang.Long".equals(type) | "long".equals(type))
                {
                    item.setLong(record, cursor.getLong(cursor.getColumnIndex(item.getName())));
                }
                else if ("class java.lang.Double".equals(type) | "double".equals(type))
                {
                    item.setDouble(record, cursor.getDouble(cursor.getColumnIndex(item.getName())));
                }
                else if ("class java.lang.Float".equals(type) | "float".equals(type))
                {
                    item.setFloat(record, cursor.getFloat(cursor.getColumnIndex(item.getName())));
                }
                else if ("class java.lang.Boolean".equals(type) | "boolean".equals(type))
                {
                    if ("true".equals(cursor.getString(cursor.getColumnIndex(item.getName()))))
                    {
                        item.setBoolean(record,true);
                    }
                    else
                    {
                        item.setBoolean(record,false);
                    }
                }

                item.setAccessible(accessFlag);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return record;
    }

    private static void string(StringBuffer sb, Class clazz) {
        for (Field item : clazz.getDeclaredFields()) {

            if (Modifier.toString(item.getModifiers()).contains("static"))
            {
                //不要static修饰的属性
                continue;
            }
            sb.append(item.getName()).append(" ").append(getSQLiteType(item.getType().toString()) + "").append(",");
        }
    }

    private static String getSQLiteType(String type)
    {
        if ("class java.lang.String".equals(type))
        {
            return "TEXT";
        }
        if ("class java.lang.Integer".equals(type) | "int".equals(type) | "class java.lang.Long".equals(type) | "long".equals(type))
        {
            return "INTEGER";
        }
        if ("class java.lang.Double".equals(type) | "double".equals(type) | "class java.lang.Float".equals(type) | "float".equals(type))
        {
            return "REAL";
        }
        if ("class java.lang.Boolean".equals(type) | "boolean".equals(type))
        {
            return "BOOLEAN";
        }

        return null;
    }
}
