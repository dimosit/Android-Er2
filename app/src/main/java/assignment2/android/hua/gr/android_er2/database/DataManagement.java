package assignment2.android.hua.gr.android_er2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;

import assignment2.android.hua.gr.android_er2.contentProvider.UserProvider;
import assignment2.android.hua.gr.android_er2.model.User;

/**
 * Created by Manos on 27/1/2016.
 */
public class DataManagement {

    private UserProvider userProvider;

    public DataManagement(Context context){
        SQLiteDatabase db = openOrCreateDB(context);
        this.userProvider = new UserProvider(db);
    }

    public SQLiteDatabase openOrCreateDB(Context context) {
        return context.openOrCreateDatabase(DbHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    public boolean insertUserToDB(User user){
        ContentValues values = new ContentValues();

        values.put(DbHelper.USEID, user.getUseid());
        values.put(DbHelper.USERNAME, user.getUsername());
        values.put(DbHelper.CURRENT_LOCATION, "");

        try {
            userProvider.insert(UserProvider.CONTENT_URI, values);
            return true;
        }
        catch (SQLException e){
            return false;
        }
    }

    public boolean insertAllUsersToDB(ArrayList<User> users){
        ContentValues values = new ContentValues();

        for (User u : users) {
            values.put(DbHelper.USERNAME, u.getUsername());
            values.put(DbHelper.USEID, u.getUseid());
            values.put(DbHelper.CURRENT_LOCATION, u.getCurrent_location());
        }

        try {
            userProvider.insert(UserProvider.CONTENT_URI, values);
            return true;
        }
        catch (SQLException e){
            return false;
        }
    }

    public ArrayList<User> getAllUsersFromDB(){
        Uri uri = UserProvider.CONTENT_URI;

        String[] projection = new String[]{
                DbHelper.USERNAME,
                DbHelper.CURRENT_LOCATION
        };

        Cursor c = userProvider.query(uri, projection, null, null, null);

        ArrayList<User> users = new ArrayList<>();
        User user = new User();

        if (c != null && c.moveToFirst()) {

            while (c.moveToNext()) {
                user.setUsername(c.getString(c.getColumnIndexOrThrow(DbHelper.USERNAME)));
                user.setCurrent_location(c.getString(c.getColumnIndexOrThrow(DbHelper.CURRENT_LOCATION)));
                users.add(user);
            }

            c.close();
        }

        return users;
    }
}