package co.vamospues.vamospues.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Manuela Duque M on 09/02/2017.
 */

public class Place implements Parcelable{

    private int id;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private String address;
    private boolean isDisco;
    private boolean isActive;
    private double budget;
    private String limitHour;
    private String imageUrl;
    private String open;
    private String close;
    private Music music;
    private Zone zone;

    public Place(){}

    private Place(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        address = in.readString();
        isDisco = in.readByte() != 0;
        isActive = in.readByte() != 0;
        budget = in.readDouble();
        limitHour = in.readString();
        imageUrl = in.readString();
        open = in.readString();
        close = in.readString();
        music = in.readParcelable(Music.class.getClassLoader());
        zone = in.readParcelable(Zone.class.getClassLoader());
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isDisco() {
        return isDisco;
    }

    public void setDisco(boolean disco) {
        isDisco = disco;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getLimitHour() {
        return limitHour;
    }

    public void setLimitHour(String limitHour) {
        this.limitHour = limitHour;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static final Parcelable.Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(address);
        dest.writeByte((byte) (isDisco ? 1 : 0));
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeDouble(budget);
        dest.writeString(limitHour);
        dest.writeString(imageUrl);
        dest.writeString(open);
        dest.writeString(close);
        dest.writeParcelable(music, flags);
        dest.writeParcelable(zone, flags);
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }
}
