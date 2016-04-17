package edu.jain.abodoandroidexample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class AsyncDataFetch extends AsyncTask<String, Integer, String> {

    private ProgressDialog progressDialog;
    private InputStream inputStream;
    private Context currentContext;

    public AsyncDataFetch(Context currentContext){
        this.currentContext = currentContext;
    }

    @Override
    protected void onPreExecute(){
        progressDialog = new ProgressDialog(currentContext);
        progressDialog.show();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                AsyncDataFetch.this.cancel(true);
            }
        });
    }
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection httpRequest = null;
        String toReturn = null;
        try {
            //hard coded to be expecting the URL to be passed in on position 0.
            //could put this in a loop, get an aggregate of multiple end points.
            URL url = new URL(params[0]);
            httpRequest = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(httpRequest.getInputStream());
            toReturn = convertStreamToString(inputStream);
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally {
            if(httpRequest != null) {
                httpRequest.disconnect();
            }
        }

        //will return null if failed to read.
        return toReturn;
    }
    @Override
    protected void onPostExecute(String result){
        //can update the UI thread, or pass this info off to some other place

    }
    /*
    This method was found from http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string

     */
    private static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


}
