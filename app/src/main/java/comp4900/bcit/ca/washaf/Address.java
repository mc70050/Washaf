package comp4900.bcit.ca.washaf;

/**
 * Created by Michael on 2017-04-28.
 * Contains the information an address would have.
 * Including street number, street name and unit number.
 */

public class Address {

    private String streetName;
    private int    streetNum;
    private String unitNum;

    /**
     * Default constructor.
     * Initializes all parameters.
     */
    public Address() {
        streetName = "";
        streetNum = 0;
        unitNum = "";
    }

    /**
     * Constructor which takes in name of street and number of address.
     * Unit number is initialized empty string.
     * @param streetName name of street
     * @param streetNum number of address
     */
    public Address(final String streetName, final int streetNum) {
        setStreetName(streetName);
        setStreetNum(streetNum);
    }

    /**
     * Constructor which takes in three parameters to initialize all parameters.
     * All parameters are validated before they are initialized.
     * @param streetName name of street
     * @param streetNum number of address
     * @param unitNum number of unit
     */
    public Address(final String streetName, final int streetNum, final String unitNum) {
        setStreetName(streetName);
        setStreetNum(streetNum);
        setUnitNum(unitNum);
    }

    /**
     * Validates name of street entered and initializes the parameter.
     * Validation is to check if the name entered has at least three letter.
     * Example format: "Kingsway Ave" or "Lougheed Highway"
     * @param name of street including postfix
     */
    protected void setStreetName(final String name) {
        streetName = (name.length() > 2) ? name : "";
    }

    /**
     * Validates street number and initializes the parameter.
     * Validation is to check and made sure number is not negative.
     * @param number of address
     */
    protected void setStreetNum(final int number) {
        streetNum = (number > 0) ? number : 0;
    }

    /**
     * Initializes the parameter as long as it has at least one letter.
     * @param number of address
     */
    protected void setUnitNum(final String number) {
        unitNum = (number.length() > 0) ? number : "";
    }

    /**
     * Access method for street name.
     * @return String
     */
    protected String getStreetName() {
        return streetName;
    }

    /**
     * Access method for street number.
     * @return integer
     */
    protected int getStreetNum() {
        return streetNum;
    }

    /**
     * Access method for unit number.
     * @return String
     */
    protected String getUnitNum() {
        return unitNum;
    }
}
