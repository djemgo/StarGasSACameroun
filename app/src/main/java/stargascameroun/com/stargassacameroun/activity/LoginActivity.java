package stargascameroun.com.stargassacameroun.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent callerIntent = getIntent();

        final StarGasCamerounDBHelper starGasCamerounDBHelper = new StarGasCamerounDBHelper(this);
        final SQLiteDatabase db = starGasCamerounDBHelper.getWritableDatabase();


        setContentView(R.layout.activity_login);
        final EditText phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        final EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);


        Button backLoginButton = (Button) findViewById(R.id.returnButton);
        final Intent backToOrderActivityIntent = new Intent(this, FeaturesActivity.class);
        backLoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(backToOrderActivityIntent);
                finish();
            }
        });

        Button registrationButon = (Button) findViewById(R.id.registrationButon);
        final Intent newRegistrationIntent = new Intent(this, RegistrationActivity.class);
        registrationButon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String telephone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (isValidPhone(telephone)) {
                    CustomerProfile customerProfile = starGasCamerounDBHelper.findCustomer(telephone, db);
                    if (customerProfile == null) {
                        String orderQuantity = callerIntent.getStringExtra("orderQuantity");
                        newRegistrationIntent.putExtra("orderQuantity", orderQuantity);
                        newRegistrationIntent.putExtra("customerPhone", telephone);
                        startActivity(newRegistrationIntent);
                        finish();
                    } else {
                        Toast.makeText(v.getContext(), "Vous etes deja enregistre !!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    phoneEditText.setError("Entrez un numero a 9 chiffres SVP");
                    phoneEditText.requestFocus();
                }
            }
        });

        Button connectButton = (Button) findViewById(R.id.connectButton);
        final SessionManager sessionManager = new SessionManager(getApplicationContext());
        final Intent customerOrdersIntent = new Intent(this, CustomerOrdersActivity.class);
        final Intent updateCustomerProfileIntent = new Intent(this, CustomerProfileActivity.class);
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String callerActivity = callerIntent.getStringExtra("from");
                String telephone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (isValidPhone(telephone) && isValidPassword(password)) {
                    CustomerProfile customerProfile = starGasCamerounDBHelper.findCustomer(telephone, db);
                    if (customerProfile == null && callerActivity.equals("order")) {
                        String orderQuantity = callerIntent.getStringExtra("orderQuantity");
                        newRegistrationIntent.putExtra("orderQuantity", orderQuantity);
                        newRegistrationIntent.putExtra("customerPhone", telephone);
                        startActivity(newRegistrationIntent);
                        finish();
                    } else if (customerProfile != null && callerActivity.equals("order")) {
                        String todaysDate = starGasCamerounDBHelper.retrieveTodaysDate();
                        long customerID = customerProfile.getCustomerId();
                        String orderQuantity = callerIntent.getStringExtra("orderQuantity");
                        CustomerOrder customerOrder = new CustomerOrder(customerID, todaysDate, orderQuantity);
                        confirmQuantityDialogAndPersist(starGasCamerounDBHelper, customerOrder, db);
                        forwardOrderPerSMS(orderQuantity);
                        sessionManager.openSession(telephone);
                    } else if (customerProfile != null && callerActivity.equals("customerOrders")) {
                        customerOrdersIntent.putExtra("customerID", telephone);
                        sessionManager.openSession(telephone);
                        startActivity(customerOrdersIntent);
                        finish();
                    } else if (customerProfile != null && callerActivity.equals("updateCustomerProfile")) {
                        updateCustomerProfileIntent.putExtra("customerPhone", telephone);
                        sessionManager.openSession(telephone);
                        startActivity(updateCustomerProfileIntent);
                        finish();
                    } else {
                        newRegistrationIntent.putExtra("customerPhone", telephone);
                        startActivity(newRegistrationIntent);
                        finish();
                    }

                } else {
//                    Toast.makeText(v.getContext(), "Verifiez votre numero de telephone ou password",
//                            Toast.LENGTH_SHORT).show();
                    if (!isValidPhone(telephone)) {
                        phoneEditText.setError("Entrez un numero a 9 chiffres SVP");
                        phoneEditText.requestFocus();
                    }
                    if (!isValidPassword(password)) {
                        passwordEditText.setError("Le mot de passe doit avoir au moins 4 caracteres SVP");
                        passwordEditText.requestFocus();
                    }


                }
            }
        });
    }

    private void forwardOrderPerSMS(String bottlesCount) {
        char openBracket = '(';
        char closeBracket = ')';
        String bottles = " bouteille" + openBracket + "s" + closeBracket;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("2487013903", null, "Commande de " + bottlesCount + bottles, null, null);
            Toast.makeText(getApplicationContext(), "Commande Envoyee.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS failed, please try again.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void callNewRegistrationActivity(Intent newRegistrationIntent, String telephone) {
//        Intent orderIntent = getIntent();
        newRegistrationIntent.putExtra("customerPhone", telephone);
        startActivity(newRegistrationIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    private void confirmQuantityDialogAndPersist(StarGasCamerounDBHelper starGasCamerounDBHelper, CustomerOrder customerOrder, SQLiteDatabase db) {
        final StarGasCamerounDBHelper starGasCamerounDBHelper2 = starGasCamerounDBHelper;
        final CustomerOrder customerOrder2 = customerOrder;
        final SQLiteDatabase db2 = db;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        char openBracket = '(';
        char closeBracket = ')';
        String bottles = " bouteille" + openBracket + "s" + closeBracket + "?";
        alertDialogBuilder.setMessage(customerOrder.getOrderQuantity() + bottles);
        alertDialogBuilder.setPositiveButton("Oui",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent positveActivity = new Intent(getApplicationContext(), FeaturesActivity.class);
                        starGasCamerounDBHelper2.addOrder(customerOrder2, db2);
                        startActivity(positveActivity);

                    }
                });
//        alertDialogBuilder.setNegativeButton(R.string.negative_button,
        alertDialogBuilder.setNegativeButton("Non",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent negativeActivity = new Intent(getApplicationContext(), FeaturesActivity.class);
                        startActivity(negativeActivity);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 3) {
            return true;
        }
        return false;
    }

    private boolean isValidPhone(String phone) {
        if (phone == null || phone.length() != 9) {
            return false;
        } else {
            return true;
        }
    }


}