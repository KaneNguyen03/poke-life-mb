package prm392.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;

import prm392.project.R;
import prm392.project.model.Food;

public class FoodAdapter extends BaseAdapter {

    private Context context;
    private List<Food> foodList;

    public FoodAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
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
        TextView ingredientView = view.findViewById(R.id.ingredient);
        TextView calorieView = view.findViewById(R.id.foodCalorie);

//        imageView.setImageResource(currentFood.getImage());
        Glide.with(context)
                .load(currentFood.getImage())
                .placeholder(R.drawable.salah)
                .into(imageView);
        nameView.setText(currentFood.getName());
        priceView.setText(currentFood.getPrice());
        ingredientView.setText(currentFood.getDescription());
        calorieView.setText(String.format("%d cal", currentFood.getCalories()));
        return view;
    }
}
