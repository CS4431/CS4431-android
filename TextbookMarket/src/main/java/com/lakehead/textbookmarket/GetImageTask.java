package com.lakehead.textbookmarket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.net.URI;

/**
 * Created by tim on 10/14/13.
 */

class GetImageTask extends AsyncTask<Void, Void, Bitmap> {

    //This ImageView is the one whose bitmap we want to set
    private ImageView imageView;
    private int width;
    private int height;
    private String url;

    public GetImageTask(String url, ImageView imageView, int width, int height)
    {
        this.url = url;
        this.imageView=imageView;
        this.width = width;
        this.height = height;
    }

    //Params are of type Void because we never need them for this AsyncTasks
    protected Bitmap doInBackground(Void... params) {
        Bitmap bitmap;
        try
        {
            URI uri = new URI(url);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet method = new HttpGet(uri);

            HttpResponse response = httpclient.execute(method);
            HttpEntity entity = response.getEntity();
            if(entity != null)
            {
                if(response.getStatusLine().getStatusCode() == 200)
                {
                    BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
                    InputStream input = b_entity.getContent();

                    bitmap = BitmapFactory.decodeStream(input);
                    return bitmap;
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        catch(Exception e)
        {
            Log.e("Exceptions",e.toString());
            return null;
        }
    }

    //Here, we scale the image we got to a desired size, and set the ImageView's bitmap
    protected void onPostExecute(Bitmap bmp)
    {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, width, height, false);
        imageView.setImageBitmap(scaledBitmap);
    }
}