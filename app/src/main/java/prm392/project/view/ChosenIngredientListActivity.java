package prm392.project.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.List;

import prm392.project.R;
import prm392.project.adapter.CartAdapter;
import prm392.project.adapter.ChosenIngredientListAdapter;
import prm392.project.model.Ingredient;
import prm392.project.model.OrderDetail;

public class ChosenIngredientListActivity extends AppCompatActivity {

    private GridView gridView;
    private ChosenIngredientListAdapter chosenIngredientAdapter;
    private List<Ingredient> ingredientList;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageButton btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chosen_ingredients_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnOrder = findViewById(R.id.btnOrder);
        gridView = findViewById(R.id.chosenIngredientListView);
        ingredientList = new ArrayList<>();

        btnOrder.setOnClickListener(v -> {
            Toast.makeText(this, "Create custom dish success", Toast.LENGTH_SHORT).show();
        });//chưa xong đây call create custom dish xong chuyển qua trang cart hoặc home

        // Khởi tạo SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this::loadChosenListData); // Đặt sự kiện khi kéo để làm mới

        loadChosenListData(); // Tải dữ liệu từ SharedPreferences

        chosenIngredientAdapter = new ChosenIngredientListAdapter(this, ingredientList);
        gridView.setAdapter(chosenIngredientAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Intent intent = new Intent(ChosenIngredientListActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_cart) {
                    Intent intent = new Intent(ChosenIngredientListActivity.this, CartListActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_profile) {
                    Intent intent = new Intent(ChosenIngredientListActivity.this, HomeActivity.class);
                    //thêm trang profile
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_location) {
                    Intent intent = new Intent(ChosenIngredientListActivity.this, GoogleMapsActivity.class);
                    startActivity(intent);
                    finish(); // Finish the current activity so that it is removed from the back stack
                }
                return true;
            }
        });
    }

    private void loadChosenListData() {
        ingredientList.clear(); // Xóa danh sách cũ
        SharedPreferences sharedPreferences = getSharedPreferences("chosen_list", MODE_PRIVATE);
        Gson gson = new Gson();

        for (String key : sharedPreferences.getAll().keySet()) {
            String json = sharedPreferences.getString(key, "");
            Ingredient ingredient = gson.fromJson(json, Ingredient.class);
            if (ingredient != null) {
                ingredientList.add(ingredient);
            }
        }

        // Kiểm tra xem có dữ liệu không
        if (ingredientList.isEmpty()) {
            Toast.makeText(this, "Giỏ nguyên liệu trống!", Toast.LENGTH_SHORT).show();
        } else {
            chosenIngredientAdapter.notifyDataSetChanged(); // Cập nhật adapter nếu có dữ liệu
        }

        swipeRefreshLayout.setRefreshing(false); // Dừng hoạt động làm mới
    }
}