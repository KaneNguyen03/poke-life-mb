package prm392.project.repo;

import android.content.Context;

import prm392.project.factory.APIClient;
import prm392.project.inter.FoodService;
import prm392.project.inter.IngredientService;

public class IngredientRepository {

    public static IngredientService getIngredientService(Context context) {
        return APIClient.getClient(context).create(IngredientService.class);
    }
}
