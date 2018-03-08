package pk.getsub.www.servergetsub.history;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by hp on 1/24/2018.
 */

@Entity
public class UserHistory {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "order")
    private String order;

    @ColumnInfo(name = "address")
    private String address;

   /* public UserHistory(int uid, String order, String address) {
        this.uid = uid;
        this.order = order;
        this.address = address;
    }
*/

    public UserHistory(String order, String address) {
        this.uid = uid;
        this.order = order;
        this.address = address;
    }



    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
