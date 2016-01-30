package assignment2.android.hua.gr.android_er2.asyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import assignment2.android.hua.gr.android_er2.R;
import assignment2.android.hua.gr.android_er2.database.DataManagement;
import assignment2.android.hua.gr.android_er2.model.User;
import assignment2.android.hua.gr.android_er2.ui.MainActivity;

/**
 * Created by Manos on 27/1/2016.
 */
public class Register extends AsyncTask<Void, Void, Void> {

    private String name;
    private ProgressDialog dialog;
    private Context context;
    User user;
    int status;

    public Register(String name, Context context) {
        this.name = name;
        this.context = context;
        this.user = new User();
        this.status = 0;
        this.dialog = new ProgressDialog(context);
    }

    // convert inputstream to String
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        return result;
    }

    public void postData() {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        String url = "http://dit117-hua.tk?";

        try {
            // Adding data
            List<NameValuePair> nameValuePairs = new ArrayList<>(2);
            nameValuePairs.add(new BasicNameValuePair("method", "assignId"));
            nameValuePairs.add(new BasicNameValuePair("username", name));

            String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
            url += paramString;

            HttpGet httpGet = new HttpGet(url);

            // Execute HTTP Get Request
            HttpResponse response = httpclient.execute(httpGet);
            InputStream inputStream = response.getEntity().getContent();

            // Convert Response Stream to json object
            JSONObject json = new JSONObject(convertInputStreamToString(inputStream));
            // get data json object
            JSONObject json_data = json.getJSONObject("data");
            // get value from data Json Object
            String useid = json_data.getString("useid");

            user.setUseid(Integer.parseInt(useid));
            user.setUsername(name);

        } catch (ClientProtocolException e) {
            status = 1;
        } catch (IOException e) {
            status = 2;
        } catch (JSONException e) {
            status = 3;
        }
    }

    public void insertUser() {
        DataManagement dataManagement = new DataManagement(context);

        if (!dataManagement.insertUserToDB(user))
            status = 4;
    }

    // dismiss progress dialog
    private void progressDialogDismiss() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage(context.getResources().getString(R.string.registering_message));
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //show dialog in main activity
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        postData();
        if (status == 0)
            insertUser();
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        progressDialogDismiss();

        if (status != 0) {
            Toast.makeText(context, R.string.registration_error, Toast.LENGTH_SHORT).show();
            return;
        }


        // Save our id in order to post to the server our location later
        SharedPreferences sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("MyId", user.getUseid());
        editor.putBoolean(context.getString(R.string.first_time_run), false);
        editor.apply();

        Toast.makeText(context, R.string.registration_success, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
