package assignment2.android.hua.gr.android_er2.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import assignment2.android.hua.gr.android_er2.R;
import assignment2.android.hua.gr.android_er2.asyncTasks.Register;
import assignment2.android.hua.gr.android_er2.network.NetworkHelper;

public class FirstActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    /**
     * When Register button is being pressed,
     * user is being registered with his nickname
     * @param v the view
     */
    public void register(View v) {
        EditText editText = (EditText) findViewById(R.id.editText);
        String nickname = editText.getText().toString();

        // If nickname is empty display proper message
        if (nickname.isEmpty())
            Toast.makeText(this, R.string.empty_name, Toast.LENGTH_SHORT).show();
        else {
            NetworkHelper networkHelper = new NetworkHelper(this);

            // If network is available, register the user
            if (networkHelper.isNetworkAvailable())
                new Register(nickname, this).execute();
            else
                networkHelper.showSettingsAlert();
        }
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}