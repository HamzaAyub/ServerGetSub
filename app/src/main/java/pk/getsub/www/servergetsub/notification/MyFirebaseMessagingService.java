package pk.getsub.www.servergetsub.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import pk.getsub.www.servergetsub.R;
import pk.getsub.www.servergetsub.UserSharPrefer;
import pk.getsub.www.servergetsub.deliveryman.DeliveryManDisplay;
import pk.getsub.www.servergetsub.splashscreen.MainSplashScreenActivity;

/**
 * Created by hp on 4/30/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "HTAG";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


/*

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
             //   scheduleJob();
                Log.d(TAG, "onMessageReceived: Schedule jobs");
            } else {
                // Handle message within 10 seconds
             //   handleNow();
                Log.d(TAG, "onMessageReceived: Handle Now");
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }



        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "some_channel_id";
            CharSequence channelName = "Some Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("my_id_1", "my_chanel_name", NotificationManagerCompat.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            //      notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);

        }




        Intent intent = new Intent(this, NotificationDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "my_id_1")
                .setSmallIcon(R.drawable.mylogo)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);



// notificationId is a unique int for each notification that you must define
        notificationManagerCompat.notify(233, mBuilder.build());*/


        //////////////////////////////////////////////////////////////////////////////////////////////


       /* UserSharPrefer sp = new UserSharPrefer(this);


        FirebaseMessaging.getInstance().subscribeToTopic(sp.getNotiTopic());*/


        UserSharPrefer sp = new UserSharPrefer(this);
        if (remoteMessage.getData().containsKey("title")){
            Log.d(TAG, "onMessageReceived:  Title Value:  " + remoteMessage.getData().get("title"));
            sp.setNotiTitle(remoteMessage.getData().get("title"));
        }
        if (remoteMessage.getData().containsKey("message")) {
            Log.d(TAG, "onMessageReceived:  Message : " + remoteMessage.getData().get("message"));
            sp.setNotiMessage(remoteMessage.getData().get("message"));
        }
        if (remoteMessage.getData().containsKey("deliverman_id")){
            sp.setDeliveryManId(remoteMessage.getData().get("deliverman_id"));
            Intent intent = new Intent(MyFirebaseMessagingService.this, DeliveryManDisplay.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }


        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "my_chanel_name";
            String description = "my_channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("chanel_id", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, NotificationDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MyFirebaseMessagingService.this, "chanel_id")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.mylogo)
                .setContentText(remoteMessage.getNotification().getBody())
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(101, mBuilder.build());


    }
}
