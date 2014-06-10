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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
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
		String story, score = "0", id = "0";
		try {
			// If the json string is empty show the error message
			if (jsonStr.equals("")) {
				story = getResources().getString(R.string.no_story_error);
			} else {
				// Get text and id from json
				storyObj = new JSONObject(jsonStr);
				story = (String) storyObj.get("text");
				score = Integer.toString((Integer) storyObj.get("rating"));
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
			
		// Disable ratingbar and send button if story not received
		} else {
			Button sendRate = (Button) findViewById(R.id.send_rate);
			sendRate.setEnabled(false);
			sendRate.setTextColor(getResources().getColor(R.color.disabled_button));
			
			RatingBar ratingBar = (RatingBar)findViewById(R.id.rating_bar);
			ratingBar.setOnTouchListener(new OnTouchListener() {
			        public boolean onTouch(View v, MotionEvent event) {
			            return true;
			        }
			    });
			ratingBar.setFocusable(false);
			ratingBar.setVisibility(View.GONE);
			TextView scoreView = (TextView) findViewById(R.id.story_score);
			scoreView.setVisibility(View.GONE);
		}
		
		// Set the text in the story_view to the other user story
		TextView storyView = (TextView) findViewById(R.id.story_view);
		storyView.setText(story);
		// Set the text in the score view to the story rating
		TextView scoreView = (TextView) findViewById(R.id.story_score);
		scoreView.setText(score);
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
		
		RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar);
		int rating = (int) Math.round(ratingBar.getRating() * 2.5);
		
    	// Update rating if user hit like button
		try {
			// Store json object with value of user rating
			JSONObject storyRate = getStoryObj();	
			storyRate.put("rating", rating);
			
			// Post json data with rating to server
			Utility.postJsonData(storyRate.toString(), getResources().getString(R.string.server_rate_story));
		
		} catch (Exception e) {
			getResources().getString(R.string.server_error);
			e.printStackTrace();
		}
	    // Go back to main activity after sending rating
	    NavUtils.navigateUpFromSameTask(this);
	}
	
	public JSONObject getStoryObj() { return storyObj; }
	
    public void goBack(View view) {
    	finish();
    }

}
