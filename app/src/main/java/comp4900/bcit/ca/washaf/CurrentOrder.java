package comp4900.bcit.ca.washaf;

import java.io.Serializable;

/**
 * Created by Michael on 2017-05-09.
 */

public class CurrentOrder implements Serializable {
    private String customerName;
    private String address;
    private String phone;
    private String email;
    private String serviceType;
    private String requestedTime;
    private long   quantity;
    private long   price;
    private OrderStatus status;

    private static final long WASHAF_PRICE = 35;
    private static final long BAG_PRICE = 10;

    public CurrentOrder() {

    }
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
        setStatus(OrderStatus.REQUESTED);
    }

    private void setPrice(String serviceType, long quantity) {
        if (serviceType.equalsIgnoreCase("wash and fold")) {
            price = WASHAF_PRICE * quantity;
        } else if (serviceType.equalsIgnoreCase("request for bag")) {
            price = BAG_PRICE * quantity;
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
}
