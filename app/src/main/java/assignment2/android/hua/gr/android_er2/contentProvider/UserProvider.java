package assignment2.android.hua.gr.android_er2.contentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

import assignment2.android.hua.gr.android_er2.database.DbHelper;

/**
 * Created by d1 on 21/1/2016.
 */
public class UserProvider extends ContentProvider {

    private static final String PROVIDER_NAME =
            "assignment2.android.hua.gr.android_er2.contentProvider.UserProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/" + DbHelper.DATABASE_TABLE;
    public static final Uri CONTENT_URI = Uri.parse(URL);

    private static HashMap<String, String> USERS_MAP;

    static final int USERS = 1;
    static final int USERS_ID = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, DbHelper.DATABASE_TABLE, USERS);
        uriMatcher.addURI(PROVIDER_NAME, DbHelper.DATABASE_TABLE + "/#", USERS_ID);
    }

    private SQLiteDatabase db;

    private boolean open() {
        Context context = getContext();
        DbHelper dbHelper = new DbHelper(context);
        /**
         * Create a writable database which will trigger its
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();

        return db != null;
    }

    @Override
    public boolean onCreate() {
        return open();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri newUri = null;

        switch (uriMatcher.match(uri)) {
            case USERS:
                // Add a new record
                long ID = db.insert(DbHelper.DATABASE_TABLE, null, values);

                // If added successfully
                if (ID > 0) {
                    newUri = ContentUris.withAppendedId(CONTENT_URI, ID);
                    getContext().getContentResolver().notifyChange(newUri, null);
                }

                break;

            default:
                throw new SQLException("Failed to insert row into " + uri);
        }

        return newUri;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DbHelper.DATABASE_TABLE);

        switch (uriMatcher.match(uri)) {

            case USERS:
                qb.setProjectionMap(USERS_MAP);
                break;

            case USERS_ID:
                qb.appendWhere(DbHelper.USEID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder.isEmpty()) {
            /**
             * sort on location id
             */
            sortOrder = DbHelper.USERNAME;
        }

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int uriType;
        int insertCount = 0;
        Uri newUri;

        uriType = uriMatcher.match(uri);

        switch (uriType) {
            case USERS:

                db.beginTransaction();

                for (ContentValues value : values) {
                    long id = db.insert(DbHelper.DATABASE_TABLE, null, value);

                    if (id > 0){
                        newUri = ContentUris.withAppendedId(CONTENT_URI, id);
                        getContext().getContentResolver().notifyChange(newUri, null);
                    }
                    insertCount++;
                }

                db.setTransactionSuccessful();
                db.endTransaction();
                break;
        }
        return insertCount;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
