package comp4900.bcit.ca.washaf;

/**
 * Created by Michael on 2017-04-28.
 * Represents all potential users of this app. This includes:
 * 1. Top level administrator, which is Sahil
 * 2. Second level administrator, which can be the owner of other laundromats
 * 3. Employee, who are the employees that works in a laundromat
 * 4. Customer, who uses the services of the laundromats
 */

public class User {

    private String  firstName;
    private String  lastName;
    private String address;
    private String  email;
    private String  phoneNum;
    private long type;

    /**
     * Default constructor
     */
    public User() {
        // No implementation.
    }

    /**
     * Constructor which takes in inputs for all parameters.
     * No validation is done here, it is expected to be done before constructor
     * is called.
     * @param firstName first name
     * @param lastName last name
     * @param address address in the form of: 1111 example street
     * @param email email address
     * @param phoneNum phone number
     * @param type type of user, see class UserType for more detail
     */
    public User(String firstName, String lastName, Address address, String email,
                String phoneNum, UserType type) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setAddress(address);
        setPhoneNum(phoneNum);
        setType(type);
    }

    // Start of all getters and setters

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address.toString();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public long getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type.ordinal();
    }

    // End of all setters and getters

    // Four functions for checking user type
    public boolean isTopAdmin() {
        return (getType() == UserType.TOP_ADMIN.ordinal()) ? true : false;
    }

    public boolean isAdmin() {
        return (getType() == UserType.ADMIN.ordinal()) ? true : false;
    }

    public boolean isEmployee() {
        return (getType() == UserType.EMPLOYEE.ordinal()) ? true : false;
    }

    public boolean isCustomer() {
        return (getType() == UserType.CUSTOMER.ordinal()) ? true : false;
    }
    // End of functions for checking user type
}
