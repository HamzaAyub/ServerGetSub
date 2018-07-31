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
    private String notiTitle;
    private String notiMessage;
    private String notiTopic;
    private String fcm_token;
    private String flagForTokenAndTopic;
    private String flagGetingUserloginnId;
    private String deliveryManTitle;
    private String deliveryManMessage;
    private String deliveryManId;
    private String deliveryManStatus;

    public UserSharPrefer(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    public String getDeliveryManId() {
        deliveryManId = sharedPreferences.getString("deliveryManId", "firebase");
        return deliveryManId;
    }

    public void setDeliveryManId(String deliveryManId) {
        this.deliveryManId = deliveryManId;
        sharedPreferences.edit().putString("deliveryManId", deliveryManId).apply();
    }

    public String getDeliveryManStatus() {
        deliveryManStatus = sharedPreferences.getString("deliveryManStatus", "defaultDeliveryManStatus");
        return deliveryManStatus;
    }

    public void setDeliveryManStatus(String deliveryManStatus) {
        this.deliveryManStatus = deliveryManStatus;
        sharedPreferences.edit().putString("deliveryManStatus", deliveryManStatus).apply();
    }

    public String getDeliveryManTitle() {
        deliveryManTitle = sharedPreferences.getString("deliveryManTitle", "defaultFlag");
        return deliveryManTitle;
    }

    public void setDeliveryManTitle(String deliveryManTitle) {
        this.deliveryManTitle = deliveryManTitle;
        sharedPreferences.edit().putString("deliveryManTitle", deliveryManTitle).apply();
    }

    public String getDeliveryManMessage() {
        deliveryManMessage = sharedPreferences.getString("deliveryManMessage", "defaultDeliveryMan");
        return deliveryManMessage;
    }

    public void setDeliveryManMessage(String deliveryManMessage) {
        this.deliveryManMessage = deliveryManMessage;
        sharedPreferences.edit().putString("deliveryManMessage", deliveryManMessage).apply();
    }

    public String getFlagGetingUserloginnId() {
        flagGetingUserloginnId = sharedPreferences.getString("flagForGettingUserloginId", "defaultFlag");
        return flagGetingUserloginnId;
    }

    public void setFlagGetingUserloginnId(String flagGetingUserloginnId) {
        this.flagGetingUserloginnId = flagGetingUserloginnId;
        sharedPreferences.edit().putString("flagForGettingUserloginId", flagGetingUserloginnId).apply();
    }

    public String getFlagForTokenAndTopic() {
        flagForTokenAndTopic = sharedPreferences.getString("flagForTokenAndTopic", "defaultFlag");
        return flagForTokenAndTopic;
    }

    public void setFlagForTokenAndTopic(String flagForTokenAndTopic) {
        this.flagForTokenAndTopic = flagForTokenAndTopic;
        sharedPreferences.edit().putString("flagForTokenAndTopic", flagForTokenAndTopic).apply();
    }

    public String getFcm_token() {
        fcm_token = sharedPreferences.getString("fcm_token", "defaultToken");
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
        sharedPreferences.edit().putString("fcm_token", fcm_token).apply();
    }

    public String getNotiTopic() {
        notiTopic = sharedPreferences.getString("notiTopic", "Default");
        return notiTopic;
    }

    public void setNotiTopic(String notiTopic) {
        this.notiTopic = notiTopic;
        sharedPreferences.edit().putString("notiTopic", notiTopic).apply();
    }


    public String getNotiTitle() {
        notiTitle = sharedPreferences.getString("notiTitle", "");
        return notiTitle;
    }

    public void setNotiTitle(String notiTitle) {
        this.notiTitle = notiTitle;
        sharedPreferences.edit().putString("notiTitle", notiTitle).apply();
    }

    public String getNotiMessage() {
        notiMessage = sharedPreferences.getString("notiMessage", "No Notification yet");
        return notiMessage;
    }

    public void setNotiMessage(String notiMessage) {
        this.notiMessage = notiMessage;
        sharedPreferences.edit().putString("notiMessage", notiMessage).apply();
    }


    public String getImgPathInFile() {
        imgPathInFile = sharedPreferences.getString("userImagePathInFile", "mNull");
        return imgPathInFile;
    }

    public void setImgPathInFile(String imgPathInFile) {
        this.imgPathInFile = imgPathInFile;
        sharedPreferences.edit().putString("userImagePathInFile", imgPathInFile);
    }

    public String getUserAddress() {
        userAddress = sharedPreferences.getString("userAddress", "mNull");
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
        sharedPreferences.edit().putString("userAddress", userAddress).apply();
    }

    public String getUserPhone() {
        userPhone = sharedPreferences.getString("userphone", "mNull");
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

    public void clearUser() {
        sharedPreferences.edit().clear().apply();
    }

    public String getName() {
        name = sharedPreferences.getString("userdata", "mNull");
        return name;
    }

    public void setName(String name) {
        this.name = name;
        sharedPreferences.edit().putString("userdata", name).apply();
    }

    public int getUserId() {
        userId = sharedPreferences.getInt("userId", 0);
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
        sharedPreferences.edit().putInt("userId", userId).apply();
    }

}


