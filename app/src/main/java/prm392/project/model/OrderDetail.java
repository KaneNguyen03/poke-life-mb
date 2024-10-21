package prm392.project.model;

public class OrderDetail {
    private String foodId;
    private int quantity;
    private String name;
    private String description;
    private double price;
    private int calories;
    private String image;

    public OrderDetail(String foodId, int quantity, String name, String description, double price, int calories, String image) {
        this.foodId = foodId;
        this.quantity = quantity;
        this.name = name;
        this.description = description;
        this.price = price;
        this.calories = calories;
        this.image = image;
    }

    public OrderDetail(String foodId, int quantity){
        this.foodId = foodId;
        this.quantity = quantity;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
