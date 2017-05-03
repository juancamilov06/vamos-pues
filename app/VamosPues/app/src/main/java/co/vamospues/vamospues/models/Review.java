package co.vamospues.vamospues.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Manuela Duque M on 31/03/2017.
 */

public class Review implements Parcelable{

    private int id;
    private String content;
    private double rating;
    private String created;
    private String username;
    private Place place;

    public Review(){}

    private Review(Parcel in) {
        id = in.readInt();
        content = in.readString();
        rating = in.readDouble();
        created = in.readString();
        username = in.readString();
        place = in.readParcelable(Place.class.getClassLoader());
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(content);
        dest.writeDouble(rating);
        dest.writeString(created);
        dest.writeString(username);
        dest.writeParcelable(place, flags);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
