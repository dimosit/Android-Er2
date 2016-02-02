package assignment2.android.hua.gr.android_er2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

import assignment2.android.hua.gr.android_er2.contentProvider.UserProvider;
import assignment2.android.hua.gr.android_er2.model.User;

/**
 * Created by Manos on 27/1/2016.
 */
public class DataManagement {

    Context context;

    public DataManagement(Context context) {
        this.context = context;
    }

    public boolean insertAllUsersToDB(ArrayList<User> users) {
        if (!users.isEmpty()) {
            ContentValues[] valueList = new ContentValues[users.size()];
            int i = 0;

            for (User u : users) {
                ContentValues values = new ContentValues();
                values.put(DbHelper.USEID, u.getUseid());
                values.put(DbHelper.USERNAME, u.getUsername());
                values.put(DbHelper.CURRENT_LOCATION, u.getCurrent_location());
                valueList[i++] = values;
            }

            try {
                context.getContentResolver()
                        .bulkInsert(UserProvider.CONTENT_URI, valueList);
            } catch (SQLException e) {
                return false;
            }
        }
        return true;
    }


    public ArrayList<User> getAllUsersFromDB() {
        Uri uri = UserProvider.CONTENT_URI;

        String[] projection = new String[]{
                DbHelper.USERNAME,
                DbHelper.CURRENT_LOCATION
        };

        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);

        ArrayList<User> users = new ArrayList<>();

        if (c != null) {

            while (c.moveToNext()) {
                User user = new User();
                user.setUsername(c.getString(c.getColumnIndexOrThrow(DbHelper.USERNAME)));
                user.setCurrent_location
                        (c.getString(c.getColumnIndexOrThrow(DbHelper.CURRENT_LOCATION)));
                users.add(user);
            }

            c.close();
        }

        return users;
    }
}