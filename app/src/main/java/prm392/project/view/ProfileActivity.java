package prm392.project.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import prm392.project.R;
import prm392.project.factory.APIClient;
import prm392.project.inter.UserService;
import prm392.project.model.User;
import prm392.project.repo.UserRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private EditText profileName, profileEmail, profileAddress, profilePhone;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        updateCartCountAtHome();
        // Initialize views
        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        profilePhone = findViewById(R.id.profile_phone);
        profileAddress = findViewById(R.id.profile_address);

        userRepository = new UserRepository(this);
        loadUserProfile();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_cart) {
                    Intent intent = new Intent(ProfileActivity.this, CartListActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_profile) {
                    Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_location) {
                    Intent intent = new Intent(ProfileActivity.this, GoogleMapsActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

    private void loadUserProfile() {
        userRepository.getUserProfile().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        User user = response.body();
                        // Set user data to views
                        profileName.setText(user.getUsername());
                        profileEmail.setText(user.getEmail());
                        profileAddress.setText(user.getAddress());
                        profilePhone.setText(user.getPhoneNumber());
                    } else {
                        // Log the response body if it's null
                        Log.e("ProfileActivity", "Response body is null");
                        Toast.makeText(ProfileActivity.this, "No user profile available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Log the HTTP status code and error body
                    Log.e("FoodDetailActivity", "Error: " + response.code() + " - " + response.errorBody());
                    Toast.makeText(ProfileActivity.this, "Failed to load user profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
}