package com.lakehead.textbookmarket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONArray;
import org.apache.http.client.HttpClient;
import android.util.Log;
import android.os.AsyncTask;


class GetJSONArrayTask extends AsyncTask<NameValuePair, Void, JSONArray> {

    private OnTaskCompleted listener;

    public GetJSONArrayTask(OnTaskCompleted listener)
    {
        this.listener=listener;
    }

    public GetJSONArrayTask(OnTaskCompleted listener, String path)
    {
        this.listener = listener;
        this.path = path;
    }

    //path refers to the URL, excluding the address and the port. "/api/book", for example.
    String path;
    JSONArray jArray;
    private final String SERVER_ADDRESS = "http://107.170.7.58:4567";

    protected JSONArray doInBackground(NameValuePair... params)
    {
        String result=new String();

        try
        {
            URI uri = new URI(SERVER_ADDRESS+path);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(uri);
            //if(params.length > 1)
            //{
            //    httpPost.addHeader("Authorization", "Token " + params[1]);
            //}

            //The POST parameters are will be added as NameValuePairs
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            //Add every NameValuePair to a list
            for(NameValuePair p: params)
            {
                nameValuePairs.add(p);
            }

            //Add all our POST parameters to the request
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


            //Execute the request and get a response
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if(entity != null)
            {
                //Try to read the result from the HTTP request into a String.
                try
                {
                    InputStream is = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    is.close();
                    result = sb.toString();
                }
                catch (Exception e) //Couldn't grab the string from HttpEntity for some reason
                {
                    Log.e("Exceptions", "Error converting result " + e.toString());
                }

                // try parse the string to a JSON Array
                try
                {
                    jArray = new JSONArray(result);
                    return jArray;

                }
                catch (JSONException e)
                {
                    Log.d("Debug","Error parsing JSON");
                    Log.e("Exceptions", e.toString());
                }
            }
            else
            {
                return null;
            }
        }
        catch(Exception e)
        {
            //This typically happens if the url can't be reached
            Log.d("Debug","Getting URL failed!");
            Log.e("Exceptions",e.toString());
            e.printStackTrace();
            return null;
        }
        return null;
    }

    //Callback for the activity. Pass the JSONArray back to it.
    protected void onPostExecute(JSONArray jArray)
    {
        listener.onTaskCompleted(jArray);
    }
}