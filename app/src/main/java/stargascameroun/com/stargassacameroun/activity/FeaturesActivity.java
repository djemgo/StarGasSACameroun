package stargascameroun.com.stargassacameroun.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import stargascameroun.com.stargassacameroun.R;
import stargascameroun.com.stargassacameroun.db.StarGasCamerounDBHelper;
import stargascameroun.com.stargassacameroun.management.SessionManager;


public class FeaturesActivity extends Activity {
    SessionManager sessionManager;
    StarGasCamerounDBHelper starGasCamerounDBHelper;
    SQLiteDatabase db;
    //    Intent callLoginActivityIntent;
    Intent customerOrdersIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);

        sessionManager = new SessionManager(getApplicationContext());

        starGasCamerounDBHelper = new StarGasCamerounDBHelper(this);

        db = starGasCamerounDBHelper.getWritableDatabase();

        final Intent callLoginActivityIntent = new Intent(this, LoginActivity.class);

        final Intent customerOrdersIntent = new Intent(this, CustomerOrdersActivity.class);


        Button orderButton = (Button) findViewById(R.id.orderButton);
        final Intent orderIntent = new Intent(this, OrderActivity.class);
        orderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                orderIntent.removeExtra("from");
                orderIntent.putExtra("from", "order");
                startActivity(orderIntent);
                finish();
            }
        });

        Button myOrdersButton = (Button) findViewById(R.id.myOrdersButton);
        myOrdersButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (sessionManager.sessionOpen()) {
                    String sessionID = sessionManager.sessionId();
                    customerOrdersIntent.putExtra("customerID", sessionID);
                    startActivity(customerOrdersIntent);
                    finish();
                } else {
                    callLoginActivityIntent.removeExtra("from");
                    callLoginActivityIntent.putExtra("from", "customerOrders");
                    startActivity(callLoginActivityIntent);
                    finish();
                }
            }
        });

        Button myProfileButton = (Button) findViewById(R.id.myAccountButton);
        final Intent updateCustomerProfileIntent = new Intent(this, CustomerProfileActivity.class);
        myProfileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (sessionManager.sessionOpen()) {
                    String sessionID = sessionManager.sessionId();
                    updateCustomerProfileIntent.putExtra("customerPhone", sessionID);
                    startActivity(updateCustomerProfileIntent);
                    finish();
                } else {
                    callLoginActivityIntent.removeExtra("from");
                    callLoginActivityIntent.putExtra("from", "updateCustomerProfile");
                    startActivity(callLoginActivityIntent);
                    finish();
                }
            }
        });

        Button notifyGasAvailabilityButton = (Button) findViewById(R.id.notifyGasAvailabilityButton);
        notifyGasAvailabilityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
//                    List<String> telephoneNumberList = starGasCamerounDBHelper.findCustomerPhoneNumbers(db);
//                    SmsManager smsManager = SmsManager.getDefault();
////                    String telephone = null;
//                    for (String telephone : telephoneNumberList) {
//                        smsManager.sendTextMessage(telephone, null, "C'est avec plaisir que nous vous annoncons le retour des nos stocks. Veuillez placer vos commandes SVP! ", null, null);
//                    }
                    displayNotification();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });

        Button exitButton = (Button) findViewById(R.id.finishButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sessionManager.closeSession();
                exitStarGasApp();
            }
        });

        final ImageButton callOrderImageButton = (ImageButton) findViewById(R.id.callOrderImageButton);
        callOrderImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callAlloGas24();
            }
        });
    }

    @SuppressLint("NewApi")
    protected void displayNotification() {
//        Log.i("Start", "notification");

      /* Invoking the default notification service */
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("New Message");
        mBuilder.setContentText("You've received new message.");
        mBuilder.setTicker("New Message Alert!");
        mBuilder.setSmallIcon(R.drawable.starcinquieme);

      /* Increase notification number every time a new notification arrives */
//        mBuilder.setNumber(++numMessages);

      /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(this, OrderActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(OrderActivity.class);

      /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

//        mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//      /* notificationID allows you to update the notification later on. */
//        mNotificationManager.notify(notificationID, mBuilder.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_features, menu);
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

    protected void callAlloGas24() {
//        Log.i("Make call", "");DIAL_PHONE

        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
//        Intent phoneIntent = new Intent(Intent.DIAL_PHONE);
        phoneIntent.setData(Uri.parse("tel:2487013903"));

        try {
            startActivity(phoneIntent);
            finish();
//            Log.i("Finished making a call...", "");
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this,
                    "Call failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    public void exitStarGasApp() {

        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
