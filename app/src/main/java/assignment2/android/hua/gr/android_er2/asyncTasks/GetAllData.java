package assignment2.android.hua.gr.android_er2.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import assignment2.android.hua.gr.android_er2.R;
import assignment2.android.hua.gr.android_er2.converter.InputStreamConverter;
import assignment2.android.hua.gr.android_er2.database.DataManagement;
import assignment2.android.hua.gr.android_er2.model.User;

public class GetAllData extends AsyncTask<Void, Void, Void> {

    private ArrayList<User> users = new ArrayList<>();
    private Context context;
    private int status;

    /**
     * GetAllData Constructor
     *
     * @param context the context
     */
    public GetAllData(Context context) {
        this.context = context;
        this.status = 0;
    }

    /**
     * Gets all the Users Data from the RESTful Web Service into JSON format,
     * parses them and adds the results into an ArrayList
     */
    void getData() {
        // Create a new HttpClient and Get Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://dit117-hua.tk");

        try {
            // Execute HTTP Get Request and get the response
            HttpResponse response = httpclient.execute(httpGet);
            // Create an Input Stream from the response
            InputStream inputStream = response.getEntity().getContent();
            // Instantiate an Input Stream Converter
            InputStreamConverter converter = new InputStreamConverter();
            // Create a JSON object from the response
            JSONObject json = new JSONObject(converter.convertInputStreamToString(inputStream));
            // Get "data" json object
            JSONArray leaders = json.getJSONArray("data");

            // Add every User's Data into the ArrayList
            for (int i = 0; i < leaders.length(); i++) {
                JSONObject jsonSingleDataRow = leaders.getJSONObject(i);
                int useid = jsonSingleDataRow.getInt("useid");
                String username = jsonSingleDataRow.getString("username");
                String location = jsonSingleDataRow.getString("current_location");
                User user = new User();
                user.setUseid(useid);
                user.setUsername(username);
                user.setCurrent_location(location);
                users.add(user);
            }

        } catch (ClientProtocolException e) {
            status = 1;
        } catch (IOException e) {
            status = 2;
        } catch (JSONException e) {
            status = 3;
        }
    }

    /**
     * Inserts every User's Data into the phone's DB
     */
    void saveData() {
        DataManagement dm = new DataManagement(context);
        dm.deleteAllUsersFromDB();

        if (!dm.insertUsersToDB(users))
            Toast.makeText(context, R.string.data_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        getData();
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        // If something went wrong, display proper toast message
        if (status != 0)
            Toast.makeText(context, R.string.fetch_error, Toast.LENGTH_SHORT).show();
            // Otherwise, save the Data
        else
            saveData();
    }
}
