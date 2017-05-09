package comp4900.bcit.ca.washaf;

/**
 * Created by Michael on 2017-05-09.
 */

public class CurrentOrder {
    private String customerName;
    private String address;
    private String phone;
    private String email;
    private String serviceType;
    private String requestedTime;
    private long   quantity;
    private String price;

    private static final long WASHAF_PRICE = 35;

    public CurrentOrder(String name, String address, String phone, String email, String serviceType, String requestedTime,
                        long quantity) {
        customerName = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.serviceType = serviceType;
        this.requestedTime = requestedTime;
        this.quantity = quantity;
        setPrice(serviceType, quantity);
    }

    private void setPrice(String serviceType, long quantity) {
        if (serviceType.equalsIgnoreCase("wash and fold")) {
            price = "$" + WASHAF_PRICE * quantity;
        }
    }

}
