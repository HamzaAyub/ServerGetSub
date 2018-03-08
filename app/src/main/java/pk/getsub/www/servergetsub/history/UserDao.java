package pk.getsub.www.servergetsub.history;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by hp on 1/24/2018.
 */

@Dao
public interface UserDao {
    @Query("SELECT * FROM userhistory")
    List<UserHistory> getAll();

    @Query("SELECT * FROM userhistory WHERE uid IN (:userIds)")
    List<UserHistory> loadAllByIds(int[] userIds);

   /* @Query("SELECT * FROM userhistory WHERE first_name LIKE :first AND "
            + "last_name LIKE :last LIMIT 1")
    UserHistory findByName(String first, String last);*/

    @Insert
    void insertAll(UserHistory... users);

    @Delete
    void delete(UserHistory user);
}
