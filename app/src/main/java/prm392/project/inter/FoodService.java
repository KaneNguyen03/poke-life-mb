package prm392.project.inter;

import java.util.List;

import prm392.project.model.CustomDishDTO;
import prm392.project.model.Food;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FoodService {

    //ở đây phải xài api food/customer mới đúng
    @GET("food")
    Call<List<Food>> getFoodList(
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize,
            @Query("keyword") String keyword
    );

    @GET("food/{id}")
    Call<Food> getFoodDetails(@Path("id") String foodId);

    @POST("food/customDish")
    Call<String> createCustomDish(List<CustomDishDTO> customDish);
}
