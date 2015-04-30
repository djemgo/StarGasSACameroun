package stargascameroun.com.stargassacameroun.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import stargascameroun.com.stargassacameroun.R;
import stargascameroun.com.stargassacameroun.db.StarGasCamerounDBHelper;
import stargascameroun.com.stargassacameroun.management.SessionManager;
import stargascameroun.com.stargassacameroun.model.CustomerProfile;


public class CustomerProfileActivity extends Activity {
    SessionManager sessionManager;
    StarGasCamerounDBHelper starGasCamerounDBHelper;
    SQLiteDatabase db;
    String firstName;
    String lastName;
    String email;
    String phone;
    String city;
    String neighborhood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(getApplicationContext());
        starGasCamerounDBHelper = new StarGasCamerounDBHelper(this);
        db = starGasCamerounDBHelper.getWritableDatabase();

        setContentView(R.layout.activity_customer_profile);

        final EditText updateFirstnameEditText = (EditText) findViewById(R.id.updateFirstnameEditText);
        final EditText updateLastnameEditText = (EditText) findViewById(R.id.updateLastnameEditText);
        final EditText updatePhoneEditText = (EditText) findViewById(R.id.updatePhoneEditText);
        final EditText updateEmailEditText = (EditText) findViewById(R.id.updateEmailEditText);
        final EditText updateCityEditText = (EditText) findViewById(R.id.updateCityEditText);
        final EditText updateNeighborhoodEditText = (EditText) findViewById(R.id.updateNeighborhoodEditText);

        final Intent previousIntent = getIntent();
        String customerPhone = previousIntent.getStringExtra("customerPhone");
        final CustomerProfile customerProfile = starGasCamerounDBHelper.findCustomer(customerPhone, db);

        updateFirstnameEditText.setText(customerProfile.getFirstname());
        updateLastnameEditText.setText(customerProfile.getLastname());
        updatePhoneEditText.setText(customerProfile.getTelephone());
        updateEmailEditText.setText(customerProfile.getEmail());
        updateCityEditText.setText(customerProfile.getCity());
        updateNeighborhoodEditText.setText(customerProfile.getNeighborhood());


        Button updateReturnButton = (Button) findViewById(R.id.updateReturnButton);
        final Intent backToFeaturesActivityIntent = new Intent(this, FeaturesActivity.class);
        updateReturnButton.setOnClickListener(new View.OnClickListener()

                                              {
                                                  public void onClick(View v) {

                                                      startActivity(backToFeaturesActivityIntent);
                                                      finish();
                                                  }
                                              }

        );

        Button registerButton = (Button) findViewById(R.id.updateButton);
        registerButton.setOnClickListener(new View.OnClickListener()

                                          {
                                              public void onClick(View v) {
                                                  String updateFirstname = updateFirstnameEditText.getText().toString();
                                                  String registrationLastname = updateLastnameEditText.getText().toString();
                                                  String registrationPhone = updatePhoneEditText.getText().toString();
                                                  String registrationEmail = updateEmailEditText.getText().toString();
                                                  String registrationCity = updateCityEditText.getText().toString();
                                                  String registrationNeighborhood = updateNeighborhoodEditText.getText().toString();
                                                  String telephone = previousIntent.getStringExtra("customerPhone");

                                                  boolean isValidInput = false;
                                                  if (!isValidPhone(registrationPhone)) {
                                                      isValidInput = false;
                                                      updatePhoneEditText.setError("Verifiez Votre Telephone SVP");
                                                      updatePhoneEditText.requestFocus();
                                                  } else {
                                                      isValidInput = true;
                                                  }
                                                  if (!isValidEmail(registrationEmail)) {
                                                      isValidInput = false;
                                                      updateEmailEditText.setError("Verifiez Votre Email SVP");
                                                      updateEmailEditText.requestFocus();
                                                  }
                                                  if (isValidInput) {
                                                      long customerID = customerProfile.getCustomerId();
                                                      starGasCamerounDBHelper.updateCustomer(updateFirstname, registrationLastname, registrationPhone, registrationEmail, registrationCity, registrationNeighborhood, db, customerID + "");
                                                      sessionManager.openSession(registrationPhone);
                                                      startActivity(backToFeaturesActivityIntent);
                                                      finish();
                                                  }


                                              }
                                          }

        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customer_profile, menu);
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
