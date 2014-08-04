package com.summerschool.friendfinderapplication;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class MainActivity extends Activity implements OnItemClickListener {

	// Parse application keys
	private static final String PARSE_APPLICATION_ID = "rU3OkVyuuIgA17MsCPBgspurzhM00QOSxIaXvzsI";
	private static final String PARSE_CLIENT_KEY = "Vw4U1lSXshwY9Nia14KV1MpGxJht8S3Q9H1N7TVP";
		
	protected EditText mTaskInput;
	protected ListView mListView;
	protected TaskAdapter mAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
        ParseAnalytics.trackAppOpened(getIntent());
        
        ParseObject.registerSubclass(Task.class);
        
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser == null){
          Intent intent = new Intent(this, LoginActivity.class);
          startActivity(intent);
          finish();
        }
        
        mTaskInput = (EditText) findViewById(R.id.task_input);
        mListView = (ListView) findViewById(R.id.task_list);
        
        mListView.setOnItemClickListener(this);
        
        mAdapter = new TaskAdapter(this, new ArrayList<Task>());
        mListView.setAdapter(mAdapter);
        
        updateData();
    }
    
    public void createTask(View v) {
    	if (mTaskInput.getText().length() > 0) {
    		Task t = new Task();
    		t.setDescription(mTaskInput.getText().toString());
    		t.setACL(new ParseACL(ParseUser.getCurrentUser()));
    		t.setUser(ParseUser.getCurrentUser());
    		t.setCompleted(false);
    		t.saveEventually();
    		// delete textfield
    		mTaskInput.setText("");
    		mAdapter.insert(t, 0);
    	}
    	updateData();
    }
    
    public void updateData() {
    	ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
    	query.whereEqualTo("user", ParseUser.getCurrentUser());
    	  query.findInBackground(new FindCallback<Task>() {
    	          
    	      @Override
    	      public void done(List<Task> tasks, ParseException error) {
    	          if(tasks != null){
    	              mAdapter.clear();
    	              mAdapter.addAll(tasks);
    	          }
    	      }
    	  });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	  case R.id.action_logout:
    	      ParseUser.logOut();
    	      Intent intent = new Intent(this, LoginActivity.class);
    	      startActivity(intent);
    	      finish();
    	      return true;
    	  }
    	  return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      Task task = mAdapter.getItem(position);
      TextView taskDescription = (TextView) view.findViewById(R.id.task_description);

      task.setCompleted(!task.isCompleted());

      if(task.isCompleted()){
          taskDescription.setPaintFlags(taskDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      }else{
          taskDescription.setPaintFlags(taskDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
      }

      task.saveEventually();
    }
}


















