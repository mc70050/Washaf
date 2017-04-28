package comp4900.bcit.ca.washaf;

/**
 * Created by Michael on 2017-04-28.
 * This represents the type of user.
 */

public enum UserType {
    /* There is only one top level administrator, which is Sahil */
    TOP_ADMIN,
    /* This is owner of other laundromats */
    ADMIN,
    /* This represents all employees that work inside a laundromat */
    EMPLOYEE,
    /* This represents all customers using the service of laundromats */
    CUSTOMER
}
