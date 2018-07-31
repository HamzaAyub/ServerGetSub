package pk.getsub.www.servergetsub.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PojoNotiAndTitle {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userlogin_id")
    @Expose
    private Integer userloginId;
    @SerializedName("topic")
    @Expose
    private String topic;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;

    public PojoNotiAndTitle(Integer userloginId, String topic, String token) {
        this.userloginId = userloginId;
        this.topic = topic;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserloginId() {
        return userloginId;
    }

    public void setUserloginId(Integer userloginId) {
        this.userloginId = userloginId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

}
