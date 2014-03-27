package com.example.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    protected static final Object CLIENT_ID = "2DLJ05OESPOJVNGS4WE0R3PGIJ5XW0T1UXDOWFUZWKGWQ5KY";
	protected static final Object CLIENT_SECRET = "GWD25TYIPH3PPFEUVKZXLEKV45K3OZS1JOO3A4ECLSZFEZ35";
	private TextView textView;
    private TextView latTextView, lngTextView;
    private EditText editText;
    private ProgressDialog progress;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
       
        textView = (TextView) findViewById(R.id.textView1);
        editText = (EditText) findViewById(R.id.editText1);
        latTextView = (TextView) findViewById(R.id.textView2);
        lngTextView = (TextView) findViewById(R.id.textView3);
        progress = new ProgressDialog(this);
      
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void fetch(View view){
    	final String urlstr = "http://tw.yahoo.com";
    	
    	AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>(){
    		
    		@Override
    		protected void onPreExecute() {
    		
    		}

			@Override
			protected String doInBackground(Void... params) {
				
				try {
					URL url = new URL(urlstr);
					URLConnection connection = url.openConnection();
					
					BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					
					StringBuilder stringBuilder = new StringBuilder();
					String line;
					
					while((line = buffer.readLine()) != null){
						stringBuilder.append(line);
					}
					
					return stringBuilder.toString();
										
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}
    		
			
			@Override
			protected void onPostExecute(String result){
				textView.setText(result);
			}
    		
    	};
    	
    	task.execute();
    	    	
    }
    
    public void fetch2(View view){
    	String address ="";
    	
    	try {
			address = URLEncoder.encode(editText.getText().toString(), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	final String urlstr = String.format("http://maps.googleapis.com/maps/api/geocode/json?address=%s&sensor=false", address);
    	
    	AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>(){
    		
    		@Override
    		protected void onPreExecute() {
    			progress.setTitle("loading...");
    			progress.show();
    		
    		}

			@Override
			protected String doInBackground(Void... params) {
				
				try {
					URL url = new URL(urlstr);
					URLConnection connection = url.openConnection();
					
					BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					
					StringBuilder stringBuilder = new StringBuilder();
					String line;
					
					while((line = buffer.readLine()) != null){
						stringBuilder.append(line);
					}
					
					

					String result = stringBuilder.toString();
					
					JSONObject data = new JSONObject(result);
					JSONObject location = data.getJSONArray("results").getJSONObject(0)
							.getJSONObject("geometry").getJSONObject("location");
					
					double lat = location.getDouble("lat");
					double lng = location.getDouble("lng");
					
					String urlstr2 = String.format("https://api.foursquare.com/v2/venues/search?client_id=%s&client_secret=%s&v=20130815&ll=%s&query=%s",CLIENT_ID, CLIENT_SECRET, lat + "," + lng,"sushi");
					
					url = new URL(urlstr2);
					connection = url.openConnection();
					
					buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					
					stringBuilder = new StringBuilder();
									
					while((line = buffer.readLine()) != null){
						stringBuilder.append(line);
					}
					
					String result2 = stringBuilder.toString();
					
					return result2;
					
										
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}
    		
			
			@Override
			protected void onPostExecute(String result){
				
				try {
					JSONObject object = new JSONObject(result);
					JSONArray array = object.getJSONObject("response").getJSONArray("venues");
					String names = "";
					for (int i = 0; i < array.length(); i++) {
						String name = array.getJSONObject(i).getString("name");
						names += name + "\n";}
					
					textView.setText(names);
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				progress.dismiss();
			}
    		
    	};
    	
    	task.execute();
    	
    }
    
}
