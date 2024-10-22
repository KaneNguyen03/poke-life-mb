package prm392.project.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
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
import prm392.project.adapter.OrderAdapter;
import prm392.project.model.Order;
import prm392.project.repo.OrderRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    GridView orderHistoryList;
    ArrayList<Order> orderList;
    OrderAdapter orderAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    OrderRepository orderRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        swipeRefreshLayout = findViewById(R.id.refresh_layout_order_history);
        orderHistoryList = findViewById(R.id.lvOrderHistory);
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, orderList);
        loadOrderData();
        updateCartCountAtHome();
        orderHistoryList.setAdapter(orderAdapter);

        // Handle pull-to-refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshOrderData();  // Your method to refresh data
            swipeRefreshLayout.setRefreshing(false);  // Stop the refresh animation
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Intent intent = new Intent(OrderHistoryActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_cart) {
                    Intent intent = new Intent(OrderHistoryActivity.this, CartListActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_profile) {
                    Intent intent = new Intent(OrderHistoryActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_location) {
                    Intent intent = new Intent(OrderHistoryActivity.this, GoogleMapsActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

    private void updateCartCount(BottomNavigationView bottomNavigationView, int itemCount) {
        MenuItem cartMenuItem = bottomNavigationView.getMenu().findItem(R.id.nav_cart);
        if (cartMenuItem != null) {
            TextView sizeCart = findViewById(R.id.cartSize);
            if (itemCount > 0) {
                sizeCart.setText(String.valueOf(itemCount));
                sizeCart.setVisibility(View.VISIBLE);
                sizeCart.setZ(1f);
                bottomNavigationView.setZ(0f);
            } else {
                sizeCart.setText("0");
                sizeCart.setZ(1f);
                bottomNavigationView.setZ(0f);
            }
        }
    }

    private void updateCartCountAtHome() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        SharedPreferences sharedPreferences = this.getSharedPreferences("cart", Context.MODE_PRIVATE);
        int itemCount = sharedPreferences.getAll().size();
        updateCartCount(bottomNavigationView, itemCount);
    }

    private void loadOrderData() {
        // call API
        orderRepository = new OrderRepository(this);
        Call<List<Order>> call = orderRepository.getOrders(1, 999999, "");
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                Log.d("OrderHistoryActivity", "Response received from food service");
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("OrderHistoryActivity", "Order data successfully loaded");
                    orderList.clear();
                    orderList.addAll(response.body());
                    orderAdapter.notifyDataSetChanged();
                } else {
                    Log.d("OrderHistoryActivity", "Response not successful");
                    Toast.makeText(OrderHistoryActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.e("HomeActivity", "API error: " + t.getMessage());
                if (t instanceof java.net.SocketTimeoutException) {
                    Toast.makeText(OrderHistoryActivity.this, "Request timed out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderHistoryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Add some dummy orders
//        orderList.add(new Order("f9053d8a-c6d5-4272-ba8f-8352b881b705", "88.00", "Pending", "2024-10-21T14:39:16.362Z"));
//        orderList.add(new Order("a1233d8b-c6d5-4272-ba8f-8432c881b600", "45.50", "Completed", "2024-10-15T10:30:12.012Z"));
//        orderList.add(new Order("h8903e6b-d7e4-1112-ba5f-7521d887a903", "72.00", "Cancelled", "2024-09-21T16:45:33.452Z"));
        // Add more orders as needed...
    }

    // Method to refresh data
    private void refreshOrderData() {
        orderList.clear();  // Clear the existing list
        loadOrderData();    // Reload the data
        orderAdapter.notifyDataSetChanged();  // Notify adapter of the data change
    }
}