package prm392.project.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import prm392.project.model.OrderDetail;

public class CartListActivity extends AppCompatActivity {

    private GridView gridView;
    private CartAdapter cartAdapter;
    private List<OrderDetail> cartList;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageButton btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnOrder = findViewById(R.id.btnOrder);
        gridView = findViewById(R.id.orderDetailListView);
        cartList = new ArrayList<>();

        btnOrder.setOnClickListener(v -> {

            Intent intent = new Intent(CartListActivity.this, OrderActivity.class);
            startActivity(intent);
        });
        // Khởi tạo SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this::loadCartData); // Đặt sự kiện khi kéo để làm mới
        cartAdapter = new CartAdapter(this, cartList);
        gridView.setAdapter(cartAdapter);

        loadCartData(); // Tải dữ liệu từ SharedPreferences


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_cart);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Intent intent = new Intent(CartListActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_cart) {
                    Intent intent = new Intent(CartListActivity.this, CartListActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_profile) {
                    Intent intent = new Intent(CartListActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_location) {
                    Intent intent = new Intent(CartListActivity.this, GoogleMapsActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

    private void loadCartData() {
        // Clear the old cart list
        cartList.clear();
        swipeRefreshLayout.setRefreshing(true); // Start refreshing UI

        // Load data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);
        Gson gson = new Gson();

        for (String key : sharedPreferences.getAll().keySet()) {
            String json = sharedPreferences.getString(key, "");
            Log.d("CartListActivity", "Loaded JSON from SharedPreferences: " + json);
            OrderDetail orderDetail = gson.fromJson(json, OrderDetail.class);
            if (orderDetail != null) {
                cartList.add(orderDetail);
            }
        }

        // Check if data was successfully loaded
        if (cartList.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
        } else {
            // Data has loaded successfully, now set the adapter
            if (cartAdapter == null) {
                cartAdapter = new CartAdapter(this, cartList);
                gridView.setAdapter(cartAdapter); // Set adapter after data is ready
            } else {
                cartAdapter.notifyDataSetChanged(); // Notify the adapter about new data
            }
        }

        swipeRefreshLayout.setRefreshing(false); // Stop refreshing UI
    }
}
