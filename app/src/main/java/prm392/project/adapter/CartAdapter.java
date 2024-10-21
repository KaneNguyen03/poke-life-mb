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
import prm392.project.model.Food;
import prm392.project.model.OrderDetail;

public class CartAdapter extends BaseAdapter {

    private Context context;
    private List<OrderDetail> orderDetail;

    public CartAdapter(Context context, List<OrderDetail> orderDetail) {
        this.context = context;
        this.orderDetail = orderDetail;
    }

    @Override
    public int getCount() {
        return orderDetail.size();
    }

    @Override
    public Object getItem(int position) {
        return orderDetail.get(position);
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
        OrderDetail currentDetail = orderDetail.get(i);

        ImageView imageView = view.findViewById(R.id.foodImage);
        TextView nameView = view.findViewById(R.id.foodName);
        TextView priceView = view.findViewById(R.id.foodPrice);
        TextView descriptionView = view.findViewById(R.id.description);
        TextView calorieView = view.findViewById(R.id.foodCalorie);
        TextView quantityView = view.findViewById(R.id.foodQuantity);
        ImageButton btnMinus = view.findViewById(R.id.btnMinusQuantity);
        ImageButton btnAdd = view.findViewById(R.id.btnAddQuantity);

        DecimalFormat formatter = new DecimalFormat("#,###"); // Định dạng số với dấu phẩy
        String formattedPrice = formatter.format(currentDetail.getPrice()) + " VNĐ"; // Thêm đơn vị VNĐ

        Glide.with(context)
                .load(currentDetail.getImage())
                .placeholder(R.drawable.salah)
                .into(imageView);

        nameView.setText(currentDetail.getName());
        priceView.setText(formattedPrice);
        quantityView.setText(currentDetail.getQuantity());
        descriptionView.setText(currentDetail.getDescription());
        calorieView.setText(String.format("%d cal", currentDetail.getCalories())); // Bạn có thể định dạng lại nếu cần

        // Lắng nghe sự kiện nhấn nút cộng
        btnAdd.setOnClickListener(v -> {
            int currentQuantity = currentDetail.getQuantity();
            currentDetail.setQuantity(currentQuantity + 1); // Tăng số lượng
            quantityView.setText(String.valueOf(currentDetail.getQuantity())); // Cập nhật TextView
            saveFoodToLocalStorage(currentDetail); // Lưu vào SharedPreferences
        });

        // Lắng nghe sự kiện nhấn nút trừ
        btnMinus.setOnClickListener(v -> {
            int currentQuantity = currentDetail.getQuantity();
            if (currentQuantity > 1) {
                // Giảm số lượng
                currentDetail.setQuantity(currentQuantity - 1);
                quantityView.setText(String.valueOf(currentDetail.getQuantity())); // Cập nhật TextView
                saveFoodToLocalStorage(currentDetail); // Lưu vào SharedPreferences
            } else {
                // Nếu số lượng bằng 1, xóa item khỏi SharedPreferences và danh sách
                SharedPreferences sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(currentDetail.getFoodId()); // Xóa item
                editor.apply();

                // Xóa item khỏi danh sách và thông báo cho adapter biết
                orderDetail.remove(i); // Xóa item khỏi danh sách
                notifyDataSetChanged(); // Cập nhật giao diện người dùng

                // Thông báo cho người dùng
                Toast.makeText(context, currentDetail.getName() + " removed from cart!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void saveFoodToLocalStorage(OrderDetail currentDetail) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Chuyển đổi đối tượng OrderDetail thành JSON
        Gson gson = new Gson();
        String json = gson.toJson(currentDetail);

        // Lưu vào sharedPreferences với id của món ăn làm key
        editor.putString(currentDetail.getFoodId(), json);
        editor.apply();
    }
}
