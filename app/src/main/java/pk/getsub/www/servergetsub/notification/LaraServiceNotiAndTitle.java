package pk.getsub.www.servergetsub.notification;

import pk.getsub.www.servergetsub.retrofit.UserPojo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface LaraServiceNotiAndTitle {
    @POST("user-fcm-info")
    Call<PojoNotiAndTitle> saveUser(@Body PojoNotiAndTitle u);

    @PUT("user-fcm-info/{id}")
    Call<PojoNotiAndTitle> updateUser(@Path("id") int id, @Body PojoNotiAndTitle u);

    @GET("user-fcm-info/{id}")
    Call<PojoNotiAndTitle> getUser(@Path("id") int id);
}
