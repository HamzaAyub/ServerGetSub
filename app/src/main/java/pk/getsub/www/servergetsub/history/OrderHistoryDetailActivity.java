package pk.getsub.www.servergetsub.history;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import pk.getsub.www.servergetsub.R;

public class OrderHistoryDetailActivity extends AppCompatActivity {
    private static final String TAG = "HTAG";
    private TextView txtId;
    private TextView txtAddress;
    private TextView txtOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_detail);
        Toolbar toolbar = findViewById(R.id.id_toolbar_order_history_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

/*        Log.d(TAG, "id: " + getIntent().getStringExtra("history_id"));
        Log.d(TAG, "Order : " + getIntent().getStringExtra("history_order"));
        Log.d(TAG, "Address : " + getIntent().getStringExtra("history_address"));*/

        //  txtId = (TextView) findViewById(R.id.id_user_order_history);
        txtAddress = findViewById(R.id.id_address_order_history);
        txtOrder = findViewById(R.id.id_order_order_history_detail);
        //    txtId.setText(getIntent().getStringExtra("history_id"));
        txtAddress.setText(getIntent().getStringExtra("history_address"));
        txtOrder.setText(getIntent().getStringExtra("history_order"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

