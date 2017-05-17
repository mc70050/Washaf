package comp4900.bcit.ca.washaf;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Michael on 2017-05-09.
 * Represents all orders. Name was given at beginning of project.
 */
public class CurrentOrder implements Serializable {
    private String customerName;
    private String address;
    private String phone;
    private String email;
    private String serviceType;
    private String requestedTime;
    private String pickup_type;
    private String pickup_day;
    private String pickup_time;
    private String delivery_type;
    private String delivery_day;
    private String delivery_time;
    private String delivery_address;
    private long   quantity;
    private long   price;
    private OrderStatus status;
    private String customer_id;

    private static final long WASHAF_PRICE = 35;
    private static final long BAG_PRICE = 10;

    /**
     * Default constructor.
     */
    public CurrentOrder() {}

    /**
     * Constructor for orders that do not require delivery service.
     * @param name of customer
     * @param address of customer
     * @param phone of customer
     * @param email of customer
     * @param serviceType type of service
     * @param requestedTime for the order
     * @param quantity number of bags for the order
     * @param pickup_type either pick up or drop off
     * @param pickup_day day requested for pick up
     * @param pickup_time time for order to be picked up
     * @param delivery_type delivery or pick up, but must be pick up in this constructor
     * @param delivery_day day for delivery
     * @param delivery_time time frame for dellivery
     */
    public CurrentOrder(String name, String address, String phone, String email, String serviceType, String requestedTime,
                        long quantity, String pickup_type, String pickup_day, String pickup_time, String delivery_type,
                        String delivery_day, String delivery_time) {
        customerName = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.serviceType = serviceType;
        this.requestedTime = requestedTime;
        this.quantity = quantity;
        setPrice(serviceType, quantity);
        setPickup_type(pickup_type);
        setPickup_day(pickup_day);
        setPickup_time(pickup_time);
        setDelivery_type(delivery_type);
        setDelivery_day(delivery_day);
        setDelivery_time(delivery_time);
        setStatus(OrderStatus.REQUESTED);
    }

    public CurrentOrder(String name, String address, String phone, String email, String serviceType, String requestedTime,
                        long quantity, String pickup_type, String pickup_day, String pickup_time, String delivery_type,
                        String delivery_day, String delivery_time, String delivery_address) {
        customerName = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.serviceType = serviceType;
        this.requestedTime = requestedTime;
        this.quantity = quantity;
        setPrice(serviceType, quantity);
        setPickup_type(pickup_type);
        setPickup_day(pickup_day);
        setPickup_time(pickup_time);
        setDelivery_type(delivery_type);
        setDelivery_day(delivery_day);
        setDelivery_time(delivery_time);
        setDelivery_address(delivery_address);
        setStatus(OrderStatus.REQUESTED);
    }

    public CurrentOrder(String name, String address, String phone, String email, String serviceType, String requestedTime,
                        long quantity,String delivery_type) {
        customerName = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.serviceType = serviceType;
        this.requestedTime = requestedTime;
        this.quantity = quantity;
        setDelivery_type(delivery_type);
        setPrice(serviceType, quantity);
        setStatus(OrderStatus.REQUESTED);
    }

    private void setPrice(String serviceType, long quantity) {
        if (serviceType.equalsIgnoreCase("wash and fold")) {
            price = WASHAF_PRICE * quantity;
        } else if (serviceType.equalsIgnoreCase("request for bags")) {
            if (delivery_type.equalsIgnoreCase("pick up")) {
                Log.d("setPrice", "pick up price");
                price = BAG_PRICE * quantity;
            } else {
                Log.d("setPrice", "delivery price");
                price = BAG_PRICE * quantity + 2;
            }
        }
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        Log.d("Phone", phone);
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(String requestedTime) {
        this.requestedTime = requestedTime;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getPickup_type() {
        return pickup_type;
    }

    public void setPickup_type(String service_type) {
        this.pickup_type = service_type;
    }

    public String getPickup_day() {
        return pickup_day;
    }

    public void setPickup_day(String pickup_day) {
        this.pickup_day = pickup_day;
    }

    public String getPickup_time() {
        return pickup_time;
    }

    public void setPickup_time(String pickup_time) {
        this.pickup_time = pickup_time;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getDelivery_day() {
        return delivery_day;
    }

    public void setDelivery_day(String delivery_day) {
        this.delivery_day = delivery_day;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getCustomer_id() {
        return customer_id;
    }
    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }
}
