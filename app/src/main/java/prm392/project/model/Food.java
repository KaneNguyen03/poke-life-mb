package prm392.project.model;

public class Food {

    private String name;
    private int image;
    private String ingredient;
    private double price;
    private int calories;

    public Food(String name, int image, String ingredient, double price, int calories) {
        this.name = name;
        this.image = image;
        this.ingredient = ingredient;
        this.price = price;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public String getIngredient() {
        return ingredient;
    }

    public double getPrice() {
        return price;
    }

    public int getCalories() {
        return calories;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}
