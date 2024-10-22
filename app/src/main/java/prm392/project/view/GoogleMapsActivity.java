package prm392.project.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;

import prm392.project.R;

public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap myMap;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private SearchView svMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_google_maps);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        updateCartCountAtHome();
        svMap = findViewById(R.id.svMap);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        svMap.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = svMap.getQuery().toString();
                List<Address> addressList = null;
                if (location != null) {
                    Geocoder geocoder = new Geocoder(GoogleMapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    myMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        mapFragment.getMapAsync(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_location);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Intent intent = new Intent(GoogleMapsActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_cart) {
                    Intent intent = new Intent(GoogleMapsActivity.this, CartListActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_profile) {
                    Intent intent = new Intent(GoogleMapsActivity.this, ProfileActivity.class);
                    //chưa có trang profile
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_location) {
                    Intent intent = new Intent(GoogleMapsActivity.this, GoogleMapsActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        // 10.875305406119857, 106.80072268190438: Nhà văn hóa Sinh viên TP.HCM
        LatLng NVHSV = new LatLng(10.875305406119857, 106.80072268190438);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NVHSV, 15));

        MarkerOptions options = new MarkerOptions().position(NVHSV).title("Nhà văn hóa Sinh viên TP.HCM");
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        myMap.addMarker(options);

        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);
        myMap.getUiSettings().setZoomGesturesEnabled(false);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(GoogleMapsActivity.this);
                } else {
                    Toast.makeText(GoogleMapsActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission is denied, please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_google_map_type, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.mapNormal){
            myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        if(id == R.id.mapSattelite){
            myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        if(id == R.id.mapTerrain){
            myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        if(id == R.id.mapHybird){
            myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        return super.onOptionsItemSelected(item);
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