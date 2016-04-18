package edu.jain.abodoandroidexample;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class AsyncImageFetcher extends AsyncTask<String, Integer, Drawable> {

    private AsyncImageFetchResponse delegate = null;
    public interface AsyncImageFetchResponse {
        void processFinish(Drawable output);
    }

    public AsyncImageFetcher(AsyncImageFetchResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Drawable doInBackground(String... params) {
        try {
            InputStream is = (InputStream) new URL(params[0]).getContent();
            return Drawable.createFromStream(is, params[1]);
        }
        catch(MalformedURLException e){
            return null;
        }
        catch(IOException e){
            return null;
        }
    }

    @Override
    protected void onPostExecute(Drawable result){
        delegate.processFinish(result);
    }
}
