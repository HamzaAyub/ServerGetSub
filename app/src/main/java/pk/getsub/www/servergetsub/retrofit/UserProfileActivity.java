package pk.getsub.www.servergetsub.retrofit;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import pk.getsub.www.servergetsub.R;
import pk.getsub.www.servergetsub.UserSharPrefer;
import pk.getsub.www.servergetsub.checkinternet.ConnectionDetector;
import pk.getsub.www.servergetsub.map.OrderMapActivity;
import pk.getsub.www.servergetsub.splashscreen.SplashScreen;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = "HTAG";
    private Button btnNextUserProfile;
    private EditText editNameUserProfile;
    private EditText editAddressUserProfile;
    private ImageView imgProfile;
    private ConstraintLayout constraintLayout;
    private static final int IMG_CODE = 100;
    private Bitmap bitmap = null;  // necessary cz it use in 2 places onActivityResult() and imgToString()
    private String phoneNumber;
    private SharedPreferences spObj;
    private static Uri selectedImageUri;
    private UserSharPrefer storeUser;
    private static int myImgCheckTest = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        storeUser = new UserSharPrefer(UserProfileActivity.this);
        // spObj = getSharedPreferences("myProfileFile" , MODE_PRIVATE);
        phoneNumber = "77";
        imgProfile = (ImageView) findViewById(R.id.img_circle_user_profile);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout_user_profile);
        editNameUserProfile = (EditText) findViewById(R.id.edit_name_user_profile);
        editAddressUserProfile = (EditText) findViewById(R.id.edit_address_user_profile);
        btnNextUserProfile = (Button) findViewById(R.id.btn_next_user_profile);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMG_CODE);*/
                //  myImgCheckTest = 0;
            }
        });
        btnNextUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(new ConnectionDetector(UserProfileActivity.this).CheckConnected())) {
                    showMessage("Check your Internet");
                } else {

                    String number = storeUser.getUserPhone(); // real number from last activity
                    String editName = editNameUserProfile.getText().toString();
                    String editAddress = editAddressUserProfile.getText().toString();
                    String imgphoto = null;
                    if (editName.equals("")) {
                        showMessage("Fill the Name");
                        return;
                    } else if (editAddress.equals("")) {
                        showMessage("Fill the Address");
                        return;
                    } else if (myImgCheckTest == 0) {
                        storeUser.setName(editName);
                        storeUser.setUserAddress(editAddress);
                        storeUser.setUserImage("mNull");
                        storeUser.setUserPhone(number); // phoneNumber

                        UserPojo user = new UserPojo(editName, editAddress, number, "testImage");
                        sendPost(user);
                        startActivity(new Intent(UserProfileActivity.this, SplashScreen.class));
                        return;
                    }
                    imgphoto = imageToString();


                    //     String imgphoto =null;
                    // Store data in sharePreferencnes
                    storeUser.setName(editName);
                    storeUser.setUserAddress(editAddress);
                    storeUser.setUserImage(imgphoto);
                    storeUser.setUserPhone(number); // phoneNumber
                    UserPojo user = new UserPojo(editName, editAddress, number, "testImage");
                    sendPost(user);
                    startActivity(new Intent(UserProfileActivity.this, SplashScreen.class));
                }// connection check else
            }
        });
    }

    public void sendPost(UserPojo user) {

        Gson gson = new GsonBuilder().setLenient().create();  // if there is some syntext error in json array
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.getsub.pk/mlarafolder/laraserver/public/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        LaraService services = retrofit.create(LaraService.class);
        Call<UserPojo> client = services.saveUser(user);
        client.enqueue(new Callback<UserPojo>() {
            @Override
            public void onResponse(Call<UserPojo> call, Response<UserPojo> response) {
                Log.d(TAG, "onResponse:" + response);
                Log.d(TAG, "onResponse: Signup : " + response.message());
                Log.d(TAG, "onResponse:" + response.body().getId());
                int myId = response.body().getId();
                storeUser.setUserId(myId);
                Log.d(TAG, "onResponse: Store User Id: " + storeUser.getUserId());
                startActivity(new Intent(UserProfileActivity.this, OrderMapActivity.class));

            }

            @Override
            public void onFailure(Call<UserPojo> call, Throwable t) {
                Log.d(TAG, "onFailure:" + t);
                Snackbar.make(constraintLayout, "Some Faileure Try Again", Snackbar.LENGTH_SHORT).show();
                startActivity(new Intent(UserProfileActivity.this, UserProfileActivity.class));
                showMessage("Some Connection Error");
            }
        });
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

    private String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] imgByte = null;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        imgByte = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////

// Methods for images

   /* public String passProfileImage(){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver() , selectedImageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
      return saveToInternalStorage(bitmap);
    }*/

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

 /*   private void loadImageFromStorage(String path) {

        try {
            File f = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            // ImageView img=(ImageView)findViewById(R.id.imgPicker);
            //  imgStore.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }*/

    public void showMessage(final String msg) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Alert Message")
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  Snackbar.make( constraintLayout, msg ,Snackbar.LENGTH_SHORT).show();
                        Log.d(TAG, "showMessageBox: " + msg);
                        return;
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();

    }
}


