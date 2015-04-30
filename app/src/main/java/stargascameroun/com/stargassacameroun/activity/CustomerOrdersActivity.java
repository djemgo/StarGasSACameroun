package stargascameroun.com.stargassacameroun.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import stargascameroun.com.stargassacameroun.R;
import stargascameroun.com.stargassacameroun.db.StarGasCamerounDBHelper;
import stargascameroun.com.stargassacameroun.management.SessionManager;
import stargascameroun.com.stargassacameroun.model.CustomerOrder;
import stargascameroun.com.stargassacameroun.model.CustomerProfile;

public class CustomerOrdersActivity extends Activity {
    SessionManager sessionManager;
    StarGasCamerounDBHelper starGasCamerounDBHelper;
    SQLiteDatabase db;

    Intent backToFeaturesActivityIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_orders);

        sessionManager = new SessionManager(getApplicationContext());
        starGasCamerounDBHelper = new StarGasCamerounDBHelper(this);
        db = starGasCamerounDBHelper.getWritableDatabase();

        backToFeaturesActivityIntent = new Intent(this, FeaturesActivity.class);

        Intent callerIntent = getIntent();
        String customerIDString = callerIntent.getStringExtra("customerID");
        long customerID = new Long(customerIDString);
        CustomerProfile customerProfile = starGasCamerounDBHelper.findCustomer(customerIDString,db);
        ArrayList<CustomerOrder> customerOrdersAsArray = starGasCamerounDBHelper.findCustomerOrders(customerProfile.getCustomerId(), db);


        TableLayout ordersTableLayout = (TableLayout)findViewById(R.id.ordersTableLayout);
        if(customerOrdersAsArray!=null && customerOrdersAsArray.size()!=0){
            String status = "En cours";
            String quantity;
            String date;

            for(CustomerOrder customerOrder:customerOrdersAsArray){
                quantity =customerOrder.getOrderQuantity();
                date = customerOrder.getOrderDate();
                TableRow tableRow = new TableRow(this);
                TextView dateTextView = new TextView(this);
                dateTextView.setText(date);
                dateTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


                TextView quantityTextView = new TextView(this);
                quantityTextView.setText(quantity);
                quantityTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


                TextView statusTextView = new TextView(this);
                statusTextView.setText(status);
                statusTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                tableRow.addView(quantityTextView);
                tableRow.addView(dateTextView);
                tableRow.addView(statusTextView);

                ordersTableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }
        }
            TableRow tableRow = new TableRow(this);
            Button retourButton = new Button(getApplicationContext());
            retourButton.setText("Retour");
            tableRow.addView(retourButton);
            ordersTableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            retourButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(backToFeaturesActivityIntent);
                    finish();
                }
            });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customer_orders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
