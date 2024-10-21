package prm392.project.adapter;

import android.content.Context;
import android.content.SharedPreferences;
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
import prm392.project.model.Ingredient;
import prm392.project.model.OrderDetail;

public class ChosenIngredientListAdapter extends BaseAdapter {
    private Context context;
    private List<Ingredient> ingredientList;

    public ChosenIngredientListAdapter(Context context, List<Ingredient> ingredientList) {
        this.context = context;
        this.ingredientList = ingredientList;
    }

    @Override
    public int getCount() {
        return ingredientList.size();
    }

    @Override
    public Object getItem(int position) {
        return ingredientList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.cart_item, viewGroup, false);
        }
        Ingredient currentIngredient = ingredientList.get(i);

        ImageView imageView = view.findViewById(R.id.foodImage);
        TextView nameView = view.findViewById(R.id.foodName);
        TextView priceView = view.findViewById(R.id.foodPrice);
        TextView descriptionView = view.findViewById(R.id.description);
        TextView calorieView = view.findViewById(R.id.foodCalorie);
        TextView quantityView = view.findViewById(R.id.foodQuantity);
        ImageButton btnMinus = view.findViewById(R.id.btnMinusQuantity);
        ImageButton btnAdd = view.findViewById(R.id.btnAddQuantity);

        DecimalFormat formatter = new DecimalFormat("#,###"); // Định dạng số với dấu phẩy
        String formattedPrice = formatter.format(currentIngredient.getPrice()) + " VNĐ"; // Thêm đơn vị VNĐ

        Glide.with(context)
                .load(currentIngredient.getImageUrl())
                .placeholder(R.drawable.salah)
                .into(imageView);

        nameView.setText(currentIngredient.getName());
        priceView.setText(formattedPrice);
        quantityView.setText(String.valueOf(currentIngredient.getQuantity()));
        descriptionView.setText(currentIngredient.getDescription());
        calorieView.setText(String.format("%d cal", currentIngredient.getCalories())); // Bạn có thể định dạng lại nếu cần

        // Lắng nghe sự kiện nhấn nút cộng
        btnAdd.setOnClickListener(v -> {
            int currentQuantity = currentIngredient.getQuantity();
            currentIngredient.setQuantity(currentQuantity + 1); // Tăng số lượng
            quantityView.setText(String.valueOf(currentIngredient.getQuantity())); // Cập nhật TextView
            saveFoodToLocalStorage(currentIngredient); // Lưu vào SharedPreferences
        });

        // Lắng nghe sự kiện nhấn nút trừ
        btnMinus.setOnClickListener(v -> {
            int currentQuantity = currentIngredient.getQuantity();
            if (currentQuantity > 1) {
                // Giảm số lượng
                currentIngredient.setQuantity(currentQuantity - 1);
                quantityView.setText(String.valueOf(currentIngredient.getQuantity())); // Cập nhật TextView
                saveFoodToLocalStorage(currentIngredient); // Lưu vào SharedPreferences
            } else {
                // Nếu số lượng bằng 1, xóa item khỏi SharedPreferences và danh sách
                SharedPreferences sharedPreferences = context.getSharedPreferences("chosen_list", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(currentIngredient.getIngredientId()); // Xóa item
                editor.apply();

                // Xóa item khỏi danh sách và thông báo cho adapter biết
                ingredientList.remove(i); // Xóa item khỏi danh sách
                notifyDataSetChanged(); // Cập nhật giao diện người dùng

                // Thông báo cho người dùng
                Toast.makeText(context, currentIngredient.getName() + " removed from chosen ingredients list!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void saveFoodToLocalStorage(Ingredient currentIngredient) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("chosen_list", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Chuyển đổi đối tượng OrderDetail thành JSON
        Gson gson = new Gson();
        String json = gson.toJson(currentIngredient);

        // Lưu vào sharedPreferences với id của món ăn làm key
        editor.putString(currentIngredient.getIngredientId(), json);
        editor.apply();
    }
}
