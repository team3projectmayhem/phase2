package com.example.dad.nsatracker;

// DBTools.java

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

// SQLiteOpenHelper helps you open or create a database


/**
 * SQLlite Database class. It stores all the location information.
 */
public class DatabaseStuff extends SQLiteOpenHelper implements Serializable {

    // Context : provides access to application-specific resources and classes

    public DatabaseStuff(Context applicationcontext) {

        // Call use the database or to create it

        super(applicationcontext, "locations.db", null, 1);

    }

    // onCreate is called the first time the database is created

    public void onCreate(SQLiteDatabase database) {

        // How to create a table in SQLite
        // Make sure you don't put a ; at the end of the query

        String query = "CREATE TABLE locations ( source STRING PRIMARY KEY, latitude TEXT, " +
                "longitude TEXT, speed TEXT, heading TEXT, timestamp INTEGER)";

        // Executes the query provided as long as the query isn't a select
        // or if the query doesn't return any data

        database.execSQL(query);

    }

    // onUpgrade is used to drop tables, add tables, or do anything
    // else it needs to upgrade
    // This is droping the table to delete the data and then calling
    // onCreate to make an empty table

    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query = "DROP TABLE IF EXISTS locations";

        // Executes the query provided as long as the query isn't a select
        // or if the query doesn't return any data

        database.execSQL(query);
        onCreate(database);
    }

    public void insertContact(HashMap<String, String> queryValues) {

        // Open a database for reading and writing

        SQLiteDatabase database = this.getWritableDatabase();

        // Stores key value pairs being the column name and the data
        // ContentValues data type is needed because the database
        // requires its data type to be passed

        ContentValues values = new ContentValues();

        values.put("source", queryValues.get("source"));
        values.put("latitude", queryValues.get("latitude"));
        values.put("longitude", queryValues.get("longitude"));
        values.put("speed", queryValues.get("speed"));
        values.put("heading", queryValues.get("heading"));
        values.put("timestamp", queryValues.get("timestamp"));

        // Inserts the data in the form of ContentValues into the
        // table name provided

        //database.insert("locations", null, values);

        // Release the reference to the SQLiteDatabase object

        database.close();
    }

//    public int updateContact(HashMap<String, String> queryValues) {
//
//        // Open a database for reading and writing
//
//        SQLiteDatabase database = this.getWritableDatabase();
//
//        // Stores key value pairs being the column name and the data
//
//        ContentValues values = new ContentValues();
//
//        values.put("source", queryValues.get("source"));
//        values.put("latitude", queryValues.get("latitude"));
//        values.put("longitude", queryValues.get("longitude"));
//        values.put("speed", queryValues.get("speed"));
//        values.put("heading", queryValues.get("heading"));
//        values.put("timestamp", queryValues.get("timestamp"));
//
//        // update(TableName, ContentValueForTable, WhereClause, ArgumentForWhereClause)
//
//        return database.update("locations", values, "contactId" + " = ?", new String[] { queryValues.get("contactId") });
//    }

    // Used to delete a contact with the matching contactId

    public void deleteContact(String source) {

        // Open a database for reading and writing

        SQLiteDatabase database = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM  locations where source='"+ source +"'";

        // Executes the query provided as long as the query isn't a select
        // or if the query doesn't return any data

        database.execSQL(deleteQuery);
    }

    public ArrayList<HashMap<String, String>> getAllContacts() {

        // ArrayList that contains every row in the database
        // and each row key / value stored in a HashMap

        ArrayList<HashMap<String, String>> contactArrayList;

        contactArrayList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT  * FROM source";

        // Open a database for reading and writing

        SQLiteDatabase database = this.getWritableDatabase();

        // Cursor provides read and write access for the
        // data returned from a database query

        // rawQuery executes the query and returns the result as a Cursor

        Cursor cursor = database.rawQuery(selectQuery, null);

        // Move to the first row

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> contactMap = new HashMap<String, String>();

                // Store the key / value pairs in a HashMap
                // Access the Cursor data by index that is in the same order
                // as used when creating the table

                contactMap.put("source", cursor.getString(0));
                contactMap.put("latitude", cursor.getString(1));
                contactMap.put("longitude", cursor.getString(2));
                contactMap.put("speed", cursor.getString(3));
                contactMap.put("heading", cursor.getString(4));
                contactMap.put("timestamp", cursor.getString(5));

                contactArrayList.add(contactMap);
            } while (cursor.moveToNext()); // Move Cursor to the next row
        }

        // return contact list
        return contactArrayList;
    }

//    public HashMap<String, String> getContactInfo(String userName) {
//        HashMap<String, String> contactMap = new HashMap<String, String>();
//
//        // Open a database for reading only
//
//        SQLiteDatabase database = this.getReadableDatabase();
//
//        String selectQuery = "SELECT * FROM contacts where userName='"+userName+"'";
//
//        // rawQuery executes the query and returns the result as a Cursor
//
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        if (cursor.moveToFirst()) {
//            do {
//
//                contactMap.put("username", cursor.getString(1));
//                contactMap.put("password", cursor.getString(2));
//                contactMap.put("secretQuestion", cursor.getString(3));
//                contactMap.put("secretAnswer", cursor.getString(4));
//
//            } while (cursor.moveToNext());
//        }
//        String x = contactMap.get("username");
//        if(x != null)
//            Log.v("test", "hello");
//        return contactMap;
//    }
}
