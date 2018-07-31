package pk.getsub.www.servergetsub.notification;

import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import pk.getsub.www.servergetsub.R;
import pk.getsub.www.servergetsub.UserSharPrefer;

public class NotificationDetailActivity extends AppCompatActivity /*implements AdapterView.OnItemSelectedListener*/ {

    private static final String TAG = "HTAG";
    private TextView txtTitle;
    private TextView txtMessage;
    private UserSharPrefer sp;
    private Spinner spinner;
    private LinearLayout mLinearLayout;
    private boolean isSpinnerTouched = false;
    private TextView txtShowArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        Toolbar toolbar = findViewById(R.id.id_toolbar_notification_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sp = new UserSharPrefer(this);
        txtTitle = findViewById(R.id.txt_title_notification_detail_activity);
        txtMessage = findViewById(R.id.txt_message_notification_detail_activity);
        txtShowArea = findViewById(R.id.id_show_selected_area_notification_detail_activity);
        txtShowArea.setText(sp.getNotiTopic());
        mLinearLayout = findViewById(R.id.id_linear_layout_snackbar_notification_detail);
        txtTitle.setText(sp.getNotiTitle());
        txtMessage.setText(sp.getNotiMessage());
        spinner = findViewById(R.id.spinner);
        // spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isSpinnerTouched = true;
                return false;
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                if (!isSpinnerTouched)
                    return;      // cz 1st time open activity not select bydefault
                // do what you want

                FirebaseMessaging.getInstance().unsubscribeFromTopic(sp.getNotiTopic()); // Unsbscribe from previoud topic

                String item = parent.getItemAtPosition(position).toString();
                sp.setNotiTopic(item);
                sp.setFlagForTokenAndTopic("true");
                txtShowArea.setText(sp.getNotiTopic());
                Log.d(TAG, "onItemSelected:  " + sp.getNotiTopic());
                Snackbar.make(mLinearLayout, "Recieve Notification according to " + item, Snackbar.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
/*

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (!isSpinnerTouched) return;      // cz 1st time open activity not select bydefault
        // do what you want

        String item = parent.getItemAtPosition(position).toString();
        sp.setNotiTopic(item);
        txtShowArea.setText(sp.getNotiTopic());
        Log.d(TAG, "onItemSelected:  " + sp.getNotiTopic());

        Snackbar.make(mLinearLayout, "Recieve Notification according to " + item, Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
