package assignment2.android.hua.gr.android_er2.asyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import assignment2.android.hua.gr.android_er2.R;
import assignment2.android.hua.gr.android_er2.database.DataManagement;
import assignment2.android.hua.gr.android_er2.model.User;

/**
 * Created by Manos on 27/1/2016.
 */
public class GetAllData extends AsyncTask<Void, Void, Void> {

    ArrayList<User> users = new ArrayList<>();
    ProgressDialog dialog;
    Context context;
    int status;

    public GetAllData(Context context){
        this.context = context;
        this.dialog = new ProgressDialog(context);
        this.status = 0;
    }

    void getData() {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://dit117-hua.tk");

        try {
            // Execute HTTP Get Request
            HttpResponse response = httpclient.execute(httpGet);
            // Convert Response's String to json object
            JSONObject json = new JSONObject(response.toString());
            // get data json object
            JSONObject json_data = json.getJSONObject("data");
            // get value from data Json Object
            String useid = json_data.getString("useid");
            String username = json_data.getString("username");
            String location = json_data.getString("current_location");

            User user = new User();
            user.setUseid(Integer.getInteger(useid));
            user.setUsername(username);
            user.setCurrent_location(location);
            users.add(user);

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
            status = 4;
    }

    // dismiss progress dialog
    private void progressDialogDismiss() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage(context.getResources().getString(R.string.getting_data));
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //show dialog in main activity
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        getData();
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        progressDialogDismiss();

        if (status != 0){
            Toast.makeText(context, R.string.fetch_error, Toast.LENGTH_SHORT).show();
            return;
        }

        saveData();
    }
}
