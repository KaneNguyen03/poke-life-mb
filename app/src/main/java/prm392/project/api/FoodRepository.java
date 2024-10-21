package prm392.project.api;

public class FoodRepository {

    public static FoodService getFoodService() {
        return APIClient.getClient().create(FoodService.class);
    }
}
