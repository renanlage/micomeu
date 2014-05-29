package com.example.micomeu;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {

	public final static String SERVER_STORY = "com.example.micomeu.SERVER_STORY";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Check if user can view another story
		// Get the current time in milliseconds
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		long now_time = today.toMillis(false);
		
		// Get last saved time
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

		long numViews = sharedPref.getLong(getString(R.string.num_views), 1000);
		long saved_time = sharedPref.getLong(getString(R.string.last_access_time), now_time);
				
		// Increment in 1 for each day since last visit
		today.setToNow();
		now_time = today.toMillis(false);
		long time_passed = (now_time - saved_time) / Long.valueOf(86400000);
		numViews += time_passed;
		
		// If any number of views was incremented update on shared preferences
		if (time_passed > 0) {
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putLong(getString(R.string.last_access_time), now_time);
			editor.commit();
		}
		
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	public void sendStory(View view) {
		// Intent to store the server response data
		Intent intent = new Intent(this, DisplayStoryActivity.class);
		String responseData;
		
		// Load shared preferences to send list of read stories
		SharedPreferences sharedPref = getSharedPreferences(
		        getString(R.string.preference_file), Context.MODE_PRIVATE);
		
		// Get the set with ids of read stories
		Set<String> ids_read = sharedPref.getStringSet(
				getString(R.string.saved_ids), new HashSet<String>());
		
		try {
			JSONObject jsonObj = new JSONObject();
			
			// Store text in json object if story was sent
			if (view.getId() == R.id.send_story) {
				
				// Get story as string from view
				EditText editText = (EditText) findViewById(R.id.edit_story);
				String text = editText.getText().toString();
				
				// Store story in a json object
				jsonObj.put("text", text);
			}
			
			// Store the list of read stories
			jsonObj.put("ids_read", new JSONArray(ids_read));
			
			// Post user data to server as json object
			// retrieve response from server with other story as json string
			responseData = Utility.postJsonData(jsonObj.toString(), getResources().getString(R.string.server_send_story));

		} catch (Exception e) {
			e.printStackTrace();
			responseData = getResources().getString(R.string.server_error);
		}
		// Add response story to intent and pass it to next view
		intent.putExtra(SERVER_STORY, responseData);
		startActivity(intent);
	}
}
