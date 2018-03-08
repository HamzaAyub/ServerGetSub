package pk.getsub.www.servergetsub.map;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import pk.getsub.www.servergetsub.R;
import pk.getsub.www.servergetsub.UserSharPrefer;
import pk.getsub.www.servergetsub.retrofit.LaraService;
import pk.getsub.www.servergetsub.retrofit.UserPojo;
import pk.getsub.www.servergetsub.retrofit.UserProfileActivity;
import pk.getsub.www.servergetsub.splashscreen.SplashScreen;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserProfileUpdateActivity extends AppCompatActivity {

    private static final String TAG = "HTAG";
    private ImageView imgProfile;
    private EditText editName;
    /*private EditText editPhone;*/
    private EditText editAddress;
    private static final int IMG_CODE = 100;
    private static int myImgCheckTest = 0;
    private Bitmap bitmap = null;  // necessary cz it use in 2 places onActivityResult() and imgToString()
    private Button btnUpdate;
    private UserSharPrefer storeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_update);

        Toolbar toolbar = findViewById(R.id.include_user_profile_update_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

       /* EditText et1 = (EditText) findViewById(R.id.edit_name_user_profile_update_activity) ;
        et1.setSelection(et1.getText().length());
        EditText et2 = (EditText) findViewById(R.id.edit_phone_user_profile_update_activity) ;
        et2.setSelection(et2.getText().length());
        EditText et3 = (EditText) findViewById(R.id.edit_address_user_profile_update_activity) ;
        et3.setSelection(et3.getText().length());*/

        imgProfile = findViewById(R.id.img_user_profile_update_detail);
        editName = findViewById(R.id.edit_name_user_profile_update_activity);
        /*editPhone = findViewById(R.id.edit_phone_user_profile_update_activity);*/
        editAddress = findViewById(R.id.edit_address_user_profile_update_activity);




        btnUpdate = findViewById(R.id.btn_update_user_profile_detail_activity);

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMG_CODE);*/
            }
        });

        storeUser = new UserSharPrefer(this);
        editName.setText(storeUser.getName());
        /*editPhone.setText(storeUser.getUserPhone());*/
        editAddress.setText(storeUser.getUserAddress());


/// moving cursor after text in edit field
        editName.setSelection(editName.getText().length());
        /*editPhone.setSelection(editPhone.getText().length());*/
        editAddress.setSelection(editAddress.getText().length());

// Update Button Click

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString();
                /*String phone = editPhone.getText().toString();*/
                String address = editAddress.getText().toString();

                if(name.equals("") /*|| phone.equals("") */|| address.equals("")){
                    showMessage("Fill All Fields");
                    return;
                }

                storeUser.setName(name);
                /*storeUser.setUserPhone(phone);*/
                storeUser.setUserAddress(address);

                //     Toast.makeText(UserProfileUpdateActivity.this, "Compeateeeeee", Toast.LENGTH_SHORT).show();


               /* UserPojo user = new UserPojo(name, address,phone);*/
                UserPojo user = new UserPojo(name, address);
                updateUser(user);
                startActivity(new Intent(UserProfileUpdateActivity.this, SplashScreen.class));


            }
        });


        // cz "myImgPath" is not store in UserProfileAcitivity
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        String ss = sp.getString("myImgPath", "mNull");
        if(ss.equals("mNull")){
            return;
        }else {
            loadImageFromStorage(ss);
        }


    }

    // method for show image from internal storage
    private void loadImageFromStorage(String path) {

        try {
            File f = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imgProfile.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "loadImageFromStorage: " + e.toString());
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_CODE && resultCode == RESULT_OK) {
            Uri mUri = data.getData();

            try {
                myImgCheckTest = 20; // cz image remain not empty while when user press image and not select image (check of next button for image)
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mUri);
// we make new object of  sp not take current activity referene from here,,, NullPointerException  of storeUser Object
                SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
                String mPath = saveToInternalStorage(bitmap);
                sp.edit().putString("myImgPath", mPath).apply();
                imgProfile.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
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
                        Log.d(TAG, "showMessageBox: " + msg);

                        //    OrderMapActivity.super.onBackPressed();
                    }
                })
                .show();
    }


    public void updateUser(UserPojo user) {

        Gson gson = new GsonBuilder().setLenient().create();  // if there is some syntext error in json array
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.getsub.pk/mlarafolder/laraserver/public/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        LaraService services = retrofit.create(LaraService.class);

      /*  Call<UserPojo> client = services.updateUser(storeUser.getUserId() , user);*/

        Call<UserPojo> client = services.updateUser(storeUser.getUserId() , user);

        client.enqueue(new Callback<UserPojo>() {
            @Override
            public void onResponse(Call<UserPojo> call, Response<UserPojo> response) {

           /*     Log.d(TAG, "onResponse:" + response);


                Log.d(TAG, "onResponse: UserProfileUpdateActivity : " + response.message());
                Log.d(TAG, "onResponse:" + response.body().getId());

                int myId = response.body().getId();
                storeUser.setUserId(myId);
                Log.d(TAG, "onResponse: Store User Id: " + storeUser.getUserId());



                Log.d(TAG, "onResponse: UserProfileUpdateActivity : " + response.message());*/

                //    Toast.makeText(UserProfileUpdateActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();

              //  showMessage("Proile is Updated");
                startActivity(new Intent(UserProfileUpdateActivity.this, OrderMapActivity.class));
             //   updateUserMessage("Your Profile is Updated");





            }

            @Override
            public void onFailure(Call<UserPojo> call, Throwable t) {
                Log.d(TAG, "onFailure:" + t);
                showMessage("Some Connection Error");
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateUserMessage(final String msg) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Update Message")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  Snackbar.make( constraintLayout, msg ,Snackbar.LENGTH_SHORT).show();
                        Log.d(TAG, "updateUserMessage: " + msg);
                        startActivity(new Intent(UserProfileUpdateActivity.this , OrderMapActivity.class));

                    }
                })
                .show();
    }



}
