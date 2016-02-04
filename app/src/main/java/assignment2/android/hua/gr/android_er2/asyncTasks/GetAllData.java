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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import assignment2.android.hua.gr.android_er2.R;
import assignment2.android.hua.gr.android_er2.database.DataManagement;
import assignment2.android.hua.gr.android_er2.model.User;

public class GetAllData extends AsyncTask<Void, Void, Void> {

    ArrayList<User> users = new ArrayList<>();
    Context context;
    int status;

    public GetAllData(Context context){
        this.context = context;
        this.status = 0;
    }

    // convert inputstream to String
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        return result;
    }

    void getData() {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://dit117-hua.tk");

        try {
            // Execute HTTP Get Request
            HttpResponse response = httpclient.execute(httpGet);
            InputStream inputStream = response.getEntity().getContent();
            JSONObject json = new JSONObject(convertInputStreamToString(inputStream));
            // get data json object
            JSONArray leaders= json.getJSONArray("data");

            for(int i = 0; i < leaders.length(); i++) {
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

    void saveData() {
        DataManagement dm = new DataManagement(context);
        if(!dm.insertAllUsersToDB(users))
            Toast.makeText(context, R.string.data_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        getData();
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {

        if (status != 0){
            Toast.makeText(context, R.string.fetch_error, Toast.LENGTH_SHORT).show();
            return;
        }

        saveData();
    }
}
