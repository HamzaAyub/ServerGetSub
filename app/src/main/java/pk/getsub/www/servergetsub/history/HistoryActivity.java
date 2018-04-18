package pk.getsub.www.servergetsub.history;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pk.getsub.www.servergetsub.R;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "HTAG";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private ArrayList<UserHistory> arrayList;
    private TextView txtMsgHistoryActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = findViewById(R.id.id_toolbar_activity_history);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtMsgHistoryActivity = (TextView) findViewById(R.id.show_txt_activity_history);


        arrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(HistoryActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyAdapter(HistoryActivity.this , arrayList);
        recyclerView.setAdapter(adapter);


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "database-name").build();


               /* UserHistory user1 = new UserHistory(  "282 Qayyum Block, Mustafa Town, Lahore" , "Olive ");
                db.userDao().insertAll(user1);*/




                List<UserHistory> myUser = db.userDao().getAll();

                Log.d(TAG, "run: " + myUser);

                for (int i = 0; i < db.userDao().getAll().size(); i++) {

                    //        UserHistory user = new UserHistory(myUser.get(i).getUid() ,myUser.get(i).getOrder(), myUser.get(i).getAddress() );
                    UserHistory user = new UserHistory(myUser.get(i).getOrder(), myUser.get(i).getAddress() );
                    arrayList.add(user);
                    //   Log.d(TAG, "run: " + myUser.get(i).getFirstName()+" // " + myUser.get(i).getLastName());
                }
                Collections.reverse(arrayList);

          //      Log.d(TAG, "run: " + arrayList.size());

                if (arrayList.size() == 0 ){
                    Log.d(TAG, "run:  you havnt order anything yet, so your history is empty");
                }
                else {
                    Log.d(TAG, "run: u send orderr" + arrayList.size());

                    txtMsgHistoryActivity.setVisibility(View.GONE);
                }


                //  Log.d(TAG, "run: " + myUser.get(1).getFirstName()+" // " + myUser.get(1).getLastName());
/*
                Log.d(TAG, "onCreate: " + db.userDao().getAll());
                Log.d(TAG, "onCreate: " + db.userDao().getAll());

                int []a = {1};

                Log.d(TAG, "onCreate: " + db.userDao().loadAllByIds(a));*/

            }
        });

        t.start();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}

