package prm392.project.model;

public class OrderDetailsDTO {
    private String foodID; // Use foodID instead of foodId
    private int quantity;

    public OrderDetailsDTO(String foodID, int quantity) {
        this.foodID = foodID; // Update the constructor
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID; // Update the setter
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
