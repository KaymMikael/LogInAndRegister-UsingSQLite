package com.example.loginandregister.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.loginandregister.Models.UserAccount;

public class Database extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    public static final String databaseName = "Account.db";
    public static final String TABLE_ACCOUNTS = "accounts";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ID = "id";

    public Database(@Nullable Context context) {
        super(context, databaseName, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String query = "CREATE TABLE " + TABLE_ACCOUNTS + "( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT" +
                    ")";
            db.execSQL(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
        onCreate(db);
    }

    @SuppressLint("Range")
    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1;
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(TABLE_ACCOUNTS, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            cursor.close();
        }
        return userId;
    }

    @SuppressLint("Range")
    public int getUserId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1;
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(TABLE_ACCOUNTS, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            cursor.close();
        }
        return userId;
    }

    @SuppressLint("Range")
    public UserAccount getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        UserAccount user = null;

        String[] columns = {COLUMN_USERNAME};
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query(TABLE_ACCOUNTS, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            user = new UserAccount();
            user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
            cursor.close();
        }

        return user;
    }

    public UserAccount getUserByUsername(String username) {
        UserAccount user = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_USERNAME, COLUMN_PASSWORD};

        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(TABLE_ACCOUNTS, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            @SuppressLint("Range") String dbUsername = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
            user = new UserAccount(id, dbUsername, password);
            cursor.close();
        }

        return user;
    }

    public boolean insertAccount(UserAccount account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, account.getUsername());
        cv.put(COLUMN_PASSWORD, account.getPassword());
        long result = db.insert(TABLE_ACCOUNTS, null, cv);
        if (result == -1) return false;
        else
            return true;
    }

    public boolean checkUsernameForUser(String username, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ACCOUNTS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_ID + " <> ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, String.valueOf(userId)});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }


    public boolean checkUsername(UserAccount account) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ACCOUNTS + " WHERE " + COLUMN_USERNAME + " = ? ";
        Cursor cursor = db.rawQuery(query, new String[]{account.getUsername()});
        if (cursor.getCount() > 0) return true;
        else
            return false;
    }

    public boolean checkUsernamePassword(UserAccount account) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ACCOUNTS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{account.getUsername(), account.getPassword()});
        if (cursor.getCount() > 0) return true;
        else
            return false;
    }

    public void updateName(UserAccount account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, account.getUsername());
        db.update(TABLE_ACCOUNTS, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(account.getId())});
    }

    public void updatePassword(UserAccount account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PASSWORD, account.getPassword());
        db.update(TABLE_ACCOUNTS, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(account.getId())});
    }

    public void deleteAllAccounts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACCOUNTS, null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_ACCOUNTS + "'");
    }

}
