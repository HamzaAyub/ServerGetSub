package pk.getsub.www.servergetsub.splashscreen;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pk.getsub.www.servergetsub.R;
import pk.getsub.www.servergetsub.UserSharPrefer;
import pk.getsub.www.servergetsub.deliveryman.DeliveryManDisplay;
import pk.getsub.www.servergetsub.map.OrderMapActivity;
import pk.getsub.www.servergetsub.map.UserProfileUpdateActivity;
import pk.getsub.www.servergetsub.notification.LaraServiceNotiAndTitle;
import pk.getsub.www.servergetsub.notification.NotificationDetailActivity;
import pk.getsub.www.servergetsub.notification.PojoNotiAndTitle;
import pk.getsub.www.servergetsub.retrofit.LaraService;
import pk.getsub.www.servergetsub.retrofit.UserPojo;
import pk.getsub.www.servergetsub.retrofit.UserProfileActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainSplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "HTAG";
    private UserSharPrefer storeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_splash_screen);
        storeUser = new UserSharPrefer(MainSplashScreenActivity.this); // initialize store variable of sp
        FirebaseMessaging.getInstance().subscribeToTopic(storeUser.getNotiTopic());
        int count = 0;
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                if (key.equals("title")) {
                    //    txtTitile.setText(getIntent().getExtras().getString(key));
                    storeUser.setNotiTitle(getIntent().getExtras().getString(key));
                    count =count +1;
                } else if (key.equals("message")) {
                    //   txtMessage.setText(getIntent().getExtras().getString(key))
                    storeUser.setNotiMessage(getIntent().getExtras().getString(key));
                    count =count +1;
                } else if (key.equals("deliverman_id")) {
                    storeUser.setDeliveryManId(getIntent().getExtras().getString(key));
                    Log.d(TAG, "onCreate:  Delivery man Message : " + storeUser.getDeliveryManId());
                    startActivity(new Intent(MainSplashScreenActivity.this, DeliveryManDisplay.class));
                    finish();
                    return;
                }
            }
            Log.d(TAG, "onCreate: Activity have some data");
        } else {
            Log.d(TAG, "onCreate: Not have Data");
        }

        if(count == 2){
            Log.d(TAG, "onCreate: Total Count Of Title And MEssage :  " + count);
            startActivity(new Intent(MainSplashScreenActivity.this, NotificationDetailActivity.class));
            finish();
            return;
        }


        if (storeUser.getFlagForTokenAndTopic().equals("true")) {
            Log.d(TAG, "FlagForTokenAndTopic : Token and Topic For available for Server ");
            getUserloginIdFromServer();
//            saveTokenTopic();
            /*if (storeUser.getFlagGetingUserloginnId().equals("true")) {
                Log.d(TAG, "getFlagGetingUserloginnId() :  update recoede ");
                storeUser.setFlagGetingUserloginnId("false");
            } else {
                Log.d(TAG, "Save New Data To Server : ");
            }*/
        } else {
            Log.d(TAG, "FlagForTokenAndTopic : Nothing available for server ");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(2000);
                    }

                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    Intent i = new Intent(MainSplashScreenActivity.this, OrderMapActivity.class);
                    startActivity(i);
                    finish();
                }

            }

        }).start();

    }

    private void saveTokenTopic() {

        Gson gson = new GsonBuilder().setLenient().create();  // if there is some syntext error in json array
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://gminternational.com.pk/mlarafolder/laraserver/public/index.php/api/") https://www.getsub.pk/mlarafolder/laraserver/public/api/userlogin
                .baseUrl("https://www.getsub.pk/mlarafolder/laraserver/public/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        //  .baseUrl("http://192.168.1.4/larabeyfikar/public/api/")
        LaraServiceNotiAndTitle services = retrofit.create(LaraServiceNotiAndTitle.class);
        Call<PojoNotiAndTitle> client = services.saveUser(new PojoNotiAndTitle(storeUser.getUserId(), storeUser.getNotiTopic(), storeUser.getFcm_token()));
        client.enqueue(new Callback<PojoNotiAndTitle>() {
            @Override
            public void onResponse(Call<PojoNotiAndTitle> call, Response<PojoNotiAndTitle> response) {
                storeUser.setFlagForTokenAndTopic("false");
                Log.d(TAG, "onResponse: saveDataInServer :" + response);
                Log.d(TAG, "onResponse: Signup : saveDataInServer :  " + response.message());
                Log.d(TAG, "onResponse: saveDataInServer : " + response.body().getId());

            }

            @Override
            public void onFailure(Call<PojoNotiAndTitle> call, Throwable t) {
                Log.d(TAG, "onFailure:" + t);
                Log.d(TAG, "onFailure: saveOrUploadDataToServer ");
            }
        });
    }

    public void updateTokenTopic() {

        Gson gson = new GsonBuilder().setLenient().create();  // if there is some syntext error in json array
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.getsub.pk/mlarafolder/laraserver/public/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        LaraServiceNotiAndTitle services = retrofit.create(LaraServiceNotiAndTitle.class);
        Call<PojoNotiAndTitle> client = services.updateUser(storeUser.getUserId(), new PojoNotiAndTitle(storeUser.getUserId(), storeUser.getNotiTopic(), storeUser.getFcm_token()));
        client.enqueue(new Callback<PojoNotiAndTitle>() {
            @Override
            public void onResponse(Call<PojoNotiAndTitle> call, Response<PojoNotiAndTitle> response) {
                Log.d(TAG, "onResponse: updateTokenTopic " + response);
                Log.d(TAG, "onResponse: updateTokenTopic " + response.message());
            }

            @Override
            public void onFailure(Call<PojoNotiAndTitle> call, Throwable t) {
                Log.d(TAG, "onFailure: update error in main splash screnen ");
                Log.d(TAG, "onFailure:" + t);
            }
        });
    }

    public void getUserloginIdFromServer() {
        Gson gson = new GsonBuilder().setLenient().create();  // if there is some syntext error in json array
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.getsub.pk/mlarafolder/laraserver/public/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        LaraServiceNotiAndTitle services = retrofit.create(LaraServiceNotiAndTitle.class);
        Call<PojoNotiAndTitle> client = services.getUser(storeUser.getUserId());
        client.enqueue(new Callback<PojoNotiAndTitle>() {
            @Override
            public void onResponse(Call<PojoNotiAndTitle> call, Response<PojoNotiAndTitle> response) {
                Log.d(TAG, "onResponse: updateTokenTopic " + response);
                Log.d(TAG, "onResponse: updateTokenTopic " + response.message());
                Log.d(TAG, "onResponse:  USerLoginId : " + response.body().getUserloginId());
                if (response.body().getUserloginId().equals(storeUser.getUserId())) {
                    Log.d(TAG, "onResponse: Id Is Match Know we can update ");
                    updateTokenTopic();
                    storeUser.setFlagForTokenAndTopic("false");
                } else {
                    Log.d(TAG, "onResponse: Not Match save new data ");
                }
                //  storeUser.setFlagGetingUserloginnId("true");
            }

            @Override
            public void onFailure(Call<PojoNotiAndTitle> call, Throwable t) {
                saveTokenTopic();
                storeUser.setFlagForTokenAndTopic("false");
                Log.d(TAG, "onFailure: getUserloginIdFromServer() error in main splash screnen ");
                Log.d(TAG, "onFailure:" + t);
            }
        });
    }

}
