package pk.getsub.www.servergetsub.history;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by hp on 1/24/2018.
 */

@Database(entities = {UserHistory.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}