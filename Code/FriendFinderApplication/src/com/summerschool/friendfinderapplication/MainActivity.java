package com.summerschool.friendfinderapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class MainActivity extends Activity {

	// Parse application keys
	private static final String PARSE_APPLICATION_ID = "rU3OkVyuuIgA17MsCPBgspurzhM00QOSxIaXvzsI";
	private static final String PARSE_CLIENT_KEY = "Vw4U1lSXshwY9Nia14KV1MpGxJht8S3Q9H1N7TVP";
	
	EditText mTaskInput;
	ListView mListView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
        ParseAnalytics.trackAppOpened(getIntent());
        
        ParseObject.registerSubclass(Task.class);
        
        mTaskInput = (EditText) findViewById(R.id.task_input);
        mListView = (ListView) findViewById(R.id.task_list);
        
    }
    
    public void createTask(View v) {
    	if (mTaskInput.getText().length() > 0) {
    		Task t = new Task();
    		t.setDescription(mTaskInput.getText().toString());
    		t.setComleted(false);
    		t.saveEventually();
    		// delete textfield
    		mTaskInput.setText("");
    	}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
