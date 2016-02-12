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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import assignment2.android.hua.gr.android_er2.R;
import assignment2.android.hua.gr.android_er2.converter.InputStreamConverter;
import assignment2.android.hua.gr.android_er2.model.User;
import assignment2.android.hua.gr.android_er2.ui.MainActivity;

public class Register extends AsyncTask<Void, Void, Void> {

    private String name;
    private ProgressDialog dialog;
    private Context context;
    private User user;
    private int status;

    /**
     * Register Constructor
     * @param name the User's name
     * @param context the context
     */
    public Register(String name, Context context) {
        this.name = name;
        this.context = context;
        this.user = new User();
        this.status = 0;
        this.dialog = new ProgressDialog(context);
    }

    public void postData() {
        // Create a new HttpClient and a URL String
        HttpClient httpclient = new DefaultHttpClient();
        String url = "http://dit117-hua.tk?";

        try {
            // Create name value pairs for the method and the username
            List<NameValuePair> nameValuePairs = new ArrayList<>(2);
            nameValuePairs.add(new BasicNameValuePair("method", "assignId"));
            nameValuePairs.add(new BasicNameValuePair("username", name));

            String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
            url += paramString;

            HttpGet httpGet = new HttpGet(url);

            // Execute HTTP Get Request and get the response
            HttpResponse response = httpclient.execute(httpGet);
            // Create an Input Stream from the response
            InputStream inputStream = response.getEntity().getContent();
            // Instantiate an Input Stream Converter
            InputStreamConverter converter = new InputStreamConverter();
            // Convert Response Stream to json object
            JSONObject json = new JSONObject(converter.convertInputStreamToString(inputStream));
            // Get "data" json object
            JSONObject json_data = json.getJSONObject("data");
            // Get "useid" from "data" Json Object
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

    /**
     * Checks if the progress dialog is showing.<br>
     * If yes, it dismisses it.
     */
    private void progressDialogDismiss() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        // Prepare and show a progress dialog
        dialog.setMessage(context.getResources().getString(R.string.registering_message));
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        postData();
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        progressDialogDismiss();

        // If something went wrong, display proper toast message
        if (status != 0)
            Toast.makeText(context, R.string.registration_error, Toast.LENGTH_SHORT).show();

        /* Otherwise, save 'our' id to the phone's shared preferences,
           in order to post to the server our location later
           and change the variable that shows if the app has run again to false
           Show registration success message and continue with the Main Activity*/
        else {
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
}
