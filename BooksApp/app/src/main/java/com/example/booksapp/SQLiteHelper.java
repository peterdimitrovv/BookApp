package com.example.booksapp;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String name, String genre, String author, int year, double rating, String description, byte[] image) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO BOOKS VALUES (NULL, ?, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, genre);
        statement.bindString(3, author);
        statement.bindDouble(4, (double)year);
        statement.bindDouble(5, rating);
        statement.bindString(6, description);
        statement.bindBlob(7, image);

        statement.executeInsert();
    }

    public void updateData(String name, String genre, String author, int year, double rating, String description, byte[] image, int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE BOOKS SET name=?, genre=?, author=?, year=?, rating=?, description=?, image=? WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, name);
        statement.bindString(2, genre);
        statement.bindString(3, author);
        statement.bindDouble(4, (double)year);
        statement.bindDouble(5, rating);
        statement.bindString(6, description);
        statement.bindBlob(7, image);
        statement.bindDouble(8, (double)id);

        statement.execute();
        database.close();
    }

    public void deleteData(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM BOOKS WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindDouble(1, (double)id);
        statement.execute();
        database.close();
    }

    public Cursor getData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return  database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
