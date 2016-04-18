package edu.jain.abodoandroidexample;

public class ListingView {


    private String priceRange, title, address, imageURL;
    public ListingView(String imageURL, String priceRange, String title, String address){
        setImageURL(imageURL);
        setPriceRange(priceRange);
        setTitle(title);
        setAddress(address);
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String image) {
        this.imageURL = image;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
