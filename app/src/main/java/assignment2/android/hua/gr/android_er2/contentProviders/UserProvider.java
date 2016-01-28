package assignment2.android.hua.gr.android_er2.contentProviders;

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

    static final String PROVIDER_NAME =
            "assignment2.android.hua.gr.android_er2.contentProviders.LocationData";
    static final String URL = "content://" + PROVIDER_NAME + "/users";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String USERNAME = "username";
    public static final String USEID = "useid";
    public static final String LOCATION = "current_location";

    private static HashMap<String, String> USERS_MAP;

    static final int USERS = 1;
    static final int USERS_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "users", USERS);
        uriMatcher.addURI(PROVIDER_NAME, "users/#", USERS_ID);
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {

        Context context = getContext();
        DbHelper dbHelper = new DbHelper(context);
        /**
         * Create a writable database which will trigger its
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();
        return db != null;

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        /**
         * Add a new location record
         */
        long rowID = db.insert("users", "", values);

        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            qb.setTables("users");

            switch (uriMatcher.match(uri)) {

                case USERS:
                    qb.setProjectionMap(USERS_MAP);
                    break;

                case USERS_ID:
                    qb.appendWhere( USEID + "=" + uri.getPathSegments().get(1));
                    break;

                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);

            }

            if (sortOrder.isEmpty()) {
                /**
                 * sort on location id
                 */
                sortOrder = USERNAME;
            }

            Cursor c = qb.query(db,	projection,	selection, selectionArgs, null, null, sortOrder);
            /**
             * register to watch a content URI for changes
             */
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;

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
