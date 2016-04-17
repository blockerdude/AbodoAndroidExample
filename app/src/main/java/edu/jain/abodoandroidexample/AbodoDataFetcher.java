package edu.jain.abodoandroidexample;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

/*
This class is designed to fetch JSON data from a desired endpoint, and turn it into a useful object.
 */
public class AbodoDataFetcher {

    private Context curContext;
    //This could be set in a 'real' app.
    private static final String DATAENDPOINTLOCATION =
            "https://www.abodo.com/search/get_property_results.json?lat=43.0752983&lng=" +
                    "-89.39389799999998&min_rent=&max_rent=&passed_search_area_text=Madison,%20WI%20Apartments";
    public AbodoDataFetcher(Context currentContext){
        curContext = currentContext;
    }
    private JSONArray getJSONData(){
        String result;
        new AsyncDataFetch(curContext).execute(DATAENDPOINTLOCATION);

        return null;
    }


}
