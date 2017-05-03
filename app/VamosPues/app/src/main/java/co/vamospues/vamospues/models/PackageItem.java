package co.vamospues.vamospues.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Manuela Duque M on 21/04/2017.
 */

public class PackageItem implements Parcelable{

    private int id;
    private String name;
    private double price;
    private int quantity;
    private double total;
    private double discount;

    public PackageItem(){}

    private PackageItem(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
        total = in.readDouble();
        discount = in.readDouble();
    }

    public static final Creator<PackageItem> CREATOR = new Creator<PackageItem>() {
        @Override
        public PackageItem createFromParcel(Parcel in) {
            return new PackageItem(in);
        }

        @Override
        public PackageItem[] newArray(int size) {
            return new PackageItem[size];
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
        dest.writeDouble(price);
        dest.writeInt(quantity);
        dest.writeDouble(total);
        dest.writeDouble(discount);
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
