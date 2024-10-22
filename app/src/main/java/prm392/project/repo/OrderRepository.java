package prm392.project.repo;

import android.content.Context;

import java.util.List;

import prm392.project.factory.APIClient;
import prm392.project.inter.FoodService;
import prm392.project.inter.OrderService;
import prm392.project.model.CreateOrderDTO;
import prm392.project.model.Food;
import prm392.project.model.Order;
import retrofit2.Call;
import retrofit2.Retrofit;

public class OrderRepository {
    private OrderService orderService;

    public OrderRepository(Context context) {
        Retrofit retrofit = APIClient.getClient(context);
        orderService = retrofit.create(OrderService.class);
    }

    public Call<List<Order>> getOrders(int pageIndex, int pageSize, String keyword) {
        return orderService.getOrderList(pageIndex, pageSize, keyword);
    }

    public Call<String> createOrder(CreateOrderDTO order) {
        return orderService.createOrder(order);
    }
}
