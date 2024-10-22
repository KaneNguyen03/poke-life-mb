package prm392.project.inter;

import java.util.List;

import prm392.project.model.ChatMessage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatService {
    @POST("chat/message")
    Call<Void> sendMessage(@Body ChatMessage message);

    @GET("chat/{userId}")
    Call<List<ChatMessage>> getChatHistory(@Path("userId") String userId);
}
