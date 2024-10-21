package prm392.project.inter;

import java.util.List;

import prm392.project.model.Ingredient;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IngredientService {

    @GET("ingredient")
    Call<List<Ingredient>> getIngredientList();
}
