package co.vamospues.vamospues.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Manuela Duque M on 05/04/2017.
 */

public class Event implements Parcelable {

    private int id;
    private String name;
    private boolean starred;
    private boolean active;
    private String imageUrl;
    private String contact;
    private String date;
    private Place place;
    private String start;
    private String description;

    public Event(){}

    private Event(Parcel in) {
        id = in.readInt();
        name = in.readString();
        starred = in.readByte() != 0;
        active = in.readByte() != 0;
        imageUrl = in.readString();
        contact = in.readString();
        date = in.readString();
        place = in.readParcelable(Place.class.getClassLoader());
        start = in.readString();
        description = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeByte((byte) (starred ? 1 : 0));
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeString(imageUrl);
        dest.writeString(contact);
        dest.writeString(date);
        dest.writeParcelable(place, flags);
        dest.writeString(start);
        dest.writeString(description);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
