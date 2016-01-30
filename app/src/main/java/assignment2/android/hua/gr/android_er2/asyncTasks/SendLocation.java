package assignment2.android.hua.gr.android_er2.asyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import assignment2.android.hua.gr.android_er2.R;

/**
 * Created by Manos on 27/1/2016.
 */
public class SendLocation extends AsyncTask<Void, Void, Void> {

    String location;
    int id;
    int status;
    Context context;
    ProgressDialog dialog;

    public SendLocation(int id, String location, Context context){
        this.status = 0;
        this.id = id;
        this.location = location;
        this.context = context;
        this.dialog = new ProgressDialog(context);
    }

    // dismiss progress dialog
    private void progressDialogDismiss() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage(context.getResources().getString(R.string.posting_location));
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //show dialog in main activity
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        String url = "http://dit117-hua.tk?";

        try {
            SharedPreferences sharedPref =
                    context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            int myId = sharedPref.getInt("MyId", 0);

            // Adding data
            List<NameValuePair> nameValuePairs = new ArrayList<>(3);
            nameValuePairs.add(new BasicNameValuePair("method", "newLocation"));
            nameValuePairs.add(new BasicNameValuePair("location", location));
            nameValuePairs.add(new BasicNameValuePair("useid", String.valueOf(myId)));

            String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
            url += paramString;

            HttpGet httpGet = new HttpGet(url);

            // Execute HTTP Get Request
            HttpResponse response = httpclient.execute(httpGet);
            InputStream inputStream = response.getEntity().getContent();

            // Convert Response's String to json object
            JSONObject json = new JSONObject(inputStream.toString());
            // get code json object
            JSONObject json_code = json.getJSONObject("code");
            if (Integer.getInteger(json_code.toString()) != 1)
                status = 1;

        } catch (ClientProtocolException e) {
            status = 2;
        } catch (UnsupportedEncodingException e) {
            status = 3;
        } catch (IOException e) {
            status = 4;
        } catch (JSONException e) {
            status = 5;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        progressDialogDismiss();

        if (status != 0){
            Toast.makeText(context, R.string.posting_location_error, Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(context, R.string.posting_location_success, Toast.LENGTH_SHORT).show();
    }
}
