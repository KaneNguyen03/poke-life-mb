package prm392.project.repo;

import prm392.project.factory.APIClient;
import prm392.project.inter.FoodService;

public class FoodRepository {

    public static FoodService getFoodService() {
        return APIClient.getClient().create(FoodService.class);
    }
}
