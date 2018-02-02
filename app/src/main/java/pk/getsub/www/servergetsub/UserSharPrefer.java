package pk.getsub.www.servergetsub;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hp on 1/5/2018.
 */

public class UserSharPrefer {
    private Context context;
    private SharedPreferences sharedPreferences;
    private String name;
    private String userPhone;
    private int userId;
    private String userImage;
    private String userAddress;
    private String imgPathInFile;



    public UserSharPrefer(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

    }

    public String getImgPathInFile() {
        imgPathInFile = sharedPreferences.getString("userImagePathInFile","mNull");
        return imgPathInFile;
    }

    public void setImgPathInFile(String imgPathInFile) {
        this.imgPathInFile = imgPathInFile;
        sharedPreferences.edit().putString("userImagePathInFile" ,imgPathInFile);
    }

    public String getUserAddress() {
        userAddress = sharedPreferences.getString("userAddress" , "mNull");
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
        sharedPreferences.edit().putString("userAddress" , userAddress).apply();
    }

    public String getUserPhone() {
        userPhone = sharedPreferences.getString("userphone","mNull");
        return userPhone;
    }

    public String getUserImage() {
        userImage = sharedPreferences.getString("userImage", "mNull");
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
        sharedPreferences.edit().putString("userImage", userImage).apply();

    }

    public void setUserPhone(String phone) {
        this.userPhone = phone;
        sharedPreferences.edit().putString("userphone", phone).apply();
    }

    public void clearUser(){
        sharedPreferences.edit().clear().apply();
    }

    public String getName() {
        name = sharedPreferences.getString("userdata","mNull");
        return name;
    }

    public void setName(String name) {
        this.name = name;
        sharedPreferences.edit().putString("userdata", name).apply();
    }

    public int getUserId() {
        userId= sharedPreferences.getInt("userId",0 );
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
        sharedPreferences.edit().putInt("userId" , userId).apply();
    }

}


