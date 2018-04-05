package com.example.julianramirez.taskby;

/**
 * Created by Julian Ramirez on 3/16/2018.
 */

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.content.ContentValues;
import android.content.Context;

public class DatabaseConnector {

    private static final String DATABASE_NAME = "UserTasks";
    private SQLiteDatabase database;
    private DatabaseOpenHelper databaseOpenHelper;

    public DatabaseConnector(Context context){
        databaseOpenHelper =
                new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
    }

    public void open() throws SQLException {
        database = databaseOpenHelper.getWritableDatabase();
    }

    public void close(){
        if (database != null)
            database.close();
    }

    public void insertTask( String fullTask, String name, String details, String address, String fulldate, String month, String day, String year, String hours, String minutes, String timeOfDay){
        ContentValues newTask = new ContentValues();
        newTask.put("fullTask", fullTask);
        newTask.put("name", name);
        newTask.put("details", details);
        newTask.put("address", address);
        newTask.put("fulldate", fulldate);
        newTask.put("month", month);
        newTask.put("day", day);
        newTask.put("year", year);
        newTask.put("hours", hours);
        newTask.put("minutes", minutes);
        newTask.put("timeOfDay", timeOfDay);
        /*ContentValues oldTask = new ContentValues();
        oldTask.put("name", name);
        oldTask.put("details", details);
        oldTask.put("address", address);
        oldTask.put("fulldate", fulldate);
        oldTask.put("month", month);
        oldTask.put("day", day);
        database.insert("completed", null, oldTask);*/
        open();
        database.insert("tasks", null, newTask);
        close();
    }

    public void updateTask(long id, String fullTask, String name, String details, String address, String fulldate, String month, String day, String year, String hours, String minutes, String timeOfDay){
        ContentValues editTask = new ContentValues();
        editTask.put("fullTask", fullTask);
        editTask.put("name", name);
        editTask.put("details", details);
        editTask.put("address", address);
        editTask.put("fulldate", fulldate);
        editTask.put("month", month);
        editTask.put("day", day);
        editTask.put("year", year);
        editTask.put("hours", hours);
        editTask.put("minutes", minutes);
        editTask.put("timeOfDay", timeOfDay);
        open();
        database.update("tasks", editTask, "_id=" + id, null);
        close();
    }
    public void completeTask(long id, String fullTask, String name, String details, String address, String fulldate, String month, String day, String year, String hours, String minutes, String timeOfDay){
        ContentValues editTask = new ContentValues();
        editTask.put("fullTask", fullTask);
        editTask.put("name", name);
        editTask.put("details", details);
        editTask.put("address", address);
        editTask.put("fulldate", fulldate);
        editTask.put("month", month);
        editTask.put("day", day);
        editTask.put("year", year);
        editTask.put("hours", hours);
        editTask.put("minutes", minutes);
        editTask.put("timeOfDay", timeOfDay);
        open();
        database.insert("completed",  null, editTask);
        close();
    }

    public Cursor getAllTasks(){
        return database.query("tasks", new String[] {"_id", "name", "fulldate", "fullTask"},
                null, null, null, null, "month");

    }
    public Cursor getAllCompletedTasks(){
        return database.query("completed", new String[] {"_id", "name", "fulldate", "fullTask"},
                null, null, null, null, "month");

    }

    public Cursor getOneTask(long id){
        return database.query(
                "tasks", null, "_id=" + id, null, null, null, null);
    }

    public Cursor getOneCompletedTask(long id){
        return database.query(
                "completed", null, "_id=" + id, null, null, null, null);
    }

    public void deleteTask(long id){
        open();
        database.delete("tasks", "_id=" + id, null);
        close();
    }

    public void deleteCompletedTask(long id){
        open();
        database.delete("completed", "_id=" + id, null);
        close();
    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper {

        public DatabaseOpenHelper(Context context, String name,
        CursorFactory factory, int version){
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            String createQuery = "CREATE TABLE tasks" +
                    "(_id integer primary key autoincrement," +
                    "fullTask TEXT, name TEXT, details TEXT, address TEXT, fulldate CHAR, month CHAR, day CHAR, year CHAR," +
                    "hours CHAR, minutes CHAR, timeOfDay CHAR);";

            String createCompleted = "CREATE TABLE completed"+
                    "(_id integer primary key autoincrement,"+
                    "fullTask TEXT, name TEXT, details TEXT, address TEXT, fulldate CHAR, month CHAR, day CHAR, year CHAR," +
                    "hours CHAR, minutes CHAR, timeOfDay CHAR);";

            db.execSQL(createQuery);
            db.execSQL(createCompleted);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion){

        }
    }
}
