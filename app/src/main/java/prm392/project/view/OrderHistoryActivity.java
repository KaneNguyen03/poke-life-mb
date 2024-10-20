package prm392.project.view;

import android.os.Bundle;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import prm392.project.R;
import prm392.project.adapter.OrderAdapter;
import prm392.project.model.Food;
import prm392.project.model.Order;

public class OrderHistoryActivity extends AppCompatActivity {

    GridView orderHistoryList;
    ArrayList<Order> orderList;
    OrderAdapter orderAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

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
        loadOrderData();
        orderAdapter = new OrderAdapter(this, orderList);
        orderHistoryList.setAdapter(orderAdapter);
        // Handle pull-to-refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshOrderData();  // Your method to refresh data
            swipeRefreshLayout.setRefreshing(false);  // Stop the refresh animation
        });
    }

    private void loadOrderData() {
        orderList.add(new Order("Salad", R.drawable.salah, "Ingredients: Lettuce, Tomato, Cucumber", 10.00, 200, 10));
        orderList.add(new Order("Salad", R.drawable.salah, "Ingredients: Lettuce, Tomato, Cucumber", 10.00, 200, 10));
        orderList.add(new Order("Salad", R.drawable.salah, "Ingredients: Lettuce, Tomato, Cucumber", 10.00, 200, 10));
        orderList.add(new Order("Salad", R.drawable.salah, "Ingredients: Lettuce, Tomato, Cucumber", 10.00, 200, 10));
        // Add more food items...
    }

    // Method to refresh data
    private void refreshOrderData() {
        orderList.clear();  // Clear the existing list
        loadOrderData();    // Reload the data
        orderAdapter.notifyDataSetChanged();  // Notify adapter of the data change
    }
}