package prm392.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import java.text.DecimalFormat;

import java.util.List;

import prm392.project.R;
import prm392.project.model.Food;
import prm392.project.model.Ingredient;

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
        return view;
    }
}
