package com.example.micomeu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	public final static String EXTRA_STORY = "com.example.micomeu.STORY";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
		//Intent intent = new Intent(this,DisplayStoryActivity.class);
		// Get story as string from view
		EditText editText = (EditText) findViewById(R.id.edit_story);
		String text = editText.getText().toString();
		
		// Store story in a json object
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("text", text);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Send user data to server through a post
		postData(jsonObj.toString(), getResources().getString(R.string.server_url));
	}
	
	public String postData(String data, String url) {
		String responseData;
	    try {
		    // Create a new HttpClient and Post Header
		    HttpClient client = new DefaultHttpClient();
		    HttpPost post = new HttpPost(url);
		    
		    // Append the json string to Post
	        post.setEntity(new StringEntity(data));
	        post.setHeader("Accept", "application/json");
	        post.setHeader("Content-type", "application/json");

	        // Execute HTTP Post Request
	        HttpResponse response = client.execute(post);
	        
	        // Get story of other user as json from the response
	        responseData = EntityUtils.toString(response.getEntity(), "utf-8");
	        
	        if (responseData == null || responseData.isEmpty())
	        	throw new Exception();

	    } catch (Exception e) {
	    	responseData = getResources().getString(R.string.server_error);
	    }
	    System.out.println(responseData);
	    return responseData;
	} 

}
