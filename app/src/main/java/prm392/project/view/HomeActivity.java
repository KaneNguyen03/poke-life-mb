package prm392.project.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import prm392.project.R;
import prm392.project.adapter.FoodAdapter;
import prm392.project.repo.FoodRepository;
import prm392.project.inter.FoodService;

import prm392.project.model.Food;
import prm392.project.repo.UserRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    GridView gridView;
    FoodAdapter foodAdapter;
    ArrayList<Food> foodList;
    SwipeRefreshLayout swipeRefreshLayout;
    FoodService foodService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("HomeActivity", "onCreate: Activity is being created");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            Log.d("HomeActivity", "WindowInsets applied");
            return insets;
        });
        foodList = new ArrayList<>(); // Initialize the foodList
        Log.d("HomeActivity", "Food list initialized");

        foodService = FoodRepository.getFoodService(this); // Initialize foodService
        foodAdapter = new FoodAdapter(this, foodList);
        gridView = findViewById(R.id.foodListView);
        gridView.setAdapter(foodAdapter);

        Log.d("HomeActivity", "Food adapter set for GridView");


        ImageView menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(HomeActivity.this, v);
            popupMenu.getMenuInflater().inflate(R.menu.option_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.logout) {
                        Log.d("HomeActivity", "Logout menu item clicked");

                        // Call logout API
                        UserRepository userRepository = new UserRepository(HomeActivity.this);
                        Log.d("HomeActivity", "Calling logout API...");

                        userRepository.logout().enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                Log.d("HomeActivity", "Logout API response received");

                                if (response.isSuccessful() && response.body() != null) {
                                    Log.d("HomeActivity", "Logout API successful, response: " + response.body());
                                    if (response.body()) {
                                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Log.d("HomeActivity", "Logout API returned false");
                                        Toast.makeText(HomeActivity.this, "Logout failed", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.d("HomeActivity", "Logout API failed, response code: " + response.code());
                                    Toast.makeText(HomeActivity.this, "Logout failed", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                Log.d("HomeActivity", "Logout API call failed: " + t.getMessage());
                                Toast.makeText(HomeActivity.this, "Logout failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (item.getItemId() == R.id.orderHistory) {
                        Intent intent = new Intent(HomeActivity.this, OrderHistoryActivity.class);
                        startActivity(intent);
                    }
                    return false;
                }
            });
            popupMenu.show();
        });

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        loadFoodData();
        Log.d("HomeActivity", "Food data loading started");

        // Handle pull-to-refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            Log.d("HomeActivity", "Pull-to-refresh triggered");
            refreshFoodData();  // Your method to refresh data
            swipeRefreshLayout.setRefreshing(false);  // Stop the refresh animation
            Log.d("HomeActivity", "Pull-to-refresh completed");
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_cart) {
                    Intent intent = new Intent(HomeActivity.this, CartListActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_profile) {
                    Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_location) {
                    Intent intent = new Intent(HomeActivity.this, GoogleMapsActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

    // Method to load the initial data
    private void loadFoodData() {
        Log.d("HomeActivity", "Loading food data...");
        Call<List<Food>> call = foodService.getFoodList(1, 99999, "");
        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                Log.d("HomeActivity", "Response received from food service");
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("HomeActivity", "Food data successfully loaded");
                    foodList.clear();
                    foodList.addAll(response.body());
                    foodAdapter.notifyDataSetChanged();
                } else {
                    Log.d("HomeActivity", "Failed to load food data: " + response.code());
                    Toast.makeText(HomeActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Log.e("HomeActivity", "API error: " + t.getMessage());
                if (t instanceof java.net.SocketTimeoutException) {
                    Toast.makeText(HomeActivity.this, "Request timed out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to refresh data
    private void refreshFoodData() {
        Log.d("HomeActivity", "Refreshing food data...");
        foodList.clear();  // Clear the existing list
        loadFoodData();    // Reload the data
        foodAdapter.notifyDataSetChanged();  // Notify adapter of the data change
        Log.d("HomeActivity", "Food data refreshed");
    }
}