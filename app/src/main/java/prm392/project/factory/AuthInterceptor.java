package prm392.project.factory;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private String token;

    public AuthInterceptor(String token) {
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Ghi log URL của request
        Log.d("AuthInterceptor", "Request URL: " + originalRequest.url());

        // Lấy token mới nhất
//        String token = getToken();
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxYjI2MTViMS02M2M0LTQ1MjUtOTYxNC1kYmFlMWE4NmU3YzYiLCJlbWFpbCI6Im1pbmhxdWFuMjkxMDIwMDMyMDAzQGdtYWlsLmNvbSIsInJvbGUiOiJDdXN0b21lciIsImlhdCI6MTcyOTUyNjQ4NCwiZXhwIjoxNzI5NTI3Mzg0fQ.YTfxkY7oKxduJNyCiSSCix373uWIdSOpNUkdmavQVd8";

        // Thêm token vào header
        Request newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();

        // Ghi log phản hồi
        Response response = chain.proceed(newRequest);
        Log.d("AuthInterceptor", "Response Code: " + response.code());

        return response;
    }

}
