package pk.getsub.www.servergetsub.notification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import pk.getsub.www.servergetsub.UserSharPrefer;

/**
 * Created by hp on 4/30/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private final static String TAG = "HTAG";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        UserSharPrefer sp = new UserSharPrefer(this);
        sp.setFcm_token(refreshedToken);
        sp.setFlagForTokenAndTopic("true");
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }
}
