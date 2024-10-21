package prm392.project.repo;

import java.util.List;

import prm392.project.factory.APIClient;
import prm392.project.inter.FoodService;
import prm392.project.model.CustomDishDTO;
import prm392.project.model.Food;
import retrofit2.Call;
import retrofit2.Retrofit;


public class FoodRepository {

    public static FoodService getFoodService() {
        return APIClient.getClient("").create(FoodService.class);
    }

    private FoodService foodService;

    public FoodRepository(String token) {
        Retrofit retrofit = APIClient.getClient(token);
        foodService = retrofit.create(FoodService.class);
    }

    public Call<Food> getFoodDetails(String foodId) {
        return foodService.getFoodDetails(foodId);
    }

    public Call<String> createCustomDish(List<CustomDishDTO> customDish){
        return foodService.createCustomDish(customDish);
    }
}
