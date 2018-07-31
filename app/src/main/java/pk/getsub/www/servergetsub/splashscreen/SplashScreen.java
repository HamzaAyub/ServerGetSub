package pk.getsub.www.servergetsub.splashscreen;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import pk.getsub.www.servergetsub.R;

public class SplashScreen extends AppCompatActivity {
    private static final String TAG = "HTG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(20000);
//                  //  startActivity(new Intent(SplashScreen.this , MainActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}