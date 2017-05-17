package comp4900.bcit.ca.washaf;

/**
 * Created by Michael Chen on 2017-05-11.
 * Represents all the different possible status of an order.
 */

public enum OrderStatus {
    // A request for order was just sent out and nothing has been done to it by employer side
    REQUESTED,
    // An order that needs to be picked up has been assigned to an employee
    ASSIGNED,
    // An order was picked up by an employee and on its way to the designated store
    PICKED_UP,
    // An order is currently in the store to be serviced
    IN_STORE,
    // An order has been serviced and is ready to be delivered to customer
    READY_FOR_DELIVERY,
    // An order has been picked up by an employee and on its way to customer
    IN_DELIVERY,
    // An order has been fully completed
    COMPLETED,
    // An order has been requested and does not require to be picked up, store-side is waiting for
    // customer to drop off the order
    WAITING_FOR_DROPOFF,
    // An order has been serviced and is waiting to be picked up by customer at store.
    WAITING_FOR_PICKUP
}
