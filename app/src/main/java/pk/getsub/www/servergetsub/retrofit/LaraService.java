package pk.getsub.www.servergetsub.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by hp on 1/5/2018.
 */

public interface LaraService {
    @POST("userlogin")
    Call<UserPojo> saveUser(@Body UserPojo u);


    @PUT("userlogin/{id}")
    Call<UserPojo> updateUser(@Path("id") int id, @Body UserPojo u);

    @GET("userlogin/{id}")
    Call<UserPojo> getUser(@Path("id") String phone);

}
