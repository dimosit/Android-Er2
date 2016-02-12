package assignment2.android.hua.gr.android_er2.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    /**
     * The DB's name
     */
    public   static final String   DATABASE_NAME  = "hua_users";
    /**
     * The DB's Table name
     */
    public   static final String   DATABASE_TABLE = "users";
    /**
     * The DB's version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * The user's id column
     */
    public static final String USEID = "_USEID";
    /**
     * The user's name column
     */
    public static final String USERNAME = "_USERNAME";
    /**
     * The user's current location column
     */
    public static final String CURRENT_LOCATION = "_CURRENT_LOCATION";

    /**
     * The DB's creation String
     */
    private static final String DATABASE_CREATE =
            "CREATE TABLE " + DATABASE_TABLE +
                    "(" + USEID + " INTEGER PRIMARY KEY,"
                    + USERNAME + " TEXT NOT NULL,"
                    + CURRENT_LOCATION + " TEXT" + ");";

    /**
     * DbHelper's Constructor
     * @param context the context
     */
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute the DB's creation String
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the DB and create it again
        db.execSQL("DROP TABLE IF EXISTS " +  DATABASE_TABLE);
        onCreate(db);
    }
}
