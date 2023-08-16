package com.gbsaver.chatlocker.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gbsaver.chatlocker.model.HomeModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "chatlockinfo.db";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_APP_NAME = "appname";
    private static final String KEY_ID = "id";
    private static final String KEY_ISCHECKTOLOCK = "isToCheckLock";
    private static final String KEY_ISLOCK = "isLock";
    private static final String KEY_USER_NAME = "username";
    private static final String TABLE_CHATLOCK = "chatlock";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE chatlock(id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT,isLock INTEGER,isToCheckLock INTEGER,appname TEXT)");
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS chatlock");
        onCreate(sQLiteDatabase);
    }

    public long addChatLockInfo(HomeModel homeModel) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_NAME, homeModel.getUsername());
        contentValues.put(KEY_APP_NAME, homeModel.getAppName());
        contentValues.put(KEY_ISLOCK, Integer.valueOf(homeModel.getIsLock()));
        contentValues.put(KEY_ISCHECKTOLOCK, Integer.valueOf(homeModel.getIsToCheckLock()));
        long insert = readableDatabase.insert(TABLE_CHATLOCK, null, contentValues);
        readableDatabase.close();
        return insert;
    }

    public List<HomeModel> getAllChatLock() {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor rawQuery = writableDatabase.rawQuery("SELECT  * FROM chatlock ORDER BY id DESC", null);
        if (rawQuery.moveToFirst()) {
            do {
                HomeModel homeModel = new HomeModel();
                homeModel.setId(rawQuery.getInt(rawQuery.getColumnIndex(KEY_ID)));
                homeModel.setAppName(rawQuery.getString(rawQuery.getColumnIndex(KEY_APP_NAME)));
                homeModel.setUsername(rawQuery.getString(rawQuery.getColumnIndex(KEY_USER_NAME)));
                homeModel.setIsLock(rawQuery.getInt(rawQuery.getColumnIndex(KEY_ISLOCK)));
                homeModel.setIsToCheckLock(rawQuery.getInt(rawQuery.getColumnIndex(KEY_ISCHECKTOLOCK)));
                arrayList.add(homeModel);
            } while (rawQuery.moveToNext());
            writableDatabase.close();
            return arrayList;
        }
        writableDatabase.close();
        return arrayList;
    }

    public int updateChatLock(HomeModel homeModel) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, Integer.valueOf(homeModel.getId()));
        contentValues.put(KEY_USER_NAME, homeModel.getUsername());
        contentValues.put(KEY_APP_NAME, homeModel.getAppName());
        contentValues.put(KEY_ISLOCK, Integer.valueOf(homeModel.getIsLock()));
        contentValues.put(KEY_ISCHECKTOLOCK, Integer.valueOf(homeModel.getIsToCheckLock()));
        return writableDatabase.update(TABLE_CHATLOCK, contentValues, "id = ?", new String[]{String.valueOf(homeModel.getId())});
    }

    public void deleteChatInfo(HomeModel homeModel) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete(TABLE_CHATLOCK, "id = ?", new String[]{String.valueOf(homeModel.getId())});
        writableDatabase.close();
    }
}
