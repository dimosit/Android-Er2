package assignment2.android.hua.gr.android_er2.services;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import assignment2.android.hua.gr.android_er2.model.User;
import assignment2.android.hua.gr.android_er2.ui.MainActivity;

/**
 * Created by d1 on 25/1/2016.
 */

//Me auto to service tha kanoume pull kai parse ta data apo to server epeidh to service trexoun sto UI thread
//    prepei na exoume thereads h to asynctask

public class GetDataFromServer extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class GetData extends AsyncTask<Void, Void, ArrayList<User>> {

        InputStream is = null;
        ArrayList<User> userData = new ArrayList<User>();
        String serverAddress = "http://dit117-hua.tk/";
        private ProgressDialog dialog;
        /** application context. */
        private Context context;

        //con for dialog
        public GetData(MainActivity activity) {
            this.context = activity;
            dialog = new ProgressDialog(context);
        }


        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Fatching data from server ...!");
            //show dialog in main activity
            this.dialog.show();
        }

        @Override
        protected ArrayList<User> doInBackground(Void... params) {

            try {
                URL url = new URL(serverAddress);
                //connect to the url
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setReadTimeout(10 * 1000);
                connection.setConnectTimeout(10 * 1000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();

                int response = connection.getResponseCode();
                //debug
                Log.d("debug", "The response is: " + response);
                is = connection.getInputStream();

                // parse xml
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                //set suppport for xml namespaces
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                //insert stream data to parser
                xpp.setInput(is, null);


            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }


            return userData;
        }
    }
}
