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

    private String  username;
    private String  password;
    private String  firstName;
    private String  lastName;
    private Address address;
    private String  email;
    private String  phoneNum;
    private UserType type;
}
