package prm392.project.model;

public class Order {

    private String OrderID;
    private String TotalPrice;
    private String OrderStatus;
    private String CreatedAt;


    public Order(String orderID, String totalPrice, String orderStatus, String createdAt) {
        OrderID = orderID;
        TotalPrice = totalPrice;
        OrderStatus = orderStatus;
        CreatedAt = createdAt;
    }

    // Getters and Setters
    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }
}
