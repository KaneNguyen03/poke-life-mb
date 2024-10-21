package prm392.project.model;

import java.util.List;

public class CreateOrderDTO {
    private String customerName;
    private String phoneNumber;
    private String address;
    private String paymentMethod;
    private List<OrderDetail> orderDetailList;
    private String comboId;

    public CreateOrderDTO(String customerName, String phoneNumber, String address, String paymentMethod, List<OrderDetail> orderDetailList, String comboId) {
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.orderDetailList = orderDetailList;
        this.comboId = comboId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public String getComboId() {
        return comboId;
    }

    public void setComboId(String comboId) {
        this.comboId = comboId;
    }
}
