package co.vamospues.vamospues.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Manuela Duque M on 20/04/2017.
 */

public class User implements Parcelable {

    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private double balance;
    private String mail;

    public User(){}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        setId(dest.readInt());
        setFirstName(dest.readString());
        setLastName(dest.readString());
        setPhone(dest.readString());
        setBalance(dest.readDouble());
        setMail(dest.readString());
    }

    private User(Parcel in) {
        in.writeInt(getId());
        in.writeString(getFirstName());
        in.writeString(getLastName());
        in.writeString(getPhone());
        in.writeDouble(getBalance());
        in.writeString(getMail());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
