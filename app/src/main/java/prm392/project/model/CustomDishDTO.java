package prm392.project.model;

import java.util.List;

public class CustomDishDTO {
    private List<Ingredient> listIngredient;
    private String name;
    private String description;
    private String imageURL;

    public CustomDishDTO(List<Ingredient> listIngredient, String name, String description, String imageURL) {
        this.listIngredient = listIngredient;
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
    }

    public List<Ingredient> getListIngredient() {
        return listIngredient;
    }

    public void setListIngredient(List<Ingredient> listIngredient) {
        this.listIngredient = listIngredient;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
