package stargascameroun.com.stargassacameroun.db;

/**
 * Created by 623990 on 3/17/2015.
 */

import android.provider.BaseColumns;

/**
 * Created by 623990 on 3/9/2015.
 */
public final class StarGasCamerounDBContract {
    public StarGasCamerounDBContract() {
    }
    /* Inner class that defines the table CustomerProfile */
    public static abstract class CustomerProfile implements BaseColumns {
        /*Customer Profile Table*/
        public static final String CUSTOMER_PROFILE_TABLE_NAME = "customerprofile";
        public static final String CUSTOMER_PROFILE_ID = "customerid";
        public static final String CUSTOMER_PROFILE_TELEPHONE = "telephone";
        public static final String CUSTOMER_PROFILE_FIRSTNAME = "firstname";
        public static final String CUSTOMER_PROFILE_LASTNAME = "lastname";
        public static final String CUSTOMER_PROFILE_EMAIL = "email";
        public static final String CUSTOMER_PROFILE_PASSWORD = "password";
        public static final String CUSTOMER_PROFILE_REGION = "region";
        public static final String CUSTOMER_PROFILE_CITY = "city";
        public static final String CUSTOMER_PROFILE_NEIGHBORHOOD = "neighborhood";
    }
    /*Customer Orders Table*/
    public static abstract class CustomerOrder implements BaseColumns {
        public static final String CUSTOMER_ORDER_TABLE_NAME = "customerorder";
        public static final String CUSTOMER_ORDER_ID = "orderid";
        public static final String CUSTOMER_PROFILE_FOREIGN_ID = "customerid";
        public static final String CUSTOMER_ORDER_DATE = "orderdate";
        public static final String CUSTOMER_ORDER_QUANTITY = "orderquantity";

    }
}
