package assignment2.android.hua.gr.android_er2.services;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import assignment2.android.hua.gr.android_er2.asyncTasks.GetAllData;
import assignment2.android.hua.gr.android_er2.model.User;
import assignment2.android.hua.gr.android_er2.network.NetworkHelper;
import assignment2.android.hua.gr.android_er2.ui.MainActivity;

/**
 * Created by d1 on 25/1/2016.
 */

public class GetDataFromServer extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        NetworkHelper networkHelper = new NetworkHelper(this);
        if (networkHelper.isNetworkAvailable())
            new GetAllData(GetDataFromServer.this).execute();
    }
}