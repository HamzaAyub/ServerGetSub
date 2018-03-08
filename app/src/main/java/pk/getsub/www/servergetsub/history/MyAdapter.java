package pk.getsub.www.servergetsub.history;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import pk.getsub.www.servergetsub.R;

/**
 * Created by hp on 1/24/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>  {


    private static final String TAG = "HTAG";

    private Context context;
    private String[] data;
    private ArrayList<UserHistory> userData;

    public MyAdapter(Context context , ArrayList<UserHistory> userData){
        this.context = context;
        this.userData = userData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recviewdata, parent , false);

        MyViewHolder vh = new MyViewHolder(view,context,userData);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.firstName.setText(userData.get(position).getOrder());
        holder.lastName.setText(userData.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView firstName;
        private TextView lastName;

        private ArrayList<UserHistory> userData = new ArrayList<UserHistory>();
        private Context ctx;
        public MyViewHolder(View itemView, Context ctx , ArrayList<UserHistory> userHistory ){

            super(itemView);

            this.userData = userHistory;
            this.ctx = ctx;

            itemView.setOnClickListener(this);
            firstName = itemView.findViewById(R.id.txt_first_name);
            lastName = itemView.findViewById(R.id.txt_las_name);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: New Clickkk");

            int position = getAdapterPosition();
            UserHistory history = this.userData.get(position);
            Log.d(TAG, "id: " + history.getUid() );
            Log.d(TAG, "Address: " + history.getAddress());
            Log.d(TAG, "Order: " + history.getOrder());

            Intent intent = new Intent(this.ctx ,OrderHistoryDetailActivity.class );
            intent.putExtra("history_id" , history.getUid());
            intent.putExtra("history_order" , history.getOrder());
            intent.putExtra("history_address" ,history.getAddress());

            this.ctx.startActivity(intent);

        }
    }

}
