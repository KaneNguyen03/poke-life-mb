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
import prm392.project.model.Ingredient;
import prm392.project.model.OrderDetail;

public class IngredientAdapter extends BaseAdapter {
    private Context context;
    private List<Ingredient> ingredientList;

    public IngredientAdapter(Context context, List<Ingredient> ingredientList) {
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
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.custom_food_card, viewGroup, false);
        }
        Ingredient cur = ingredientList.get(i);

        ImageView imageView = view.findViewById(R.id.foodImage);
        TextView nameView = view.findViewById(R.id.foodName);
        TextView priceView = view.findViewById(R.id.foodPrice);
        TextView ingredientView = view.findViewById(R.id.description);
        TextView calorieView = view.findViewById(R.id.foodCalorie);
        ImageButton btnOrder = view.findViewById(R.id.btnOrder);

        DecimalFormat formatter = new DecimalFormat("#,###"); // Định dạng số với dấu phẩy
        String formattedPrice = formatter.format(cur.getPrice()) + " VNĐ"; // Thêm đơn vị VNĐ

        priceView.setText(formattedPrice);
        Glide.with(context)
                .load(cur.getImageUrl())
                .placeholder(R.drawable.salah)
                .into(imageView);
        nameView.setText(cur.getName());
        priceView.setText(formattedPrice);
        ingredientView.setText(cur.getDescription());
        calorieView.setText(String.format("%d cal", cur.getCalories()));

        btnOrder.setOnClickListener(v -> {
            saveFoodToLocalStorage(cur);
        });
        return view;
    }

    private void saveFoodToLocalStorage(Ingredient curIngredient) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("chosen_ingredient", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Ingredient ingredient = new Ingredient(curIngredient.getIngredientId(), curIngredient.getName(), curIngredient.getCalories(),
                curIngredient.getImageUrl(), curIngredient.getPrice(), curIngredient.getDescription(), 1);

        // Chuyển đổi đối tượng Food thành JSON
        Gson gson = new Gson();
        String json = gson.toJson(ingredient);

        // Lưu vào sharedPreferences với id của món ăn làm key
        editor.putString(curIngredient.getIngredientId(), json);
        editor.apply();

        // Thông báo cho người dùng
        Toast.makeText(context,"Add " + curIngredient.getName() + " to chosen ingredients list!", Toast.LENGTH_SHORT).show();
    }
}
