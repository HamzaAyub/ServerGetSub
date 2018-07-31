package pk.getsub.www.servergetsub.map;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;
import pk.getsub.www.servergetsub.R;
import pk.getsub.www.servergetsub.UserSharPrefer;
import pk.getsub.www.servergetsub.checkaddress.CheckAddressActivity;
import pk.getsub.www.servergetsub.checkinternet.ConnectionDetector;
import pk.getsub.www.servergetsub.deliveryman.DeliveryManDisplay;
import pk.getsub.www.servergetsub.history.HistoryActivity;
import pk.getsub.www.servergetsub.notification.NotificationDetailActivity;
import pk.getsub.www.servergetsub.phoneauth.CustomPhoneAuthActivity;
import pk.getsub.www.servergetsub.splashscreen.SplashScreen;


public class OrderMapActivity extends FragmentActivity implements OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HTAG";
    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    private static final int REQUEST_LOCATION_CODE = 99;

    private ConnectionDetector connectionDetector;
    private Button btnCallOrder;
    private EditText editDetailOrder;
    private Button btnOrderMap;
    private View profileDetailView;
    private TextView txtProfileDetailName;
    private TextView txtProfileDetailPhone;
    private CircleImageView circleProfileDetailImage;
    private UserSharPrefer storeUser;


// new MAp

    private LatLng myCoordinates;
    private LocationManager locationManager;
    private final static int PERMISSION_ALL = 1;
    private final static String[] PERMISSION = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private Marker marker;

    private View mapView;

    private double myLongCord = 0.0;
    private double myLatCord = 0.0;
    private Location initialLocation = null;
    private CameraPosition mCameraPosition;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 16;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private double myLati = 0.0;
    private double myLong = 0.0;


    private int varTest = 0;

    private double dbLatitude = 0.0;
    private double dblLongitude = 0.0;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    /*    if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }*/

        setContentView(R.layout.activity_order_map);
        Toolbar toolbar = findViewById(R.id.toolbar_order_map_activity);
        //setSupportActionBar(toolbar);
        storeUser = new UserSharPrefer(OrderMapActivity.this); // initialize store variable of sp
        mDatabase = FirebaseDatabase.getInstance().getReference();

        /*if(getIntent().getExtras() != null){
            for(String key : getIntent().getExtras().keySet()) {
                if (key.equals("title")) {
                    //    txtTitile.setText(getIntent().getExtras().getString(key));
                    storeUser.setNotiTitle(getIntent().getExtras().getString(key));
                } else if (key.equals("message")) {
                    //   txtMessage.setText(getIntent().getExtras().getString(key))
                    storeUser.setNotiMessage(getIntent().getExtras().getString(key));
                }

            }

            Log.d(TAG, "onCreate: Activity have some data");
        }
        else {
            Log.d(TAG, "onCreate: Not have Data");
        }
*/

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); // for order send check

        if (storeUser.getName().equals("mNull") && storeUser.getUserAddress().equals("mNull")) {
            startActivity(new Intent(OrderMapActivity.this, CustomPhoneAuthActivity.class));
            finish();
            //System.exit(0);
            return;
        }

        connectionDetector = new ConnectionDetector(this);

        editDetailOrder = findViewById(R.id.edit_order_map);
        btnOrderMap = findViewById(R.id.btn_send_order_map);

        btnOrderMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String detailOrder = editDetailOrder.getText().toString();
                if (connectionDetector.CheckConnected()) {
                    //     Log.d(TAG, "onClick: First Check");
                } else {
                    showMessage("Check Your Internet");
                    return;

                }

                // check for iqbal town

                double startLat = 31.480000;
                double endLat = 31.529999;
                double startLong = 74.250000;
                double endLong = 74.299999;

