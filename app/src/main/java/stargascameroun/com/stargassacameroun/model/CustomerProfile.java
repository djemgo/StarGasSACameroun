package stargascameroun.com.stargassacameroun.model;

/**
 * Created by 623990 on 3/17/2015.
 */
public class CustomerProfile {
    private long customerId;
    private String telephone;
    private String lastname;
    private String firstname;
    private String email;
    private String password;
    private String city;
    private String neighborhood;

    public CustomerProfile() {
    }

    public CustomerProfile(long customerId, String telephone, String lastname, String firstname, String email, String password, String city, String neighborhood) {
        this.customerId = customerId;
        this.telephone = telephone;
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.password = password;
        this.city = city;
        this.neighborhood = neighborhood;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }


}
