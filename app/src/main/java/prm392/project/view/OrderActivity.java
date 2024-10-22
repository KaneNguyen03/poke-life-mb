package prm392.project.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import prm392.project.R;
import prm392.project.adapter.CartAdapter; // Use the existing cart adapter
import prm392.project.adapter.CheckoutAdapter;
import prm392.project.model.CreateOrderDTO;
import prm392.project.model.OrderDetail;
import prm392.project.model.OrderDetailsDTO;
import prm392.project.model.User;
import prm392.project.repo.OrderRepository;
import prm392.project.repo.UserRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    private TextView fullName, address, phoneNumber;
    private ListView cartItemListView;
    private TextView totalPrice;
    private RadioButton paymentCOD, paymentQRCode;
    private RadioGroup paymentMethodGroup;
    private Button orderButton;
    private CheckoutAdapter checkoutAdapter;
    private CartAdapter cartAdapter;
    private List<OrderDetail> cartList;
    private ImageView qrCodeImage;
    private UserRepository userRepository;
    private OrderRepository orderRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        userRepository = new UserRepository(this);
        orderRepository = new OrderRepository(this);

        fullName = findViewById(R.id.fullName);
        address = findViewById(R.id.address);
        phoneNumber = findViewById(R.id.phoneNumber);
        cartItemListView = findViewById(R.id.cartItemListView);
        totalPrice = findViewById(R.id.totalPrice);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        paymentCOD = findViewById(R.id.paymentCOD);
        paymentQRCode = findViewById(R.id.paymentQRCode);
        orderButton = findViewById(R.id.orderButton);
        qrCodeImage = findViewById(R.id.qrCodeImage);
        cartList = new ArrayList<>();

        // Load customer information from SharedPreferences
        loadCustomerInfo();

        // Load cart data
        loadCartData();

        // Initialize adapter for cart items
        checkoutAdapter = new CheckoutAdapter(this, cartList);
        cartAdapter = new CartAdapter(this, cartList);
        cartItemListView.setAdapter(checkoutAdapter);

        calculateTotalPrice();

        //Show qrCodeImage
        paymentMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.paymentQRCode) {
                // Show the QR Code ImageView
                qrCodeImage.setVisibility(View.VISIBLE);
            } else {
                // Hide the QR Code ImageView
                qrCodeImage.setVisibility(View.GONE);
            }
        });

        // Handle order button click
        orderButton.setOnClickListener(v -> {
            // Get customer info from the fields
            String customerName = fullName.getText().toString();
            String customerAddress = address.getText().toString();
            String customerPhone = phoneNumber.getText().toString();
            String paymentMethod = getSelectedPaymentMethod();

            // Validate input
            if (customerName.isEmpty() || customerAddress.isEmpty() || customerPhone.isEmpty()) {
                Toast.makeText(OrderActivity.this, "Please fill in all customer information", Toast.LENGTH_SHORT).show();
                return;
            }

            if (paymentMethod == null) {
                Toast.makeText(OrderActivity.this, "Please select a payment method", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a list of OrderDetail with only foodId and quantity
            List<OrderDetailsDTO> minimalOrderDetails = new ArrayList<>();
            for (OrderDetail detail : cartList) {
                minimalOrderDetails.add(new OrderDetailsDTO(detail.getFoodId(), detail.getQuantity()));
            }

            // Create the order DTO
            CreateOrderDTO createOrderDTO = new CreateOrderDTO(customerName, customerPhone, customerAddress, paymentMethod, minimalOrderDetails);
            logRequestData(createOrderDTO);
            // Create the order using OrderRepository
            orderRepository.createOrder(createOrderDTO).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(OrderActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                        clearCart(minimalOrderDetails);
                        Intent intent = new Intent(OrderActivity.this, PaymentSuccessActivity.class);
                        startActivity(intent);
                    } else {
                        Log.e("OrderActivity", "Failed to place order: " + response.code() + " - " + response.message());
                        try {
                            // Log the error body for more details
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                            Log.e("OrderActivity", "Error body: " + errorBody);
                        } catch (Exception e) {
                            Log.e("OrderActivity", "Failed to read error body: " + e.getMessage());
                        }
                        Toast.makeText(OrderActivity.this, "Failed to place order: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(OrderActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(OrderActivity.this, PaymentSuccessActivity.class);
            startActivity(intent);
        });
    }

    private void loadCustomerInfo() {
        userRepository.getUserProfile().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        User user = response.body();
                        // Set user data to views
                        fullName.setText(user.getUsername());
                        address.setText(user.getAddress());
                        phoneNumber.setText(user.getPhoneNumber());
                    } else {
                        // Log the response body if it's null
                        Log.e("ProfileActivity", "Response body is null");
                    }
                } else {
                    // Log the HTTP status code and error body
                    Log.e("FoodDetailActivity", "Error: " + response.code() + " - " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCartData() {
        // Load data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);
        Gson gson = new Gson();

        for (String key : sharedPreferences.getAll().keySet()) {
            String json = sharedPreferences.getString(key, "");
            OrderDetail orderDetail = gson.fromJson(json, OrderDetail.class);
            if (orderDetail != null) {
                cartList.add(orderDetail);
            }
        }
    }

    private void calculateTotalPrice() {
        double total = 0;
        for (OrderDetail orderDetail : cartList) {
            total += orderDetail.getPrice() * orderDetail.getQuantity();
        }

        DecimalFormat formatter = new DecimalFormat("#,###"); // Định dạng số với dấu phẩy
        String formattedPrice = formatter.format(total) + " VNĐ"; // Thêm đơn vị VNĐ
        totalPrice.setText(formattedPrice);
    }

    private String getSelectedPaymentMethod() {
        int selectedId = paymentMethodGroup.getCheckedRadioButtonId();
        if (selectedId == paymentCOD.getId()) {
            return "COD";
        } else if (selectedId == paymentQRCode.getId()) {
            return "QRCODE";
        } else {
            return null;
        }
    }

    private void clearCart(List<OrderDetailsDTO> list) {
        // Clear SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (OrderDetailsDTO detail : list) {
            editor.remove(detail.getFoodID()); // Xóa item
            editor.apply();
        }
        editor.remove("cart");

        // Clear cart list and update adapter
        cartList.clear();
        cartAdapter.notifyDataSetChanged();
        checkoutAdapter.notifyDataSetChanged();

        // Update the total price
        calculateTotalPrice();
    }

    private void logRequestData(CreateOrderDTO order) {
        Log.d("OrderActivity", "Customer Name: " + order.getCustomerName());
        Log.d("OrderActivity", "Address: " + order.getAddress());
        Log.d("OrderActivity", "Phone Number: " + order.getPhoneNumber());
        Log.d("OrderActivity", "Payment Method: " + order.getPaymentMethod());

        for (OrderDetailsDTO detail : order.getOrderDetails()) {
            Log.d("OrderActivity", "Order Detail - Food ID: " + detail.getFoodID() + ", Quantity: " + detail.getQuantity());
        }
    }
}
