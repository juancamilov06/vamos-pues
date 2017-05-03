package co.vamospues.vamospues.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Manuela Duque M on 21/04/2017.
 */

public class PlacePackage implements Parcelable {

    private int id;
    private Package aPackage;
    private Place place;
    private double price;
    private double discount;

    public PlacePackage(){}

    private PlacePackage(Parcel in) {
        id = in.readInt();
        aPackage = in.readParcelable(Package.class.getClassLoader());
        place = in.readParcelable(Place.class.getClassLoader());
        price = in.readDouble();
        discount = in.readDouble();
    }

    public static final Creator<PlacePackage> CREATOR = new Creator<PlacePackage>() {
        @Override
        public PlacePackage createFromParcel(Parcel in) {
            return new PlacePackage(in);
        }

        @Override
        public PlacePackage[] newArray(int size) {
            return new PlacePackage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(aPackage, flags);
        dest.writeParcelable(place, flags);
        dest.writeDouble(price);
        dest.writeDouble(discount);
    }

    public Package getaPackage() {
        return aPackage;
    }

    public void setaPackage(Package aPackage) {
        this.aPackage = aPackage;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
