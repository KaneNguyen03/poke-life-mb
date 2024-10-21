package prm392.project.repo;

import prm392.project.factory.APIClient;
import prm392.project.inter.UserService;
import prm392.project.model.User;
import retrofit2.Call;
import retrofit2.Retrofit;

public class UserRepository {
    private UserService userService;

    public UserRepository(String token) {
        Retrofit retrofit = APIClient.getClient(token);
        userService = retrofit.create(UserService.class);
    }

    public Call<User> getUserProfile() {
        return userService.getCurrentUser();
    }


}
