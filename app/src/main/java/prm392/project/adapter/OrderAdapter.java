package prm392.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import prm392.project.R;
import prm392.project.model.Order;

public class OrderAdapter extends BaseAdapter {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> foodList) {
        this.context = context;
        this.orderList = foodList;
    }


    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.custom_order_history_card, viewGroup, false);
        }
        Order currentFood = orderList.get(i);

        ImageView imageView = view.findViewById(R.id.foodOrderImage);
        TextView nameView = view.findViewById(R.id.historyOrderFoodName);
        TextView priceView = view.findViewById(R.id.orderHistoryFoodPrice);
        TextView ingredientView = view.findViewById(R.id.historyOrderIngredient);
        TextView calorieView = view.findViewById(R.id.orderHistoryFoodCal);
        TextView quantityView = view.findViewById(R.id.orderHistoryQuantity);

        imageView.setImageResource(currentFood.getImage());
        nameView.setText(currentFood.getName());
        priceView.setText(String.format("$%.2f", currentFood.getPrice()));
        ingredientView.setText(currentFood.getIngredient());
        calorieView.setText(String.format("%d cal", currentFood.getCalories()));
        quantityView.setText(String.format("x%d", currentFood.getQuantity()));
        return view;
    }
}
