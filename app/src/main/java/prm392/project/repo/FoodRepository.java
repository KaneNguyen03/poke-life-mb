package prm392.project.repo;

import android.content.Context;

import java.util.List;

import prm392.project.factory.APIClient;
import prm392.project.inter.FoodService;
import prm392.project.model.Food;
import retrofit2.Call;
import retrofit2.Retrofit;


public class    FoodRepository {

    public static FoodService getFoodService(Context context) {
        return APIClient.getClient(context).create(FoodService.class);
    }

    private FoodService foodService;

    public FoodRepository(Context context) {
        Retrofit retrofit = APIClient.getClient(context);
        foodService = retrofit.create(FoodService.class);
    }

    public Call<Food> getFoodDetails(String foodId) {
        return foodService.getFoodDetails(foodId);
    }

}