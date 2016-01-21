package assignment2.android.hua.gr.android_er2.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by d1 on 14/1/2016.
 */

public class DbHelper extends SQLiteOpenHelper {

    //Database info
    private  static final String   DATABASE_NAME  = "zadmin_android";
    public   static final String   DATABASE_TABLE = "users";
    private static final int DATABASE_VERSION = 1;

    //Database columns
    public static final String USEID = "_USEID";
    public static final String USERNAME = "_USERNAME";
    public  static final String CURRENT_LOCATION = "_CURRENT_LOATION";

    //Create the database
    private static final String DATABASE_CREATE =
            "CREATE TABLE " + DATABASE_TABLE +
                    "("+USEID+"INTEGER PRIMARY KEY,"
                    +USERNAME+"TEXT NOT NULL,"
                    +CURRENT_LOCATION+"TEXT NOT NULL"+");";

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}