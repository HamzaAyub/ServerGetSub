package pk.getsub.www.servergetsub.checkaddress;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pk.getsub.www.servergetsub.R;
import pk.getsub.www.servergetsub.UserSharPrefer;
import pk.getsub.www.servergetsub.history.AppDatabase;
import pk.getsub.www.servergetsub.history.UserHistory;
import pk.getsub.www.servergetsub.map.OrderMapActivity;
import pk.getsub.www.servergetsub.orderfrontpageactivity.OrderPojo;
import pk.getsub.www.servergetsub.orderfrontpageactivity.OrderService;
import pk.getsub.www.servergetsub.splashscreen.SplashScreen;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckAddressActivity extends AppCompatActivity {


    private static final String TAG = "HTAG";
    private EditText editShowAddress;
    private Button btnCheckAddress;
    private TextView txtshow;
    private String myOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_address);


        Toolbar toolbar = findViewById(R.id.toolbar_check_address);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtshow = findViewById(R.id.txt_order_check_address);
        // myOrder = "Default Value";

        editShowAddress = findViewById(R.id.edit_address_show_chek_address);
        btnCheckAddress = findViewById(R.id.btn_address_check_address);

        if (getIntent() != null) {
            myOrder = getIntent().getStringExtra("myOrder");
            //   txtshow.setText(myOrder);
        } else {
            txtshow.setText("Order Not Written");
        }

        final UserSharPrefer user = new UserSharPrefer(this);
        final String userAddress = user.getUserAddress();
        final int userId1 = user.getUserId();
        editShowAddress.setText(userAddress);


/// moving cursor after text in edit field
        editShowAddress.setSelection(editShowAddress.getText().length());


        btnCheckAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //    OrderPojo order = new OrderPojo(1, myOrder , "Second address");






                String myAddress = editShowAddress.getText().toString();
                if(myAddress.equals("")){
                    msgCheckBox("Please Enter Address");
                    return;
                }


                OrderPojo order = new OrderPojo(userId1, myOrder , editShowAddress.getText().toString()); // user id set karni hai
                sendOrder(order);
                //   showMessage("Simple OrderSend");
                startActivity(new Intent(CheckAddressActivity.this, SplashScreen.class));

                //      Log.d(TAG, "onClick: Check Values " + userId1 + "/"+ myOrder + "/"+ editShowAddress.getText().toString());

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendOrder(OrderPojo order) {
        Gson gson = new GsonBuilder().setLenient().create();  // if there is some syntext error in json array
        Retrofit retrofit = new Retrofit.Builder()
               // .baseUrl("http://gminternational.com.pk/mlarafolder/laraserver/public/index.php/api/")  https://www.getsub.pk/mlarafolder/laraserver/public/api/userorder

                .baseUrl("https://www.getsub.pk/mlarafolder/laraserver/public/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        OrderService service = retrofit.create(OrderService.class);
        Call<OrderPojo> client = service.userOrder(order);
        client.enqueue(new Callback<OrderPojo>() {
            @Override
            public void onResponse(Call<OrderPojo> call, Response<OrderPojo> response) {
                Log.d(TAG, "onResponse: Order Send "+ response);

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                                AppDatabase.class, "database-name").build();

                        UserHistory user1 = new UserHistory(  editShowAddress.getText().toString() , myOrder);
                        //  UserHistory user1 = new UserHistory(  "This is my address" , "This is my Orderrr");
                        db.userDao().insertAll(user1);
                    }
                });
                t.start();

                startActivity(new Intent(CheckAddressActivity.this, ConfirmOrder.class));
                finish();
             //   showMessage("Your order is confirm");

          //      startActivity(new Intent(CheckAddressActivity.this, OrderMapActivity.class));

                //   showMessage("Response : Order Send ");
            }

            @Override
            public void onFailure(Call<OrderPojo> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
                showMessage("Some Error Occurs.....");
            }
        });
    }


    public void showMessage(final String msg) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Alert Message")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  Snackbar.make( constraintLayout, msg ,Snackbar.LENGTH_SHORT).show();
                        //   Log.d(TAG, "showMessageBox: " + msg);

                        startActivity(new Intent(CheckAddressActivity.this , OrderMapActivity.class));
                    }
                })
                .show();
    }

    public void msgCheckBox(final String msg) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Alert Message")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  Snackbar.make( constraintLayout, msg ,Snackbar.LENGTH_SHORT).show();
                        //   Log.d(TAG, "showMessageBox: " + msg);
                        return;
                    }
                })
                .show();
    }


}

