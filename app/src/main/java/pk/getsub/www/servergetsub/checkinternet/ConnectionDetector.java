package pk.getsub.www.servergetsub.checkinternet;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by hp on 1/5/2018.
 */

public class ConnectionDetector {
    private Context context;

    public ConnectionDetector(Context context){
        this.context = context;
    }
    public boolean CheckConnected(){

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE); // Context.CONNECTIVITY_SERVICE
        if(connectivity !=null){
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if(info != null && info.isConnected()){
                return true;
            }
        }
        return false;
    }
}


