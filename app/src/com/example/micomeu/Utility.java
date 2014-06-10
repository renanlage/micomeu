package com.example.micomeu;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class Utility {
	
	public static String postJsonData(String data, String url) throws Exception {
		String responseData;
	    try {
		    // Create a new HttpClient and Post Header
		    HttpClient client = new DefaultHttpClient();
		    HttpPost post = new HttpPost(url);
		    
		    // Append the json string to Post
	        post.setEntity(new StringEntity(data, "utf-8"));
	        post.setHeader("Accept", "application/json");
	        post.setHeader("Content-type", "application/json");

	        // Execute HTTP Post Request
	        HttpResponse response = client.execute(post);
	        
	        // Get story of other user as json from the response
	        responseData = EntityUtils.toString(response.getEntity(), "utf-8");

	    } catch (Exception e) {
	    	throw e;
	    }
	    return responseData;
	}
	
}
