package prm392.project.repo;

import prm392.project.factory.APIClient;
import prm392.project.inter.FoodService;
import prm392.project.inter.IngredientService;

public class IngredientRepository {

    public static IngredientService getIngredientService() {
        return APIClient.getClient("").create(IngredientService.class);
    }
}
