package prm392.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Date;

import prm392.project.R;
import prm392.project.model.Order;

public class OrderAdapter extends BaseAdapter {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
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

        Order currentOrder = orderList.get(i);

        TextView orderIdView = view.findViewById(R.id.orderId);
        TextView createdAtView = view.findViewById(R.id.orderCreatedAt);
        TextView totalPriceView = view.findViewById(R.id.orderTotalPrice);
        TextView orderStatusView = view.findViewById(R.id.orderStatus);

        // Set the data into TextViews
        orderIdView.setText("Order ID: " + currentOrder.getOrderID());

        // Format createdAt (Assume it's in ISO 8601 format)
        String formattedDate = formatDate(currentOrder.getCreatedAt());
        createdAtView.setText("Created At: " + formattedDate);

        // Format price in VND
        String formattedPrice = formatPriceVND(currentOrder.getTotalPrice());
        totalPriceView.setText("Total Price: " + formattedPrice);

        orderStatusView.setText("Status: " + currentOrder.getOrderStatus());

        return view;
    }

    // Helper method to format price as VND
    private String formatPriceVND(String price) {
        // Assuming price is in String format, convert to a long
        long priceValue = Long.parseLong(price);

        // Use NumberFormat to format the price in VND
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(priceValue);
    }

    // Helper method to format createdAt date
    private String formatDate(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString; // Return the original string in case of parsing error
        }
    }
}
