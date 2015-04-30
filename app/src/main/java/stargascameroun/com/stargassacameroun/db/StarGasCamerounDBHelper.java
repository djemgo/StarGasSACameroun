package stargascameroun.com.stargassacameroun.db;

/**
 * Created by 623990 on 3/17/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import stargascameroun.com.stargassacameroun.model.CustomerOrder;
import stargascameroun.com.stargassacameroun.model.CustomerProfile;

/**
 * Created by 623990 on 3/9/2015.
 */
public class StarGasCamerounDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "stargascameroun.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = " , ";
    private static final String SQL_CREATE_CUSTOMER_PROFILE_TABLE =
            "CREATE TABLE " + StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_TABLE_NAME + " (" +
                    StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
                    StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_TELEPHONE + TEXT_TYPE + COMMA_SEP +
                    StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_FIRSTNAME + TEXT_TYPE + COMMA_SEP +
                    StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_LASTNAME + TEXT_TYPE + COMMA_SEP +
                    StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_EMAIL + TEXT_TYPE + COMMA_SEP +
                    StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_PASSWORD + TEXT_TYPE + COMMA_SEP +
                    StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_REGION + TEXT_TYPE + COMMA_SEP +
                    StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_CITY + TEXT_TYPE + COMMA_SEP +
                    StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_NEIGHBORHOOD + TEXT_TYPE + " )";
    private static final String SQL_CREATE_CUSTOMER_ORDER_TABLE =
            "CREATE TABLE " + StarGasCamerounDBContract.CustomerOrder.CUSTOMER_ORDER_TABLE_NAME + " (" +
                    StarGasCamerounDBContract.CustomerOrder.CUSTOMER_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
                    StarGasCamerounDBContract.CustomerOrder.CUSTOMER_PROFILE_FOREIGN_ID + " INTEGER, " +
                    StarGasCamerounDBContract.CustomerOrder.CUSTOMER_ORDER_DATE + TEXT_TYPE + COMMA_SEP +
                    StarGasCamerounDBContract.CustomerOrder.CUSTOMER_ORDER_QUANTITY + TEXT_TYPE + COMMA_SEP +
                    "FOREIGN KEY(" + StarGasCamerounDBContract.CustomerOrder.CUSTOMER_PROFILE_FOREIGN_ID + ") " + " REFERENCES " + StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_TABLE_NAME + " (" + StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_ID + " ))";
    private static final int DATABASE_VERSION = 2;
    private static final String SQL_DELETE_CUSTOMER_PROFILE_TABLE =
            "DROP TABLE IF EXISTS " + StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_TABLE_NAME;

    private static final String SQL_DELETE_CUSTOMER_ORDER_TABLE =
            "DROP TABLE IF EXISTS " + StarGasCamerounDBContract.CustomerOrder.CUSTOMER_ORDER_TABLE_NAME;


    public StarGasCamerounDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CUSTOMER_PROFILE_TABLE);
        db.execSQL(SQL_CREATE_CUSTOMER_ORDER_TABLE);
        System.out.println();
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_CUSTOMER_PROFILE_TABLE);
        db.execSQL(SQL_DELETE_CUSTOMER_ORDER_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long createNewCustomer(String registrationFirstname, String registrationLastname, String registrationPhone,
                                  String registrationEmail, String registrationPassword, String registrationCity,
                                  String registrationNeighborhood, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_FIRSTNAME, registrationFirstname);
        values.put(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_LASTNAME, registrationLastname);
        values.put(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_TELEPHONE, registrationPhone);
        values.put(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_EMAIL, registrationEmail);
        values.put(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_PASSWORD, registrationPassword);
        values.put(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_CITY, registrationCity);
        values.put(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_REGION, "Centre");
        values.put(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_NEIGHBORHOOD, registrationNeighborhood);

        long newRowId = -1;
        try {
            db.beginTransaction();
            newRowId = db.insert(
                    StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_TABLE_NAME,
                    null,
                    values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            System.out.print(false);
            return -1;
        } finally {
            db.endTransaction();
        }
        return newRowId;
    }

    public long updateCustomer(String registrationFirstname, String registrationLastname, String registrationPhone,
                               String registrationEmail, String registrationCity,
                               String registrationNeighborhood, SQLiteDatabase db, String customerID) {
        ContentValues values = new ContentValues();
        values.put(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_FIRSTNAME, registrationFirstname);
        values.put(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_LASTNAME, registrationLastname);
        values.put(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_TELEPHONE, registrationPhone);
        values.put(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_EMAIL, registrationEmail);
//        values.put(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_PASSWORD, registrationPassword);
        values.put(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_CITY, registrationCity);
        values.put(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_REGION, "Centre");
        values.put(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_NEIGHBORHOOD, registrationNeighborhood);

        long newRowId = -1;
        try {
            db.beginTransaction();
            newRowId = db.update(
                    StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_TABLE_NAME,
                    values,
                    StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_ID + "=?",
                    new String[]{customerID + ""}
            );
            db.setTransactionSuccessful();
        } catch (Exception e) {
            System.out.print(false);
            return -1;
        } finally {
            db.endTransaction();
            db.close();
        }
        return newRowId;
    }


    public CustomerProfile findCustomer(String telephone, SQLiteDatabase db) {
        boolean customerExist = false;
        CustomerProfile customerProfile = null;
        long customerId;
        String lastname;
        String firstname;
        String email;
        String password;
        String city;
        String neighborhood;

        String[] projection = {
                StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_ID,
                StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_FIRSTNAME,
                StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_LASTNAME,
                StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_TELEPHONE,
                StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_EMAIL,
                StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_PASSWORD,
                StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_REGION,
                StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_CITY,
                StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_NEIGHBORHOOD};
        Cursor cursor = null;
        try {
            cursor = db.query(
                    StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_TELEPHONE + "=?",                                // The columns for the WHERE clause
                    new String[]{telephone},                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );
        } catch (Exception e) {
            e.printStackTrace();
//            TODO Handle this!
            return null;

        }
        if (cursor.moveToFirst()) {

            lastname = cursor.getString(cursor.getColumnIndexOrThrow(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_LASTNAME));
            customerId = cursor.getLong(cursor.getColumnIndexOrThrow(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_ID));
            firstname = cursor.getString(cursor.getColumnIndexOrThrow(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_FIRSTNAME));
            email = cursor.getString(cursor.getColumnIndexOrThrow(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_EMAIL));
            password = cursor.getString(cursor.getColumnIndexOrThrow(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_PASSWORD));
            city = cursor.getString(cursor.getColumnIndexOrThrow(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_CITY));
            neighborhood = cursor.getString(cursor.getColumnIndexOrThrow(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_NEIGHBORHOOD));

            customerProfile = new CustomerProfile(customerId, telephone, lastname, firstname, email, password, city, neighborhood);
        }
        return customerProfile;


    }

    public List<String> findCustomerPhoneNumbers(SQLiteDatabase db) {
        String telephoneNumber;
        List<String> telephoneNumberList = new ArrayList<String>();

        String[] projection = {
                StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_TELEPHONE};
        Cursor cursor = null;
        try {
            cursor = db.query(
                    StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );
        } catch (Exception e) {
            e.printStackTrace();
//            TODO Handle this!
            return null;

        }
        if (cursor.moveToFirst()) {
            do {
                telephoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(StarGasCamerounDBContract.CustomerProfile.CUSTOMER_PROFILE_TELEPHONE));
                telephoneNumberList.add(telephoneNumber);
            }
            while (cursor.moveToNext());
        }
        return telephoneNumberList;
    }

    public ArrayList<CustomerOrder> findCustomerOrders(long customerID, SQLiteDatabase db) {
        boolean customerExist = false;
        ArrayList<CustomerOrder> customerOrders = new ArrayList<CustomerOrder>();
        String orderDate;
        String orderQuantity;

        String[] projection = {
                StarGasCamerounDBContract.CustomerOrder.CUSTOMER_ORDER_ID,
                StarGasCamerounDBContract.CustomerOrder.CUSTOMER_PROFILE_FOREIGN_ID,
                StarGasCamerounDBContract.CustomerOrder.CUSTOMER_ORDER_QUANTITY,
                StarGasCamerounDBContract.CustomerOrder.CUSTOMER_ORDER_DATE,
        };
        Cursor cursor = null;
        try {
            cursor = db.query(
                    StarGasCamerounDBContract.CustomerOrder.CUSTOMER_ORDER_TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    StarGasCamerounDBContract.CustomerOrder.CUSTOMER_PROFILE_FOREIGN_ID + "=?",                                // The columns for the WHERE clause
                    new String[]{customerID + ""},                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor.moveToFirst()) {
            do {
                CustomerOrder customerOrder;

                orderDate = cursor.getString(cursor.getColumnIndexOrThrow(StarGasCamerounDBContract.CustomerOrder.CUSTOMER_ORDER_DATE));
                orderQuantity = cursor.getString(cursor.getColumnIndexOrThrow(StarGasCamerounDBContract.CustomerOrder.CUSTOMER_ORDER_QUANTITY));
                customerOrder = new CustomerOrder(customerID, orderDate, orderQuantity);
                customerOrders.add(customerOrder);
            }
            while (cursor.moveToNext());
        } else {
            return null;
        }
        return customerOrders;
    }


    public long addOrder(CustomerOrder customerOrder, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(StarGasCamerounDBContract.CustomerOrder.CUSTOMER_PROFILE_FOREIGN_ID, customerOrder.getCustomerID());
        values.put(StarGasCamerounDBContract.CustomerOrder.CUSTOMER_ORDER_DATE, customerOrder.getOrderDate());
        values.put(StarGasCamerounDBContract.CustomerOrder.CUSTOMER_ORDER_QUANTITY, customerOrder.getOrderQuantity());

        long newRowId;
        try {
            db.beginTransaction();
            newRowId = db.insert(
                    StarGasCamerounDBContract.CustomerOrder.CUSTOMER_ORDER_TABLE_NAME,
                    null,
                    values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            System.out.print(false);
            return -1;
        } finally {
            db.endTransaction();
        }

        return newRowId;

    }

    public String retrieveTodaysDate() {
        Date todaysDate = new Date();
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMM/yy");
        try {
            dateString = sdfr.format(todaysDate);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return dateString;
    }

}