package assignment2.android.hua.gr.android_er2.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import assignment2.android.hua.gr.android_er2.R;
import assignment2.android.hua.gr.android_er2.asyncTasks.Register;

public class FirstActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        register();
    }

    public void register() {
        EditText editText = (EditText) findViewById(R.id.editText);
        String nickname = editText.getText().toString();

        if (nickname.isEmpty())
            Toast.makeText(this, R.string.empty_name, Toast.LENGTH_SHORT).show();
        else
            new Register(nickname, getApplicationContext()).execute();
    }
}