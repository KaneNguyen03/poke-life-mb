package prm392.project.factory;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static String baseURL = "https://poke-life.onrender.com/api/";
    private static Retrofit retrofit;

    public static Retrofit getClient(String token) {
        Log.d("APIClient", "Creating Retrofit client with token: " + token);

        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(90, TimeUnit.SECONDS) // Thay đổi thời gian chờ ở đây
                .readTimeout(90, TimeUnit.SECONDS) // Thời gian chờ đọc
                .writeTimeout(90, TimeUnit.SECONDS) // Thời gian chờ ghi
                .addInterceptor(new AuthInterceptor(token))
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
