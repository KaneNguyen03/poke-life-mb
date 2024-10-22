package prm392.project.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import prm392.project.R;
import prm392.project.inter.ChatService;
import prm392.project.model.ChatMessage;
import prm392.project.model.User;
import prm392.project.repo.UserRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {
    private Socket socket;
    private LinearLayout messagesContainer;
    private EditText messageInput;
    private UserRepository userRepository;
    private ChatService chatService;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat);

        messagesContainer = findViewById(R.id.messages_container);
        messageInput = findViewById(R.id.message_input);
        Button sendButton = findViewById(R.id.send_button);
        userRepository = new UserRepository(this);
        loadUserProfile();

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://poke-life.onrender.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        chatService = retrofit.create(ChatService.class);

        // Connect to Socket.IO server
        try {
            socket = IO.socket("https://poke-life.onrender.com");
            socket.connect();
            Log.d("ChatActivity", "Connect socket.io successfully");
        } catch (URISyntaxException e) {
            Log.d("ChatActivity", "Connect socket.io failed!");
            e.printStackTrace();
        }

        // Listen for messages
        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(() -> {
                    JSONObject message = (JSONObject) args[0];
                    addMessage("Admin: " + message.optString("text"));
                });
            }
        });

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> {
            finish(); // This will close the current activity and return to the previous one
        });

        // Handle send button click
        sendButton.setOnClickListener(view -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
                messageInput.setText("");
            }
        });
    }

    private void sendMessage(String message) {
        String sender = currentUser.getEmail();
        String userId = "admin";

        ChatMessage chatMessage = new ChatMessage(sender, message, userId);
        chatService.sendMessage(chatMessage).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    addMessage("You: " + message); // Display message in UI
                } else {
                    Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMessage(String message) {
        TextView messageView = new TextView(this);
        messageView.setText(message);
        messagesContainer.addView(messageView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
        socket.off("message");
    }

    private void loadUserProfile() {
        userRepository.getUserProfile().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        User user = response.body();
                        // Set user data to views
                        currentUser = user;
                        // Load chat history after getting the user profile
                        loadChatHistory();
                    } else {
                        Log.e("ChatActivity", "Response body is null");
                        Toast.makeText(ChatActivity.this, "No user profile available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("ChatActivity", "Error: " + response.code() + " - " + response.errorBody());
                    Toast.makeText(ChatActivity.this, "Failed to load user profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadChatHistory() {
        String userId = currentUser.getEmail(); // Assuming the user ID is the email

        chatService.getChatHistory(userId).enqueue(new Callback<List<ChatMessage>>() {
            @Override
            public void onResponse(Call<List<ChatMessage>> call, Response<List<ChatMessage>> response) {
                if (response.isSuccessful()) {
                    List<ChatMessage> chatMessages = response.body();
                    if (chatMessages != null) {
                        for (ChatMessage message : chatMessages) {
                            String displayMessage = message.getSender().equals(userId) ? "You: " : "Admin: ";
                            displayMessage += message.getText();
                            addMessage(displayMessage);
                        }
                    }
                } else {
                    // Log the error details
                    Log.e("ChatActivity", "Error code: " + response.code());
                    Log.e("ChatActivity", "Error message: " + response.message());
                    if (response.errorBody() != null) {
                        try {
                            Log.e("ChatActivity", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(ChatActivity.this, "Failed to load chat history", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatMessage>> call, Throwable t) {
                // Log the failure details
                Log.e("ChatActivity", "API call failed", t);
                Toast.makeText(ChatActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
