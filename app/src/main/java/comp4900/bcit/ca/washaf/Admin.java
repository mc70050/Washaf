package comp4900.bcit.ca.washaf;

/**
 * Created by Michael on 2017/5/14.
 */

public class Admin extends User {
    public String storeName;

    public Admin() {}

    public Admin(String firstName, String lastName, String address, String email,
                 String phoneNum, long type, String storeName) {
        super();
        setStoreName(storeName);
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreName() { return storeName; }
}
