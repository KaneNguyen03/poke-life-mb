package prm392.project.inter;

import java.util.List;

import prm392.project.model.CreateOrderDTO;
import prm392.project.model.Food;
import prm392.project.model.Order;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderService {
    @GET("order/customerID")
    Call<List<Order>> getOrderList(
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize,
            @Query("keyword") String keyword
    );

    @POST("order")
    Call<String> createOrder(@Body CreateOrderDTO order);
}
