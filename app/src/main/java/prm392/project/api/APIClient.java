package prm392.project.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static String baseURL = "https://poke-life.onrender.com/api/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(30, TimeUnit.SECONDS) // Thay đổi thời gian chờ ở đây
                .readTimeout(30, TimeUnit.SECONDS) // Thời gian chờ đọc
                .writeTimeout(30, TimeUnit.SECONDS) // Thời gian chờ ghi
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
