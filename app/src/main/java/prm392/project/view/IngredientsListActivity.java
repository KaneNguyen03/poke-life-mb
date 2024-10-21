package prm392.project.view;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import prm392.project.R;
import prm392.project.adapter.FoodAdapter;
import prm392.project.adapter.IngredientAdapter;
import prm392.project.inter.FoodService;
import prm392.project.inter.IngredientService;
import prm392.project.model.Food;
import prm392.project.model.Ingredient;
import prm392.project.repo.IngredientRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientsListActivity extends AppCompatActivity {

    GridView gridView;
    IngredientAdapter ingredientAdapter;
    ArrayList<Ingredient> ingredientsList;
    SwipeRefreshLayout swipeRefreshLayout;
    IngredientService ingredientService;
    Button btnNormalDish, btnChosen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingredients_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ingredientsList = new ArrayList<>();
        ingredientService = IngredientRepository.getIngredientService();
        ingredientAdapter = new IngredientAdapter(this, ingredientsList);
        gridView = findViewById(R.id.ingredientListView);
        gridView.setAdapter(ingredientAdapter);
        btnNormalDish = findViewById(R.id.btnNormalDish);
        btnChosen = findViewById(R.id.btnViewChosen);

        btnNormalDish.setOnClickListener(v -> {
            Intent intent = new Intent(IngredientsListActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        btnNormalDish.setOnClickListener(v -> {
            Intent intent = new Intent(IngredientsListActivity.this, ChosenIngredientListActivity.class);
            startActivity(intent);
        });

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        loadIngredientData();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshIngredientData();  // Your method to refresh data
            swipeRefreshLayout.setRefreshing(false);  // Stop the refresh animation
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Intent intent = new Intent(IngredientsListActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_cart) {
                    Intent intent = new Intent(IngredientsListActivity.this, CartListActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_profile) {
                    Intent intent = new Intent(IngredientsListActivity.this, HomeActivity.class);
                    //thêm trang profile
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_location) {
                    Intent intent = new Intent(IngredientsListActivity.this, GoogleMapsActivity.class);
                    startActivity(intent);
                    finish(); // Finish the current activity so that it is removed from the back stack
                }
                return true;
            }
        });
    }

    private void loadIngredientData() {
        Log.d("IngredientActivity", "Loading ingredient data...");
        Call<List<Ingredient>> call = ingredientService.getIngredientList();
        call.enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                Log.d("IngredientActivity", "Response received");
                if (response.isSuccessful() && response.body() != null) {
                    ingredientsList.clear();
                    ingredientsList.addAll(response.body());
                    ingredientAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(IngredientsListActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Ingredient>> call, Throwable t) {
                if (t instanceof java.net.SocketTimeoutException) {
                    Toast.makeText(IngredientsListActivity.this, "Request timed out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(IngredientsListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e("API Error", t.getMessage());
            }
        });
    }

    // Method to refresh data
    private void refreshIngredientData() {
        ingredientsList.clear();  // Clear the existing list
        loadIngredientData();    // Reload the data
        ingredientAdapter.notifyDataSetChanged();  // Notify adapter of the data change
    }
}