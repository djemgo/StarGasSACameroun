package stargascameroun.com.stargassacameroun.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import stargascameroun.com.stargassacameroun.R;
import stargascameroun.com.stargassacameroun.db.StarGasCamerounDBHelper;
import stargascameroun.com.stargassacameroun.management.SessionManager;
import stargascameroun.com.stargassacameroun.model.CustomerOrder;
import stargascameroun.com.stargassacameroun.model.CustomerProfile;


public class OrderActivity extends ActionBarActivity {
    Button backOrderButton;
    Button submitOrderButton;
    Intent backToFeaturesActivityIntent;
    Intent callLoginActivityIntent;
    SessionManager sessionManager;
    StarGasCamerounDBHelper starGasCamerounDBHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setTitle("Placez votre Commande");
        getSupportActionBar().setIcon(R.drawable.allogas247_01);
        backOrderButton = (Button) findViewById(R.id.backOrderButton);
        submitOrderButton = (Button) findViewById(R.id.submitOrderButton);
        backToFeaturesActivityIntent = new Intent(this, FeaturesActivity.class);
        callLoginActivityIntent = new Intent(this, LoginActivity.class);
        sessionManager = new SessionManager(getApplicationContext());
        starGasCamerounDBHelper = new StarGasCamerounDBHelper(this);
        db = starGasCamerounDBHelper.getWritableDatabase();
        backOrderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(backToFeaturesActivityIntent);
                finish();
            }
        });

        submitOrderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText quantityEditText = (EditText) findViewById(R.id.quantiteEditText);
                String quantity = quantityEditText.getText().toString();
                if (quantity == null || quantity.length() == 0) {
                    quantityEditText.setError("La quantite est requise SVP");
                } else {
                    quantity = quantityEditText.getText().toString();
                    if (sessionManager.sessionOpen()) {
                        callLoginActivityIntent.putExtra("orderQuantity", quantity);
                        callLoginActivityIntent.removeExtra("from");
                        callLoginActivityIntent.putExtra("from", "order");
                        String customerPhone = sessionManager.sessionId();
                        String todaysDate = starGasCamerounDBHelper.retrieveTodaysDate();
                        CustomerProfile customerProfile = starGasCamerounDBHelper.findCustomer(customerPhone, db);
                        long customerID = customerProfile.getCustomerId();
                        CustomerOrder customerOrder = new CustomerOrder(customerID, todaysDate, quantity);
                        starGasCamerounDBHelper.addOrder(customerOrder, db);
                        forwardOrderPerSMS(quantity);
                        Toast.makeText(v.getContext(), "Merci de commander a StarGas, Cameroun",
                                Toast.LENGTH_SHORT).show();
                        startActivity(backToFeaturesActivityIntent);
                        finish();
                    } else {
                        callLoginActivityIntent.putExtra("from", "order");
                        callLoginActivityIntent.putExtra("orderQuantity", quantity);
                        startActivity(callLoginActivityIntent);
                        finish();
                    }
                }

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order, menu);
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

    private void forwardOrderPerSMS(String bottlesCount) {
        char openBracket = '(';
        char closeBracket = ')';
        String bottles = " bouteille" + openBracket + "s" + closeBracket;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("2487013903", null, "Commande de " + bottlesCount + bottles, null, null);
            Toast.makeText(getApplicationContext(), "Commande Envoyee, Merci!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS failed, please try again.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


}