// with out change location

                if (ActivityCompat.checkSelfPermission(
                        OrderMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(
                        OrderMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                initialLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (initialLocation != null) {
                    myLatCord = initialLocation.getLatitude();
                    myLongCord = initialLocation.getLongitude();
                } else {
                    initialLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    //     Log.d(TAG, " Else Part of initial Location");
                }

                if (initialLocation != null) {
                    myLatCord = initialLocation.getLatitude();
                    myLongCord = initialLocation.getLongitude();
                } else {
                    alertMessageInitialaLocation(1);
                    return;
                }
                if (!((myLatCord > 31.480000 && myLatCord < 31.529999 && myLongCord > 74.250000 && myLongCord < 74.299999)
                        || (myLatCord > 31.627000 && myLatCord < 31.630000 && myLongCord > 74.284000 && myLongCord < 74.287000)
                        || (myLatCord > 31.370000 && myLatCord < 31.400000 && myLongCord > 74.220000 && myLongCord < 74.250000)
                )) {
                    checkOrderBox("Sorry.... \nOur Service Not Availabe At your Area");
                    return;
                }

                if (connectionDetector.CheckConnected()) {
                    if (detailOrder.equals("")) {
                        checkOrderBox("Please Enter Your Order");

                    } else {
                        startActivity(new Intent(OrderMapActivity.this, CheckAddressActivity.class).putExtra("myOrder", detailOrder));

                    }
                } else {
                    showMessage("Check Your Internet");
                }
            }
        });

        btnCallOrder = findViewById(R.id.btn_call_order_map);
        btnCallOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderMapActivity.this, CallOrderActivity.class));

            }
        });

        ////////////////////////////////////////////////////////////

        DrawerLayout drawer = findViewById(R.id.drawer_layout_order_map_activity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        // actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPureWhite));
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_order_map);
        navigationView.setNavigationItemSelectedListener(this);

        profileDetailView = navigationView.getHeaderView(0);
        circleProfileDetailImage = profileDetailView.findViewById(R.id.profile_detail_circle_iamge);
        txtProfileDetailName = profileDetailView.findViewById(R.id.txt_profile_detail_name);
        txtProfileDetailPhone = profileDetailView.findViewById(R.id.txt_profile_detail_phone);
        txtProfileDetailName.setText(storeUser.getName()); // get text from sp and sett in profiile
        txtProfileDetailPhone.setText(storeUser.getUserPhone());

// cz "myImgPath" is not store in UserProfileAcitivity
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        String ss = sp.getString("myImgPath", "mNull");

        loadImageFromStorage(ss);  // comment just for some testing ..

/*
        // cz "myImgPath" is not store in UserProfileAcitivity
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        String ss = sp.getString("myImgPath", "mNull");
        if(ss.equals("mNull")){
            return;
        }else {
            loadImageFromStorage(ss);
        }*/


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        /*LatLng sydney = new LatLng(0.0, 0.0);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.phone)));*/

        //    mMap.setPadding(0, 420, 0, 0);

        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 60, 60);
        getLocationPermission();
        updateLocationUI();
        createLocationRequest();

    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            Log.d(TAG, "getLocationPermission: 1");
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            Log.d(TAG, "getLocationPermission: 4");
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission is Granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        mLocationPermissionGranted = true;
                        mMap.setMyLocationEnabled(true);
                    }
                } else  // permission is denied
                {
                    Log.d(TAG, "onRequestPermissionsResult: requet location permission");
                    return;
                }
                return;
        }


        updateLocationUI();
    }


    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    protected void createLocationRequest() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                if (locationSettingsResponse.getLocationSettingsStates().isGpsUsable()
                        && locationSettingsResponse.getLocationSettingsStates().isNetworkLocationUsable()) {
                    Log.d(TAG, "onSuccess: Location is Enable");
                    //    getDeviceLocation();
                    myCurrentLocation();
                } else {
                    Log.d(TAG, "onSuccess: Location Is Not Enabel");
                    showAlert(1);
                    return;
                }
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.

                    Log.d(TAG, "onFailure: ResolveAvle Error ");
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(OrderMapActivity.this, 1);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                } else {
                    Log.d(TAG, "onFailure: Not Resolveable Error ");
                }
            }
        });


    }


    private void myCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            showAlert(1);
            return;
        }
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    Log.d(TAG, " Latitude : " + location.getLatitude() + " Longitude : " + location.getLongitude());

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(),
                                    location.getLongitude()), DEFAULT_ZOOM));

                    dbLatitude = location.getLatitude();
                    dblLongitude = location.getLongitude();

                    String userID = Integer.toString(storeUser.getUserId());
                    mDatabase.child("location").child(userID).child("latitude").setValue(dbLatitude);
                    mDatabase.child("location").child(userID).child("longitude").setValue(dblLongitude);

                } else {
                    Log.d(TAG, "onSuccess: Location Not Enable Last Method");


                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                }
            }

        });
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////

    // else code then map importan code

    private void showAlert(final int status) {
        String message, title, btnText;
        if (status == 1) {
            message = "Your Location is Off "; // Please Enable Location" + "use this app
            title = "Enabel Location";
            btnText = "Location Setting";
        } else {
            message = "please allow this app to access location";
            title = "Permission access ";
            btnText = "Greate";
        }
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnText, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (status == 1) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                        /*else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(PERMISSION, PERMISSION_ALL);
                            }
                        }*/
                    }
                })
             /*   .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Activity goes back to previous check some code

                        finish();

                    }
                })*/
        ;
        dialog.show();
    }


