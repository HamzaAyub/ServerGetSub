package pk.getsub.www.servergetsub.splashscreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pk.getsub.www.servergetsub.R;
import pk.getsub.www.servergetsub.map.OrderMapActivity;

public class MainSplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_splash_screen);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    synchronized (this) {
                        wait(2000);
                    }


                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                finally {
                    Intent i = new Intent(MainSplashScreenActivity.this,OrderMapActivity.class);
                    startActivity(i);
                    finish();
                }

            }


        }).start();

    }
}
