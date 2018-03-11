package com.whitewalkers.smartnotificationsmanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.StringBuilderPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static DatabaseHandler dbInstance;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "snm";
    private static final String TABLE_NOTIFICAIONS = "notifications";
    private static final String TABLE_APP_SETTINGS = "appSettings";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_TAG = "tag";
    private static final String KEY_PACKAGENAME = "packageName";
    private static final String KEY_APPNAME = "appName";
    private static final String KEY_POSTTIME = "postTime";
    private static final String KEY_ICONID = "iconId";
    private static final String KEY_BLOCKED = "blocked";
    private static final String KEY_ALWAYS_BLOCK = "block";
    private static final String KEY_ALWAYS_SHOW = "show";
    private static final int MAX_ENTRIES_TO_SHOW = 20;

    private Context context;

    public static DatabaseHandler getInstance(Context context){
        if(dbInstance == null){
            dbInstance = new DatabaseHandler(context);
        }

        return dbInstance;
    }

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + TABLE_NOTIFICAIONS + "("
                + KEY_POSTTIME + " INTEGER PRIMARY KEY,"
                + KEY_ID + " INTEGER,"
                + KEY_TAG + " TEXT,"
                + KEY_PACKAGENAME + " TEXT,"
                + KEY_APPNAME + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_ICONID + " INTEGER,"
                + KEY_BLOCKED + " INTEGER"
                + ")";
        db.execSQL(CREATE_NOTIFICATIONS_TABLE);
        Log.e("gaurav", "Creating app notifications table");
        String CREATE_APP_SETTINGS_TABLE = "CREATE TABLE " + TABLE_APP_SETTINGS + " ("
                + KEY_PACKAGENAME + " TEXT PRIMARY KEY, "
                + KEY_ALWAYS_BLOCK + " INTEGER DEFAULT 0, "
                + KEY_ALWAYS_SHOW + " INTEGER DEFAULT 0"
                + ");";
        Log.e("gaurav", CREATE_APP_SETTINGS_TABLE);
        db.execSQL(CREATE_APP_SETTINGS_TABLE);
        Log.e("gaurav", "Created app notifications table");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // code to add the new contact
    void addNotification(SNMNotification notification) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, notification.getId());
        values.put(KEY_TAG, notification.getTag());
        values.put(KEY_POSTTIME, notification.getPostTime());
        values.put(KEY_TITLE, notification.getTitle());
        values.put(KEY_DESCRIPTION, notification.getDescription());
        values.put(KEY_PACKAGENAME, notification.getPackageName());
        values.put(KEY_APPNAME, notification.getAppName());
        values.put(KEY_ICONID, notification.getIconid());
        values.put(KEY_BLOCKED, notification.getBlocked());

        // Inserting Row
        db.insert(TABLE_NOTIFICAIONS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public List<SNMNotification> getAllNotifications() {
        List<SNMNotification> notificationList = new ArrayList<SNMNotification>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NOTIFICAIONS + " ORDER BY " + KEY_POSTTIME + " DESC LIMIT " + MAX_ENTRIES_TO_SHOW;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if(cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    SNMNotification notification = new SNMNotification();
                    notification.setPostTime(Long.parseLong(cursor.getString(0)));
                    notification.setId(Integer.parseInt(cursor.getString(1)));
                    notification.setTag(cursor.getString(2));
                    notification.setPackageName(cursor.getString(3));
                    notification.setAppName(cursor.getString(4));
                    notification.setTitle(cursor.getString(5));
                    notification.setDescription(cursor.getString(6));
                    notification.setIconid(Integer.parseInt(cursor.getString(7)));
                    notification.setBlocked(Integer.parseInt(cursor.getString(8)));
                    notificationList.add(notification);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();
        return notificationList;
    }

    public List<SNMNotification> getAllNotificationsForExport() {
        List<SNMNotification> notificationList = new ArrayList<SNMNotification>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NOTIFICAIONS + " ORDER BY " + KEY_POSTTIME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if(cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    SNMNotification notification = new SNMNotification();
                    notification.setPostTime(Long.parseLong(cursor.getString(0)));
                    notification.setId(Integer.parseInt(cursor.getString(1)));
                    notification.setTag(cursor.getString(2));
                    notification.setPackageName(cursor.getString(3));
                    notification.setAppName(cursor.getString(4));
                    notification.setTitle(cursor.getString(5));
                    notification.setDescription(cursor.getString(6));
                    notification.setIconid(Integer.parseInt(cursor.getString(7)));
                    notification.setBlocked(Integer.parseInt(cursor.getString(8)));
                    notificationList.add(notification);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();
        return notificationList;
    }

    public ArrayList<AppSetting> getAllAppSettings() {
        ArrayList<AppSetting> settings = new ArrayList<AppSetting>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_APP_SETTINGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if(cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    AppSetting setting = new AppSetting();
                    setting.setPackageName(cursor.getString(0));
                    setting.setAlwaysBlock((Integer.parseInt((cursor.getString(1)))==1));
                    setting.setAlwaysShow((Integer.parseInt((cursor.getString(2)))==1));
                    settings.add(setting);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return settings;
    }

    public AppSetting getAppSetting(String packageName) {
        AppSetting setting = new AppSetting();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_APP_SETTINGS + " WHERE " + KEY_PACKAGENAME + " like \'" + packageName + "\'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if(cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    setting = new AppSetting();
                    setting.setPackageName(cursor.getString(0));
                    setting.setAlwaysBlock((Integer.parseInt((cursor.getString(1)))==1));
                    setting.setAlwaysShow((Integer.parseInt((cursor.getString(2)))==1));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        // return contact list
        return setting;
    }

    public void setAppSetting(String packageName, boolean isAlwaysBlock, boolean isAlwaysShow) {
        AppSetting setting = new AppSetting();
        // Select All Query
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PACKAGENAME, packageName);
        values.put(KEY_ALWAYS_BLOCK, isAlwaysBlock? 1: 0);
        values.put(KEY_ALWAYS_SHOW, isAlwaysShow? 1: 0);

        // Inserting Row
        db.insertWithOnConflict(TABLE_APP_SETTINGS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();

        Helper.updateBlockAndShowList(this.context);
    }

    public void clearNotificationsTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTIFICAIONS, null, null);
        db.close();
    }

    public void clearAppSettingsTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_APP_SETTINGS, null, null);
        db.close();
    }


    public void exportNotifications(){
        List<SNMNotification> notifications = this.getAllNotificationsForExport();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String fileName = "notif_" + formatter.format(new Date()) + ".tsv";
        StringBuilder sb = new StringBuilder();

        String[] headers = {
                "PostTime",
                "AppName",
                "Package",
                "Title",
                "Description",
                "Blocked"
        };

        sb.append(TextUtils.join("\t", headers));
        sb.append("\n");
        for (SNMNotification notification : notifications) {
            Object[] row = {
                notification.getPostTime(),
                notification.getAppName(),
                notification.getPackageName(),
                notification.getTitle(),
                notification.getDescription(),
                notification.getBlocked()
            };

            sb.append(TextUtils.join("\t", row));
            sb.append("\n");
        }

        FileUtils.writeToFile(this.context, sb.toString(), fileName);
    }


    /*
    // code to update the single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    */
}