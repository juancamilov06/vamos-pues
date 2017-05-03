package co.vamospues.vamospues.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Manuela Duque M on 26/04/2017.
 */

public class ItemOrderItem implements Parcelable {

    private ItemOrder itemOrder;
    private SelectablePlaceItem selectablePlaceItem;
    private int quantity;

    public ItemOrderItem(){}

    private ItemOrderItem(Parcel in) {
        itemOrder = in.readParcelable(ItemOrder.class.getClassLoader());
        selectablePlaceItem = in.readParcelable(SelectablePlaceItem.class.getClassLoader());
        quantity = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(itemOrder, flags);
        dest.writeParcelable(selectablePlaceItem, flags);
        dest.writeInt(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemOrderItem> CREATOR = new Creator<ItemOrderItem>() {
        @Override
        public ItemOrderItem createFromParcel(Parcel in) {
            return new ItemOrderItem(in);
        }

        @Override
        public ItemOrderItem[] newArray(int size) {
            return new ItemOrderItem[size];
        }
    };

    public ItemOrder getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(ItemOrder itemOrder) {
        this.itemOrder = itemOrder;
    }

    public SelectablePlaceItem getSelectablePlaceItem() {
        return selectablePlaceItem;
    }

    public void setSelectablePlaceItem(SelectablePlaceItem selectablePlaceItem) {
        this.selectablePlaceItem = selectablePlaceItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
