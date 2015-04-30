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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import stargascameroun.com.stargassacameroun.R;
import stargascameroun.com.stargassacameroun.db.StarGasCamerounDBHelper;
import stargascameroun.com.stargassacameroun.management.SessionManager;
import stargascameroun.com.stargassacameroun.model.CustomerOrder;


public class RegistrationActivity extends Activity {
    SessionManager sessionManager;
    StarGasCamerounDBHelper starGasCamerounDBHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(getApplicationContext());
        starGasCamerounDBHelper = new StarGasCamerounDBHelper(this);
        db = starGasCamerounDBHelper.getWritableDatabase();


        setContentView(R.layout.activity_registration);

        final EditText registrationFirstnameEditText = (EditText) findViewById(R.id.registrationFirstnameEditText);
        final EditText registrationLastnameEditText = (EditText) findViewById(R.id.registrationLastnameEditText);
        final EditText registrationPhoneEditText = (EditText) findViewById(R.id.registrationPhoneEditText);
        final EditText registrationEmailEditText = (EditText) findViewById(R.id.registrationEmailEditText);
        final EditText registrationPasswordEditText = (EditText) findViewById(R.id.registrationPasswordEditText);
        final EditText registrationConfirmPasswordEditText = (EditText) findViewById(R.id.registrationConfirmPasswordEditText);
        final EditText registrationCityEditText = (EditText) findViewById(R.id.registrationCityEditText);
        final EditText registrationNeighborhoodEditText = (EditText) findViewById(R.id.registrationNeighborhoodEditText);

        final Intent loginInfoIntent = getIntent();
        String customerPhone = loginInfoIntent.getStringExtra("customerPhone");
        if (registrationPhoneEditText != null) {
            registrationPhoneEditText.setText(customerPhone);
        }

        Button registrationReturnButton = (Button) findViewById(R.id.registrationReturnButton);

        final Intent backToFeaturesActivityIntent = new Intent(this, FeaturesActivity.class);
        registrationReturnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(backToFeaturesActivityIntent);
                finish();
            }
        });

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String registrationFirstname = registrationFirstnameEditText.getText().toString();
                String registrationLastname = registrationLastnameEditText.getText().toString();
                String registrationPhone = registrationPhoneEditText.getText().toString();
                String registrationEmail = registrationEmailEditText.getText().toString();
                String registrationPassword = registrationPasswordEditText.getText().toString();
                String registrationConfirmPassword = registrationConfirmPasswordEditText.getText().toString();
                String registrationCity = registrationCityEditText.getText().toString();
                String registrationNeighborhood = registrationNeighborhoodEditText.getText().toString();

                boolean isValidInput = false;
//                while (!isValidInput) {
                if (!isValidPhone(registrationPhone)) {
                    isValidInput = false;
                    registrationPhoneEditText.setError("Verifiez Votre Telephone SVP");
                    registrationPhoneEditText.requestFocus();
                } else {
                    isValidInput = true;
                }
                if (!isValidEmail(registrationEmail)) {
                    isValidInput = false;
                    registrationEmailEditText.setError("Verifiez Votre Email SVP");
                    registrationEmailEditText.requestFocus();
                } else {
                    isValidInput = true;
                }
                if (!isValidPassword(registrationPassword)) {
                    isValidInput = false;
                    registrationPasswordEditText.setError("Le mot de passe doit avoir au moins 4 caracteres SVP");
                    registrationPasswordEditText.requestFocus();
                } else {
                    isValidInput = true;
                }
                if (!isValidPassword(registrationConfirmPassword)) {
                    isValidInput = false;
                    registrationConfirmPasswordEditText.setError("Le mot de passe doit avoir au moins 4 caracteres SVP");
                    registrationConfirmPasswordEditText.requestFocus();
                } else {
                    isValidInput = true;
                }
                if (!confirmPassWord(registrationPassword, registrationConfirmPassword)) {
                    isValidInput = false;
                    registrationConfirmPasswordEditText.setError("Verifiez votre mot de passe");
                    registrationConfirmPasswordEditText.requestFocus();
                }
                if (isValidInput) {
                    long customerID = starGasCamerounDBHelper.createNewCustomer(registrationFirstname, registrationLastname, registrationPhone, registrationEmail, registrationPassword, registrationCity, registrationNeighborhood, db);
                    String orderQuantity = loginInfoIntent.getStringExtra("orderQuantity");
                    if (customerID != -1) {
                        sessionManager.openSession(registrationPhone);
                        Toast.makeText(v.getContext(), "Merci de vous enregistrer",
                                Toast.LENGTH_SHORT).show();

                    }
                    if (customerID != -1 && orderQuantity != null && orderQuantity.length() != 0) {
                        String todaysDate = starGasCamerounDBHelper.retrieveTodaysDate();
                        CustomerOrder customerOrder = new CustomerOrder(customerID, todaysDate, orderQuantity);
                        confirmQuantityDialogAndPersist(customerOrder, db, registrationPhone);
                    } else if (orderQuantity == null || orderQuantity.length() == 0) {
                        startActivity(backToFeaturesActivityIntent);
                        finish();
                    }
                }


            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
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

    private void open(String quantity) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage(R.string.decision);
        alertDialogBuilder.setMessage(quantity + " bouteleilles?");
//        alertDialogBuilder.setPositiveButton(R.string.positive_button,
        alertDialogBuilder.setPositiveButton("Oui",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent positveActivity = new Intent(getApplicationContext(), FeaturesActivity.class);
                        startActivity(positveActivity);
                        finish();

                    }
                });
//        alertDialogBuilder.setNegativeButton(R.string.negative_button,
        alertDialogBuilder.setNegativeButton("Non",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent negativeActivity = new Intent(getApplicationContext(), FeaturesActivity.class);
                        startActivity(negativeActivity);
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void confirmQuantityDialogAndPersist(final CustomerOrder customerOrder, SQLiteDatabase db, String phone) {
        final StarGasCamerounDBHelper starGasCamerounDBHelper2 = starGasCamerounDBHelper;
        final CustomerOrder customerOrder2 = customerOrder;
        final SQLiteDatabase db2 = db;
        final SessionManager sessionManager = new SessionManager(getApplicationContext());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final String registrationPhone2 = phone;
        char openBracket = '(';
        char closeBracket = ')';
        String bottles = " bouteille" + openBracket + "s" + closeBracket + "?";
        alertDialogBuilder.setMessage(customerOrder.getOrderQuantity() + bottles);
       alertDialogBuilder.setPositiveButton("Oui",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent positveActivity = new Intent(getApplicationContext(), FeaturesActivity.class);
                        starGasCamerounDBHelper.addOrder(customerOrder2, db2);
                        forwardOrderPerSMS(customerOrder.getOrderQuantity());
                        startActivity(positveActivity);
                        finish();

                    }
                });
        alertDialogBuilder.setNegativeButton("Non",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent negativeActivity = new Intent(getApplicationContext(), FeaturesActivity.class);
                        sessionManager.openSession(registrationPhone2);
                        startActivity(negativeActivity);
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

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

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (email == null || email.length() == 0) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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

    private boolean confirmPassWord(String passWord, String confirmPassword) {
        return passWord != null && passWord.length() > 0 && passWord.equals(confirmPassword);
    }


}
