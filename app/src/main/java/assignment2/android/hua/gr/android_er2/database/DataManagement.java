package assignment2.android.hua.gr.android_er2.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;

import assignment2.android.hua.gr.android_er2.contentProviders.UserProvider;
import assignment2.android.hua.gr.android_er2.model.User;

/**
 * Created by Manos on 27/1/2016.
 */
public class DataManagement {

    private UserProvider userProvider;

    public DataManagement(){
        this.userProvider = new UserProvider();
    }

    public boolean insertUserToDB(User user){
        ContentValues values = new ContentValues();

        values.put(UserProvider.USERNAME, user.getUsername());
        values.put(UserProvider.USEID, user.getUseid());

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
            values.put(UserProvider.USERNAME, u.getUsername());
            values.put(UserProvider.USEID, u.getUseid());
            values.put(UserProvider.LOCATION, u.getCurrent_location());
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
                UserProvider.USERNAME,
                UserProvider.LOCATION
        };

        Cursor c = userProvider.query(uri, projection, null, null, null);

        ArrayList<User> users = new ArrayList<>();
        User user = new User();

        if (c != null && c.moveToFirst()) {

            while (c.moveToNext()) {
                user.setUsername(c.getString(c.getColumnIndexOrThrow(UserProvider.USERNAME)));
                user.setCurrent_location(c.getString(c.getColumnIndexOrThrow(UserProvider.LOCATION)));
                users.add(user);
            }

            c.close();
        }

        return users;
    }
}