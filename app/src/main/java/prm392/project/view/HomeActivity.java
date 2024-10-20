package prm392.project.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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

import prm392.project.R;
import prm392.project.adapter.FoodAdapter;
import prm392.project.model.Food;

public class HomeActivity extends AppCompatActivity {

    GridView gridView;
    FoodAdapter foodAdapter;
    ArrayList<Food> foodList;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(HomeActivity.this, v);
            popupMenu.getMenuInflater().inflate(R.menu.option_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.logout) {
                        Toast.makeText(HomeActivity.this, "Click logout", Toast.LENGTH_SHORT).show();
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
        gridView = findViewById(R.id.foodListView);
        foodList = new ArrayList<>();
        loadFoodData();

        foodAdapter = new FoodAdapter(this, foodList);
        gridView.setAdapter(foodAdapter);

        // Handle pull-to-refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshFoodData();  // Your method to refresh data
            swipeRefreshLayout.setRefreshing(false);  // Stop the refresh animation
        });
//        foodAdapter = new FoodAdapter(this, foodList);
//        gridView.setAdapter(foodAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Toast.makeText(HomeActivity.this, "Home", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.nav_cart) {
                    Toast.makeText(HomeActivity.this, "cart", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.nav_profile) {
                    Toast.makeText(HomeActivity.this, "profile", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.nav_location) {
                    Toast.makeText(HomeActivity.this, "location", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    // Method to load the initial data
    private void loadFoodData() {
        foodList.add(new Food("Salad", R.drawable.salah, "Ingredients: Lettuce, Tomato, Cucumber", 10.00, 100));
        foodList.add(new Food("Salad", R.drawable.salah, "Ingredients: Lettuce, Tomato, Cucumber", 10.00, 100));
        foodList.add(new Food("Salad", R.drawable.salah, "Ingredients: Lettuce, Tomato, Cucumber", 10.00, 100));
        foodList.add(new Food("Salad", R.drawable.salah, "Ingredients: Lettuce, Tomato, Cucumber", 10.00, 100));
        foodList.add(new Food("Salad", R.drawable.salah, "Ingredients: Lettuce, Tomato, Cucumber", 10.00, 100));
        foodList.add(new Food("Salad", R.drawable.salah, "Ingredients: Lettuce, Tomato, Cucumber", 10.00, 100));
        foodList.add(new Food("Salad", R.drawable.salah, "Ingredients: Lettuce, Tomato, Cucumber", 10.00, 100));
        foodList.add(new Food("Salad", R.drawable.salah, "Ingredients: Lettuce, Tomato, Cucumber", 10.00, 100));
        // Add more food items...
    }

    // Method to refresh data
    private void refreshFoodData() {
        foodList.clear();  // Clear the existing list
        loadFoodData();    // Reload the data
        foodAdapter.notifyDataSetChanged();  // Notify adapter of the data change
    }
}