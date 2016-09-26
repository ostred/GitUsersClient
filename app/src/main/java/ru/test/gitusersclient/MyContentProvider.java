package ru.test.gitusersclient;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class MyContentProvider extends ContentProvider {

    static final String DB_NAME = "usersdb";
    static final int DB_VERSION = 1;
    static final String USERS_TABLE = "gitUsers";

    static final String USERS_ID = "_id";
    static final String USER_NAME = "name";
    static final String USER_AVATAR = "avatar";

    static final String DB_CREATE = "create table " + USERS_TABLE + "("
            + USERS_ID + " integer primary key autoincrement, "
            + USER_NAME + " text, " + USER_AVATAR + " text" + ");";

    static final String AUTHORITY = "ru.test.providers.mGitClient";

    static final String USER_PATH = "USERs";

    public static final Uri USER_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + USER_PATH);

    static final String USER_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + USER_PATH;

    static final String USER_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + USER_PATH;

    static final int URI_USERS = 1;

    static final int URI_USERS_ID = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, USER_PATH, URI_USERS);
        uriMatcher.addURI(AUTHORITY, USER_PATH + "/#", URI_USERS_ID);
    }

    DBHelper dbHelper;
    SQLiteDatabase db;

    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case URI_USERS:
                break;
            case URI_USERS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = USERS_ID + " = " + id;
                } else {
                    selection = selection + " AND " + USERS_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(USERS_TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                USER_CONTENT_URI);
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != URI_USERS)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(USERS_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(USER_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_USERS:

                break;
            case URI_USERS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = USERS_ID + " = " + id;
                } else {
                    selection = selection + " AND " + USERS_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(USERS_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_USERS:
                break;
            case URI_USERS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = USERS_ID + " = " + id;
                } else {
                    selection = selection + " AND " + USERS_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(USERS_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_USERS:
                return USER_CONTENT_TYPE;
            case URI_USERS_ID:
                return USER_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}