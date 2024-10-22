package prm392.project.repo;

import android.content.Context;
import prm392.project.factory.APIClient;
import prm392.project.inter.AuthService;
import prm392.project.model.SignIn;
import prm392.project.model.ResponseTokenDTO;
import prm392.project.model.SignUp;
import retrofit2.Call;
import retrofit2.Retrofit;

public class AuthRepository {
    private AuthService authService;

    public AuthRepository(Context context) {
        Retrofit retrofit = APIClient.getClient(context);
        authService = retrofit.create(AuthService.class);
    }

    public Call<ResponseTokenDTO> signIn(SignIn account) {
        return authService.login(account);
    }

    public Call<ResponseTokenDTO> signUp(SignUp account){
        return authService.signup(account);
    }
}
