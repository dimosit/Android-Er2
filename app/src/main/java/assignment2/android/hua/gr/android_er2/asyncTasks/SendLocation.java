package assignment2.android.hua.gr.android_er2.asyncTasks;

import android.content.Context;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import assignment2.android.hua.gr.android_er2.R;

public class SendLocation extends AsyncTask<Void, Void, Void> {

    private String location;
    private int id;
    private int status;
    private Context context;

    /**
     * SendLocation Constructor
     *
     * @param id       'our' id
     * @param location 'our' location
     * @param context  the context
     */
    public SendLocation(int id, String location, Context context) {
        this.status = 0;
        this.id = id;
        this.location = location;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        // Create a new HttpClient and URL String
        HttpClient httpclient = new DefaultHttpClient();
        String url = "http://dit117-hua.tk?";

        try {
            // Create name value pairs for the method, the location and the useid
            List<NameValuePair> nameValuePairs = new ArrayList<>(3);
            nameValuePairs.add(new BasicNameValuePair("method", "newLocation"));
            nameValuePairs.add(new BasicNameValuePair("location", location));
            nameValuePairs.add(new BasicNameValuePair("useid", String.valueOf(id)));

            String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
            url += paramString;

            HttpGet httpGet = new HttpGet(url);

            // Execute HTTP Get Request and get the response
            HttpResponse response = httpclient.execute(httpGet);

            // If the response status is not OK
            if (response.getStatusLine().getStatusCode() != 200)
                status = 1;

        } catch (ClientProtocolException e) {
            status = 2;
        } catch (UnsupportedEncodingException e) {
            status = 3;
        } catch (IOException e) {
            status = 4;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        // If something went wrong, display proper toast message
        if (status != 0)
            Toast.makeText(context, R.string.posting_location_error, Toast.LENGTH_SHORT).show();
        else
            // Otherwise, display success toast message
            Toast.makeText(context, R.string.posting_location_success, Toast.LENGTH_SHORT).show();
    }
}
