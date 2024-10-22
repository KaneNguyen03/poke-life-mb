package prm392.project.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

import prm392.project.R;
import prm392.project.inter.OnCartUpdateListener;
import prm392.project.model.Food;
import prm392.project.model.OrderDetail;
import prm392.project.view.FoodDetailActivity;
import prm392.project.view.HomeActivity;

public class FoodAdapter extends BaseAdapter {

    private Context context;
    private List<Food> foodList;
    private OnCartUpdateListener cartUpdateListener;

    public FoodAdapter(Context context, List<Food> foodList, OnCartUpdateListener cartUpdateListener) {
        this.context = context;
        this.foodList = foodList;
        this.cartUpdateListener = cartUpdateListener; // Khởi tạo listener
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.custom_food_card, viewGroup, false);
        }
        Food currentFood = foodList.get(i);

        ImageView imageView = view.findViewById(R.id.foodImage);
        TextView nameView = view.findViewById(R.id.foodName);
        TextView priceView = view.findViewById(R.id.foodPrice);
        TextView descriptionView = view.findViewById(R.id.description);
        TextView calorieView = view.findViewById(R.id.foodCalorie);
        ImageButton addButton = view.findViewById(R.id.btnAddToCart);

        DecimalFormat formatter = new DecimalFormat("#,###"); // Định dạng số với dấu phẩy
        String formattedPrice = formatter.format(currentFood.getPrice()) + " VNĐ"; // Thêm đơn vị VNĐ

//        imageView.setImageResource(currentFood.getImage());
        Glide.with(context)
                .load(currentFood.getImage())
                .placeholder(R.drawable.salah)
                .into(imageView);
        nameView.setText(currentFood.getName());
        priceView.setText(formattedPrice);
        descriptionView.setText(currentFood.getDescription());
        calorieView.setText(String.format("%d cal", currentFood.getCalories()));

        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FoodDetailActivity.class);
            intent.putExtra("food_id", currentFood.getFoodID());
            context.startActivity(intent);
        });


        addButton.setOnClickListener(v -> {
            saveFoodToLocalStorage(currentFood);
        });
        return view;
    }

    private void saveFoodToLocalStorage(Food currentFood) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        OrderDetail orderDetail = new OrderDetail(currentFood.getFoodID(), 1, currentFood.getName(),
                currentFood.getDescription(), currentFood.getPrice(), currentFood.getCalories(), currentFood.getImage());

        Gson gson = new Gson();
        String json = gson.toJson(orderDetail);
        editor.putString(orderDetail.getFoodId(), json);
        editor.apply();

        Log.d("FoodAdapter", "Saved JSON in SharedPreferences: " + json);

        // Cập nhật số lượng giỏ hàng
        if (cartUpdateListener != null) {
            cartUpdateListener.onCartUpdated(getCartItemCount()); // Gọi listener
        }

        Toast.makeText(context,"Add " + currentFood.getName() + " to cart!", Toast.LENGTH_SHORT).show();
    }

    private int getCartItemCount() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        return sharedPreferences.getAll().size(); // Đếm số lượng item trong giỏ
    }
}
