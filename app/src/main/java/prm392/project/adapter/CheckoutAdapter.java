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
import prm392.project.model.OrderDetail;

public class CheckoutAdapter extends BaseAdapter {

    private Context context;
    private List<OrderDetail> orderDetail;

    public CheckoutAdapter(Context context, List<OrderDetail> orderDetail) {
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
            view = LayoutInflater.from(context).inflate(R.layout.checkout_item, viewGroup, false);
        }
        OrderDetail currentDetail = orderDetail.get(i);

        ImageView imageView = view.findViewById(R.id.foodImage);
        TextView nameView = view.findViewById(R.id.foodName);
        TextView priceView = view.findViewById(R.id.foodPrice);
        TextView descriptionView = view.findViewById(R.id.description);
        TextView calorieView = view.findViewById(R.id.foodCalorie);
        TextView quantityView = view.findViewById(R.id.foodQuantity);

        DecimalFormat formatter = new DecimalFormat("#,###"); // Định dạng số với dấu phẩy
        String formattedPrice = formatter.format(currentDetail.getPrice()) + " VNĐ"; // Thêm đơn vị VNĐ

        Glide.with(context)
                .load(currentDetail.getImage())
                .placeholder(R.drawable.salah)
                .into(imageView);

        nameView.setText(currentDetail.getName());
        priceView.setText(formattedPrice);
        quantityView.setText(String.valueOf(currentDetail.getQuantity()));
        descriptionView.setText(currentDetail.getDescription());
        calorieView.setText(String.format("%d cal", currentDetail.getCalories())); // Bạn có thể định dạng lại nếu cần
        return view;
    }

}