/*

    public boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;

        } else
            return true;
    }




*/


    public void showMessage(final String msg) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Alert Message")
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  Snackbar.make( constraintLayout, msg ,Snackbar.LENGTH_SHORT).show();
                        Log.d(TAG, "showMessageBox: " + msg);

                        //    OrderMapActivity.super.onBackPressed();
                    }
                })
                .show();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.id_home_menu) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_order_map_activity);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
        } else if (id == R.id.id_call_order_menu) {
            startActivity(new Intent(OrderMapActivity.this, CallOrderActivity.class));
        } else if (id == R.id.id_my_profile_menu1) {
            startActivity(new Intent(OrderMapActivity.this, UserProfileDetailActivity.class));


        } else if (id == R.id.id_history_menu) {
            //    Log.d(TAG, "onNavigationItemSelected: Hidtoryyyyy");
            startActivity(new Intent(OrderMapActivity.this, HistoryActivity.class));
        } else if (id == R.id.id_log_out_menu) {

            logOutMsg("Are You Sure want to Log Out");

          /*  myLogOut();
         //   startActivity(new Intent(OrderMapActivity.this , SplashScreen.class));
            startActivity(new Intent(OrderMapActivity.this , CustomPhoneAuthActivity.class));
            finish();*/
        } else if (id == R.id.id_about_us_menu) {
            startActivity(new Intent(OrderMapActivity.this, AboutUsActivity.class));
        } else if (id == R.id.id_notification_menu) {
            startActivity(new Intent(OrderMapActivity.this, NotificationDetailActivity.class));
        } else if (id == R.id.id_delivery_man_menu) {
            startActivity(new Intent(OrderMapActivity.this, DeliveryManDisplay.class));
        }

        return false;
    }

    private void myLogOut() {
        storeUser.setUserAddress("mNull");
        storeUser.setName("mNull");
        storeUser.setUserPhone("mNull");
        storeUser.setUserId(0);
        storeUser.setImgPathInFile("mNull");

        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_order_map_activity);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //  super.onBackPressed();
            moveTaskToBack(true);

        }
    }
/*
    @Override
    protected void onRestart() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        super.onRestart();
    }*/


    // method for show image from internal storage
    private void loadImageFromStorage(String path) {


        // there is exception showing on this bcz image not uploaded  ... future thek karna hai


       /* try {
            File f = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            circleProfileDetailImage.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "loadImageFromStorage: " + e.toString());
        }*/

    }

    private void checkOrderBox(final String msg) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Alert Message")
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  Snackbar.make( constraintLayout, msg ,Snackbar.LENGTH_SHORT).show();
                        Log.d(TAG, "showMessageBox: order field check " + msg);

                        return;
                    }
                })
                .show();
    }


    private void logOutMsg(final String msg) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Log Out")
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  Snackbar.make( constraintLayout, msg ,Snackbar.LENGTH_SHORT).show();
                        Log.d(TAG, "Log Out Message " + msg);
                        // return;

                        myLogOut();
                        //   startActivity(new Intent(OrderMapActivity.this , SplashScreen.class));
                        startActivity(new Intent(OrderMapActivity.this, CustomPhoneAuthActivity.class));
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "click logoutMsg No Response");
                        return;
                    }
                })
                .show();
    }


    private void alertMessageInitialaLocation(final int status) {
        String message, title, btnText;
        if (status == 1) {
            message = "If Location is on then wait few seconds. "; // Please Enable Location" + "use this app
            title = "Enabel Location";
            btnText = "Location Setting";
        } else {
            message = "please allow this app to access location";
            title = "Permission access ";
            btnText = "Greate";
        }
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnText, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (status == 1) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                        /*else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(PERMISSION, PERMISSION_ALL);
                            }
                        }*/
                    }
                })
             /*   .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Activity goes back to previous check some code

                        finish();

                    }
                })*/
        ;
        dialog.show();
    }


}


