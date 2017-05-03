package co.vamospues.vamospues.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Manuela Duque M on 22/04/2017.
 */

public class SelectablePlaceItem implements Parcelable{

    private int id;
    private double price;
    private double discount;
    private Place place;
    private Item item;
    private boolean selected;

    public SelectablePlaceItem(){}

    private SelectablePlaceItem(Parcel in){
        id = in.readInt();
        price = in.readDouble();
        discount = in.readDouble();
        place = in.readParcelable(Place.class.getClassLoader());
        item = in.readParcelable(Item.class.getClassLoader());
        selected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<SelectablePlaceItem> CREATOR = new Parcelable.Creator<SelectablePlaceItem>() {
        @Override
        public SelectablePlaceItem createFromParcel(Parcel in) {
            return new SelectablePlaceItem(in);
        }

        @Override
        public SelectablePlaceItem[] newArray(int size) {
            return new SelectablePlaceItem[size];
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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
        dest.writeByte((byte) (selected ? 1 : 0));
    }
}
