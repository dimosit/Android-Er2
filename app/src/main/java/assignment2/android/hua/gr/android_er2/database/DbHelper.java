package assignment2.android.hua.gr.android_er2.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    //Database info
    public   static final String   DATABASE_NAME  = "hua_users";
    public   static final String   DATABASE_TABLE = "users";
    private static final int DATABASE_VERSION = 1;

    //Database columns
    public static final String USEID = "_USEID";
    public static final String USERNAME = "_USERNAME";
    public  static final String CURRENT_LOCATION = "_CURRENT_LOCATION";

    //Create the database
    private static final String DATABASE_CREATE =
            "CREATE TABLE " + DATABASE_TABLE +
                    "(" + USEID + " INTEGER PRIMARY KEY,"
                    + USERNAME + " TEXT NOT NULL,"
                    + CURRENT_LOCATION + " TEXT" + ");";

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +  DATABASE_TABLE);
        onCreate(db);
    }
}