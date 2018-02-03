package pk.getsub.www.servergetsub.map;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import pk.getsub.www.servergetsub.R;

public class CallOrderActivity extends AppCompatActivity {


    private static final String TAG = "HTAG";
    private Button btnFirstCall;
    private Button btnSecondCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_order);

        btnFirstCall = findViewById(R.id.btn_first_call_order);
        btnSecondCall = findViewById(R.id.btn_second_call_order);

        btnFirstCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //     Toast.makeText(CallOrderActivity.this, "Test Calll Button", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:03494906012"));

                if (ActivityCompat.checkSelfPermission(CallOrderActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    showMessage("Allow Permission For Call");

                    return;
                }

                startActivity(intent);
            }
        });

        btnSecondCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:03494906012"));

                if (ActivityCompat.checkSelfPermission(CallOrderActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    showMessage("Allow Permission For Call");
                    return;
                }
                startActivity(intent);

            }
        });

    }


    public void showMessage(final String msg) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Alert Message")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  Snackbar.make( constraintLayout, msg ,Snackbar.LENGTH_SHORT).show();
                        Log.d(TAG, "showMessageBox: " + msg);

                     /*   Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);*/
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS , Uri.parse("package:" +getPackageName()));
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        //    OrderMapActivity.super.onBackPressed();
                    }
                })
                .show();
    }


}


