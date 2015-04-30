package stargascameroun.com.stargassacameroun.model;

/**
 * Created by 623990 on 3/17/2015.
 */
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 623990 on 3/9/2015.
 */
public class CustomerOrder implements Parcelable {
    private String orderDate;
    private String orderQuantity;
    private long customerID;

    public CustomerOrder(long customerID, String orderDate, String orderQuantity) {
        this.customerID = customerID;
        this.orderDate = orderDate;
        this.orderQuantity = orderQuantity;
    }

    public long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(long customerID) {
        this.customerID = customerID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}