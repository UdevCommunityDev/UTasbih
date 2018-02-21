package org.udevcommunity.utasbih.utasbih;

/**
 * UTasbihSQLiteHelper
 *
 * This Class is used to communicate with the database
 *
 * @package :
 * @author : UDevCommunity <Contact@UDevCommunity.com>
 * @license :
 * @link : https://github.com/ztickm/UTasbih
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

public class UTasbihSQLiteHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;  // Database Version
    private static final String DATABASE_NAME = "UTasbihD";     // Database Name

    public UTasbihSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create utasbih table
        String CREATE_BOOK_TABLE = "CREATE TABLE utasbih ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "mode INTEGER, "+
                "day DATE DEFAULT CURRENT_DATE, "+
                "number INTEGER )";

        // create utasbih table
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table(utasbih) table if existed
        db.execSQL("DROP TABLE IF EXISTS utasbih");

        // create new utasbih table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------


/*
add,delete,update,get function
*/

    // Utasbih table name
    private static final String TABLE_UTASBIH = "utasbih";

    // utasbih Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_MODE = "mode";
    private static final String KEY_DAY = "day";
    private static final String KEY_NUMBER = "number";

    private static final String[] COLUMNS = {KEY_ID, KEY_MODE, KEY_DAY, KEY_NUMBER};

    // Add new Record to the database or just update it
    public void addStat(int mode, int number)
    {
        DayInfo temp = getInfo(mode);
        if (temp != null)
        {
            temp.setNumber(temp.getNumber()+number);
            updateInfo(temp);
        }
        else
        {
            addInfo(new DayInfo(0, mode, new Date(System.currentTimeMillis()), number));
        }
    }

    // Add new Record to the database as object
    public void addInfo(DayInfo info){
        Log.d("addBook", info.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_MODE, Integer.toString(info.getMode())); // get mode
        values.put(KEY_DAY, info.getDay().toString()); // get Day as string
        values.put(KEY_NUMBER, Integer.toString(info.getNumber())); // get number

        // 3. insert
        db.insert(TABLE_UTASBIH, null, values);

        // 4. close
        db.close();
    }

    public DayInfo getInfo(int mode){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        DayInfo info = new DayInfo();

        // 2. build query
        Cursor cursor;
        cursor = null;
        try {
            cursor = db.query(TABLE_UTASBIH, // a. table
                    COLUMNS, // b. column names
                    "day = date('now') AND mode = ?", // c. selections
                    new String[]{String.valueOf(mode)}, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // 3. if we got results get the first one
        if (cursor.moveToFirst()) {

        // 4. build DayInfo object
        info.setId(Integer.parseInt(cursor.getString(0)));
        info.setMode(Integer.parseInt(cursor.getString(1)));
        info.setDay(Date.valueOf(cursor.getString(2)));
        info.setNumber(Integer.parseInt(cursor.getString(3)));
        }
        else {
            return null;
        }

        cursor.close();
        // 5. return info
        return info;
    }

    // Get All Infos
    public List<DayInfo> getAllInfos(int mode) {
        List<DayInfo> infos = new LinkedList<DayInfo>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_UTASBIH + " WHERE mode = " + Integer.toString(mode);

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build DayInfo and add it to list
        DayInfo info = null;
        if (cursor.moveToFirst()) {
            do {
                info = new DayInfo();
                info.setId(Integer.parseInt(cursor.getString(0)));
                info.setMode(Integer.parseInt(cursor.getString(1)));
                info.setDay(Date.valueOf(cursor.getString(2)));
                info.setNumber(Integer.parseInt(cursor.getString(3)));

                // Add info to infos
                infos.add(info);
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();
        // return infos
        return infos;
    }

    // Updating single info
    public int updateInfo(DayInfo info) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_NUMBER, info.getNumber());

        // 3. updating row
        int i = db.update(TABLE_UTASBIH, values, KEY_ID+" = ?", new String[] { String.valueOf(info.getId()) });

        // 4. close
        db.close();

        return i;
    }

}