package edu.jain.abodoandroidexample;

import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    private static final String DATAENDPOINTLOCATION =
            "https://www.abodo.com/search/get_property_results.json?lat=43.0752983&lng=" +
                    "-89.39389799999998&min_rent=&max_rent=&passed_search_area_text=Madison,%20WI%20Apartments";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AsyncDataFetch(MainActivity.this, new AsyncDataFetch.AsyncDataFetchResponse() {
            @Override
            public void processFinish(String output) {
                //output should be the JSON string fetched from the endpoint.
                try {
                    JSONArray jsonArray = new JSONArray(output);
                    LinearLayout listingsView = (LinearLayout) findViewById(R.id.LinearLayout_listings);
                    JSONObject curListing;
                    for(int x=0; x<jsonArray.length(); x++){
                        curListing = jsonArray.getJSONObject(x);
                        ViewGroup expanded = (ViewGroup) View.inflate(MainActivity.this, R.layout.listing_view, listingsView);
                        TextView listingPrice = (TextView) listingsView.getChildAt(x).findViewById(R.id.TextView_listingPrice);
                        TextView listingAddress = (TextView) listingsView.getChildAt(x).findViewById(R.id.TextView_propAddress);
                        TextView listingTitle = (TextView) listingsView.getChildAt(x).findViewById(R.id.TextView_listingTitle);
                        final ImageView listingImage = (ImageView) listingsView.getChildAt(x).findViewById(R.id.ImageView_listing);
                        listingPrice.setText(curListing.getString("rent_range"));
                        if(curListing.has("address")){
                            listingAddress.setText(curListing.getString("address"));
                        }
                        else {
                            listingAddress.setText(curListing.getString("prop_display_name"));
                        }
                        listingTitle.setText(curListing.getString("beds_range"));

                        new AsyncImageFetcher(new AsyncImageFetcher.AsyncImageFetchResponse() {
                            @Override
                            public void processFinish(Drawable output) {
                                if(output != null) {
                                    listingImage.setImageDrawable(output);
                                }
                                else {
                                    System.out.println("Image was null");
                                }
                            }
                        }).execute(curListing.getString("tile_url"), curListing.getString("prop_display_name"));

                    }
                }
                catch(JSONException e){
                    //malformed JSON
                    e.printStackTrace();
                }
            }
        }).execute(DATAENDPOINTLOCATION);

    }


}
