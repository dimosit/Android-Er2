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
    static final String URL = "content://" + PROVIDER_NAME + "/coordinates";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = "_id";
    //static final String LATITUDE = "latitude";
    //static final String LONGITUDE = "longitude";

    private static HashMap<String, String> COORDINATES_MAP;

    static final int COORDINATES = 1;
    static final int COORDINATE_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "coordinates", COORDINATES);
        uriMatcher.addURI(PROVIDER_NAME, "coordinates/#", COORDINATE_ID);
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
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            qb.setTables("coordinates");

            switch (uriMatcher.match(uri)) {

                case COORDINATES:
                    qb.setProjectionMap(COORDINATES_MAP);
                    break;

                case COORDINATE_ID:
                    qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                    break;

                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);

            }

            if (sortOrder.isEmpty()) {
                /**
                 * sort on location id
                 */
                sortOrder = _ID;
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

    @SuppressWarnings("ConstantConditions")
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        /**
         * Add a new location record
         */
        long rowID = db.insert("coordinates", "", values);

        /**
         * If record is added successfully
         */
        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);

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
