package prm392.project.model;

public class Ingredient {
    private String ingredientId;
    private String name;
    private int calories;
    private String imageUrl;
    private double price;
    private String description;
    private int quantity;

    public Ingredient(String ingredientId, String name, int calories, String imageUrl, double price, String description, int quantity) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.calories = calories;
        this.imageUrl = imageUrl;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
    }

    public Ingredient(String ingredientId, int quantity) {
        this.ingredientId = ingredientId;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(String ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
