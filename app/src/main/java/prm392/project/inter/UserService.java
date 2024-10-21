package prm392.project.inter;

import prm392.project.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {
    @GET("auth/local/getCurrentUser")
    Call<User> getCurrentUser();

    @POST("auth/logout")
    Call<Boolean> logout();
}
