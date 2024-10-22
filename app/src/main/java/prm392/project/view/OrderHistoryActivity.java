package prm392.project.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
        orderHistoryList.setAdapter(orderAdapter);

        // Handle pull-to-refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshOrderData();  // Your method to refresh data
            swipeRefreshLayout.setRefreshing(false);  // Stop the refresh animation
        });
    }

    private void loadOrderData() {
        // call API
        orderRepository = new OrderRepository("");
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