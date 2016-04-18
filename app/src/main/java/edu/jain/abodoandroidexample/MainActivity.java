package edu.jain.abodoandroidexample;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private boolean touched = false;
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
                    final ArrayList<ListingView> listings = new ArrayList<>();
                    LinearLayout listingsView = (LinearLayout) findViewById(R.id.LinearLayout_listings);
                    JSONObject curObject;
                    ListingView curListing;
                    for(int x=0; x<jsonArray.length(); x++){
                        curObject = jsonArray.getJSONObject(x);
                        ViewGroup expanded = (ViewGroup) View.inflate(MainActivity.this, R.layout.listing_view, listingsView);
                        TextView listingPrice = (TextView) listingsView.getChildAt(x).findViewById(R.id.TextView_listingPrice);
                        TextView listingAddress = (TextView) listingsView.getChildAt(x).findViewById(R.id.TextView_propAddress);
                        TextView listingTitle = (TextView) listingsView.getChildAt(x).findViewById(R.id.TextView_listingTitle);
                        ImageButton listingImage = (ImageButton) listingsView.getChildAt(x).findViewById(R.id.ImageButton_listing);
                        curListing = getListingFromJSONObject(curObject);
                        if(curListing != null) {
                            listings.add(curListing);
                            listingPrice.setText(curListing.getPriceRange());
                            listingAddress.setText(curListing.getAddress());
                            listingTitle.setText(curListing.getTitle());
                            setImage(listingImage, curListing.getImageURL(), curListing.getAddress());
                        }
                        final ListingView finalCurListing = curListing;
                        listingImage.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (!touched) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                    builder.setTitle(finalCurListing.getTitle());
                                    builder.setMessage("This listing is for " + finalCurListing.getAddress());
                                    AlertDialog dialog = builder.create();
                                    dialog.show();

                                }
                                else{
                                    touched = false;
                                }
                                return true;
                            }

                        });
                    }
                }
                catch(JSONException e){
                    //malformed JSON
                    e.printStackTrace();
                }
            }
        }).execute(DATAENDPOINTLOCATION);

    }

    private ListingView getListingFromJSONObject(JSONObject curObject){
        try {
            ListingView listingView;
            String address, priceRange, title, imageURL;
            if (curObject.has("address")) {
                address = curObject.getString("address");
            } else {
                address = curObject.getString("prop_display_name");
            }
            priceRange = curObject.getString("rent_range");
            title = curObject.getString("beds_range");
            imageURL = curObject.getString("tile_url");

            listingView = new ListingView(imageURL, priceRange, title, address);
            return listingView;
        }
        catch(JSONException e){
            return null;
        }

    }
    private void setImage(final ImageButton button, String url, String desc){
        new AsyncImageFetcher(new AsyncImageFetcher.AsyncImageFetchResponse() {
            @Override
            public void processFinish(Drawable output) {
                if(output != null){
                    button.setImageDrawable(output);
                }
                else{
                    //image is null, for some reason it failed to fetch
                }
            }
        }).execute(url, desc);
        //for some reason it is much faster to use these values then get from curListing
    }



}
