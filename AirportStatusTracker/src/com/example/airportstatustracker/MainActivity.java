package com.example.airportstatustracker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.airportstatustracker.R;



import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class MainActivity extends Activity {
	
	private Button sButton;
	private EditText editText;

	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       StrictMode.setThreadPolicy(policy); 
        sButton = (Button) findViewById(R.id.button1);
        editText = (EditText)findViewById(R.id.editText1);
        
        sButton.setOnClickListener(new View.OnClickListener(){
       	 //private Button.OnClickListener ButtonCapture = new Button.OnClickListener(){
       			
       			@Override
				public void onClick(View v){
       					
       				String Airport = "";
       				       				
       				EditText et=(EditText)findViewById(R.id.editText2);
       				Airport=et.getText().toString();
       			    
       				HttpClient httpclient = new DefaultHttpClient();
       				HttpResponse response;
       				String responseString = null;
       				try {
       				 HttpGet httpget = new HttpGet("http://services.faa.gov/airport/status/"+Airport+"?format=json");
       	             response = httpclient.execute(httpget);
       				    StatusLine statusLine = response.getStatusLine();
       				    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
       				    	
       				   
       				     
       				        ByteArrayOutputStream out = new ByteArrayOutputStream();
       				        response.getEntity().writeTo(out);
       				        out.close();
       				        responseString = out.toString();
       				     String combine = JSONAnalysis(responseString);
       				     String wind = combine.split(";")[0];
       				     String weather = combine.split(";")[1];
       				     String Status = combine.split(";")[2];
       				  String name = combine.split(";")[3];
       				     
       				     editText.setText("Airport Name: "+name+"\n"+"Airport Status:"+Status+"\n"+"Wind: "+wind + "F"+"\n" + "weather is: "+weather);
       				    } else{
       				        //Closes the connection.
       				        response.getEntity().getContent().close();
       				        throw new IOException(statusLine.getReasonPhrase());
       				    }
       				} catch (ClientProtocolException e) {
       				    e.printStackTrace();
       				} catch (IOException e) {
       				    e.printStackTrace();
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
    
    public String JSONAnalysis(String jsonString)
    {
    	String combine="1";
    	String wind="";
    	String weather="";
    	String status="";
    	String name="";
    	JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(jsonString);
			JSONObject  obser=jsonObj.getJSONObject("weather");    	
			JSONObject  obser2=jsonObj.getJSONObject("status"); 
			//JSONObject  obser3=jsonObj.getJSONObject("name"); 
			
	    	wind=obser.getString("wind");
	    	weather = obser.getString("weather");
	    	status = obser2.getString("reason");
	    	name = jsonObj.getString("name");
	    	
	    	combine = wind+";"+weather+";"+ status+";"+name ;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
    
    	
    	return combine;
    }
    
}
