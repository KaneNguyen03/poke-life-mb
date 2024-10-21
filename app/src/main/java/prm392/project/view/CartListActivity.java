package prm392.project.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
        gridView = findViewById(R.id.foodListView);
        cartList = new ArrayList<>();

        btnOrder.setOnClickListener(v -> {
            Toast.makeText(this, "Order success", Toast.LENGTH_SHORT).show();
        });
        // Khởi tạo SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this::loadCartData); // Đặt sự kiện khi kéo để làm mới

        loadCartData(); // Tải dữ liệu từ SharedPreferences

        cartAdapter = new CartAdapter(this, cartList);
        gridView.setAdapter(cartAdapter);
    }

    private void loadCartData() {
        cartList.clear(); // Xóa danh sách cũ
        SharedPreferences sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);
        Gson gson = new Gson();

        for (String key : sharedPreferences.getAll().keySet()) {
            String json = sharedPreferences.getString(key, "");
            OrderDetail orderDetail = gson.fromJson(json, OrderDetail.class);
            if (orderDetail != null) {
                cartList.add(orderDetail);
            }
        }

        // Kiểm tra xem có dữ liệu không
        if (cartList.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
        } else {
            cartAdapter.notifyDataSetChanged(); // Cập nhật adapter nếu có dữ liệu
        }

        swipeRefreshLayout.setRefreshing(false); // Dừng hoạt động làm mới
    }
}
