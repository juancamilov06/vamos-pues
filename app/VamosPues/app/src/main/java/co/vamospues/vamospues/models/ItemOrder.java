package co.vamospues.vamospues.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Manuela Duque M on 26/04/2017.
 */

public class ItemOrder implements Parcelable{

    private String id;
    private int userId;
    private String date;
    private boolean redeemed;
    private double total;

    public ItemOrder(){}

    private ItemOrder(Parcel in) {
        id = in.readString();
        userId = in.readInt();
        date = in.readString();
        redeemed = in.readByte() != 0;
        total = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(userId);
        dest.writeString(date);
        dest.writeByte((byte) (redeemed ? 1 : 0));
        dest.writeDouble(total);
    }

    public static final Creator<ItemOrder> CREATOR = new Creator<ItemOrder>() {
        @Override
        public ItemOrder createFromParcel(Parcel in) {
            return new ItemOrder(in);
        }

        @Override
        public ItemOrder[] newArray(int size) {
            return new ItemOrder[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUser() {
        return userId;
    }

    public void setUser(int user) {
        this.userId = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isRedeemed() {
        return redeemed;
    }

    public void setRedeemed(boolean redeemed) {
        this.redeemed = redeemed;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
