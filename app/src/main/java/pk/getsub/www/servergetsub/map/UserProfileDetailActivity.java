package pk.getsub.www.servergetsub.map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import pk.getsub.www.servergetsub.R;
import pk.getsub.www.servergetsub.UserSharPrefer;

public class UserProfileDetailActivity extends AppCompatActivity {

    private static final String TAG = "HTAG";
    private ImageView imgProfile;
    private TextView txtName;
    private TextView txtPhone;
    private TextView txtAddress;
    private Button btnUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_detail);

        imgProfile = findViewById(R.id.img_user_profile_detail);
        txtName = findViewById(R.id.txt_name_user_profile_detail_activity);
        txtPhone = findViewById(R.id.txt_phone_user_profile_detail_activity);
        txtAddress = findViewById(R.id.txt_address_user_profile_activity);
        btnUpdate = findViewById(R.id.btn_update_user_profile_detail_activity);

        UserSharPrefer userstore = new UserSharPrefer(this);
        txtName.setText(userstore.getName());
        txtPhone.setText(userstore.getUserPhone());
        txtAddress.setText(userstore.getUserAddress());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfileDetailActivity.this, UserProfileUpdateActivity.class));
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

}


