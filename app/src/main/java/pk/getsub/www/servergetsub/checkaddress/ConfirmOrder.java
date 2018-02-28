package pk.getsub.www.servergetsub.checkaddress;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pk.getsub.www.servergetsub.R;
import pk.getsub.www.servergetsub.map.OrderMapActivity;

public class ConfirmOrder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        showMessage("Your Order is Confirm");
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
                        //   Log.d(TAG, "showMessageBox: " + msg);

                        startActivity(new Intent(ConfirmOrder.this , OrderMapActivity.class));
                    }
                })
                .show();
    }
}
