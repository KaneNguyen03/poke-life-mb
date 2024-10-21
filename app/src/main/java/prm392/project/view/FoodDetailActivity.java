package prm392.project.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import prm392.project.R;
import prm392.project.model.Food;
import prm392.project.repo.FoodRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodDetailActivity extends AppCompatActivity {
    private ImageView foodImage, btnBack;
    private TextView foodName, foodDescription, foodPrice, foodCalories;
    private FoodRepository foodRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_detail);

        // Initialize views
        foodImage = findViewById(R.id.foodImage);
        foodName = findViewById(R.id.foodName);
        foodDescription = findViewById(R.id.foodDescription);
        foodPrice = findViewById(R.id.foodPrice);
        foodCalories = findViewById(R.id.foodCalories);

        String token = "this_is_token";
        foodRepository = new FoodRepository(token);

        btnBack = findViewById(R.id.btnBack);

        String foodId = getIntent().getStringExtra("food_id");

        if (foodId != null && !foodId.isEmpty()) {
            // Call the API using the string foodId
            getFoodDetails(foodId);
        } else {
            Toast.makeText(this, "Invalid food ID", Toast.LENGTH_SHORT).show();
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodDetailActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getFoodDetails(String foodId) {
        if (foodId == null || foodId.isEmpty()) {
            Log.e("FoodDetailActivity", "Invalid food ID");
            Toast.makeText(this, "Invalid food ID", Toast.LENGTH_SHORT).show();
            return;
        }

        foodRepository.getFoodDetails(foodId).enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Food food = response.body();
                        // Set data to views
                        foodName.setText(food.getName());
                        foodDescription.setText(food.getDescription());
                        foodPrice.setText(food.getPrice() + " VND");
                        foodCalories.setText("Calories: " + food.getCalories() + " kcal");

                        // Load image with Glide
                        Glide.with(FoodDetailActivity.this)
                                .load(food.getImage())
                                .into(foodImage);
                    } else {
                        // Log the response body if it's null
                        Log.e("FoodDetailActivity", "Response body is null");
                        Toast.makeText(FoodDetailActivity.this, "No food details available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Log the HTTP status code and error body
                    Log.e("FoodDetailActivity", "Error: " + response.code() + " - " + response.errorBody());
                    Toast.makeText(FoodDetailActivity.this, "Failed to load food details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                Toast.makeText(FoodDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}