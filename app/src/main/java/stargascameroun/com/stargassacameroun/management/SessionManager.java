package stargascameroun.com.stargassacameroun.management;

/**
 * Created by 623990 on 3/17/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 623990 on 3/10/2015.
 */
public class SessionManager {
    private Context applicationContect;

    public SessionManager(Context applicationContect) {
        this.applicationContect = applicationContect;
    }

    public void openSession(String customerPhone) {
        SharedPreferences sharedPreferences = applicationContect.getSharedPreferences("customerTelephoneFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("customerTelephone", customerPhone);
        editor.commit();
    }

    public void closeSession() {
        SharedPreferences sharedPreferences = applicationContect.getSharedPreferences("customerTelephoneFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public boolean sessionOpen() {
        boolean isOpen = false;
        SharedPreferences sharedPreferences = applicationContect.getSharedPreferences("customerTelephoneFile", Context.MODE_PRIVATE);
//        String alreadyExist = sharedPreferences.getString("customerTelephone", customerPhone);
        isOpen = !sharedPreferences.getAll().isEmpty();
        return isOpen;
    }

    public String sessionId(){
        String customerPhone = null;
        SharedPreferences sharedPreferences = applicationContect.getSharedPreferences("customerTelephoneFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        customerPhone = sharedPreferences.getString("customerTelephone",null);
        return customerPhone;
    }
}
