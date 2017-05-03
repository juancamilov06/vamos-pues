package co.vamospues.vamospues.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Manuela Duque M on 17/03/2017.
 */

public class PlaceItem implements Parcelable{

    private int id;
    private double price;
    private double discount;
    private Place place;
    private Item item;

    public PlaceItem(){}

    private PlaceItem(Parcel in){
        id = in.readInt();
        price = in.readDouble();
        discount = in.readDouble();
        place = in.readParcelable(Place.class.getClassLoader());
        item = in.readParcelable(Item.class.getClassLoader());
    }

    public static final Creator<PlaceItem> CREATOR = new Creator<PlaceItem>() {
        @Override
        public PlaceItem createFromParcel(Parcel in) {
            return new PlaceItem(in);
        }

        @Override
        public PlaceItem[] newArray(int size) {
            return new PlaceItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(discount);
        dest.writeDouble(price);
        dest.writeParcelable(place, flags);
        dest.writeParcelable(item, flags);
    }
}
