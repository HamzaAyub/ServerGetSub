package pk.getsub.www.servergetsub.orderfrontpageactivity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by hp on 1/5/2018.
 */

public interface OrderService {
    @POST("userorder")
    Call<OrderPojo> userOrder(@Body OrderPojo o);
}
