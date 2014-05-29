package com.example.micomeu;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DisplayStoryActivity extends ActionBarActivity {
	
	private JSONObject storyObj;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_display_story);
		
		// Get the message from the intent
		Intent intent = getIntent();
		String jsonStr = intent.getStringExtra(MainActivity.SERVER_STORY);
	
		// Obtain user story in json data
		String story;
		String id = "0";
		try {
			// If the json string is empty show the error message
			if (jsonStr.equals("")) {
				story = getResources().getString(R.string.no_story_error);
			} else {
				// Get text and id from json
				storyObj = new JSONObject(jsonStr);
				story = (String) storyObj.get("text");
				int tempId = (Integer) storyObj.get("id");
				id = Integer.toString(tempId);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			story = getResources().getString(R.string.server_error);
		}

		// Save story as read if it was loaded
		if (storyObj != null) {
			
			// Load shared preferences to mark story as read
			SharedPreferences sharedPref = getSharedPreferences(
			        getString(R.string.preference_file), Context.MODE_PRIVATE);
			
			// Get the set with ids of read stories
			Set<String> read_ids = sharedPref.getStringSet(
					getString(R.string.saved_ids), new HashSet<String>());
			
			// Append id if story read
			SharedPreferences.Editor editor = sharedPref.edit();
			read_ids.add(id);
			editor.putStringSet(getString(R.string.saved_ids), read_ids);
			editor.commit();
			
		// Disable like and dislike buttons if story was not received
		} else {
			findViewById(R.id.like_story).setEnabled(false);
			findViewById(R.id.dislike_story).setEnabled(false);
		}
		
		// Set the text in the story_view to the other user story
		TextView storyView = (TextView) findViewById(R.id.story_view);
		storyView.setText(story);
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
			View rootView = inflater.inflate(R.layout.fragment_display_story,
					container, false);
			return rootView;
		}
	}
		
	public void rateStory(View view) {
		
		// Check if user hit the like or dislike button
		boolean likeStory = false;
	    if (view.getId() == R.id.like_story) {
	    	likeStory = true;
		
	    	// Update rating if user hit like button
			try {
				// Store json object with value of user rating
				JSONObject storyRate = getStoryObj();	
				storyRate.put("like", likeStory);
				
				// Post json data with rating to server
				Utility.postJsonData(storyRate.toString(), getResources().getString(R.string.server_rate_story));
			
			} catch (Exception e) {
				getResources().getString(R.string.server_error);
				e.printStackTrace();
			}
	    }
	    // Go back to main activity after sending rating
	    NavUtils.navigateUpFromSameTask(this);
	}
	
	public JSONObject getStoryObj() { return storyObj; }
}
