package pk.getsub.www.servergetsub.map;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;
import pk.getsub.www.servergetsub.R;
import pk.getsub.www.servergetsub.UserSharPrefer;
import pk.getsub.www.servergetsub.checkaddress.CheckAddressActivity;
import pk.getsub.www.servergetsub.checkinternet.ConnectionDetector;
import pk.getsub.www.servergetsub.history.HistoryActivity;
import pk.getsub.www.servergetsub.phoneauth.CustomPhoneAuthActivity;
import pk.getsub.www.servergetsub.splashscreen.SplashScreen;
public class OrderMapActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener,
        NavigationView.OnNavigationItemSelectedListener {


  /*  public class OrderMapActivity extends FragmentActivity implements OnMapReadyCallback ,NavigationView.OnNavigationItemSelectedListener  {
*/
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

    private double myLongCord = 0.0;
    private double myLatCord = 0.0;
    private Location initialLocation;

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

    private double myLati =0.0 ;
    private double myLong = 0.0;


        private int varTest =0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_map);
        Toolbar toolbar = findViewById(R.id.toolbar_order_map_activity);
        //setSupportActionBar(toolbar);
        storeUser = new UserSharPrefer(OrderMapActivity.this); // initialize store variable of sp
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
                // check for iqbal town

                double startLat = 31.480000;
                double endLat = 31.529999;
                double startLong = 74.250000;
                double endLong = 74.299999;

// with out change location

                if (ActivityCompat.checkSelfPermission(OrderMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(OrderMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                initialLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                myLatCord = initialLocation.getLatitude();
                myLongCord = initialLocation.getLongitude();


                if(!(myLatCord >31.480000 && myLatCord < 31.529999 && myLongCord > 74.250000 && myLongCord <74.299999)){
                    checkOrderBox("Sorry... \nOur Service Not Availabe At your Area");
                    return;
                }

                if (connectionDetector.CheckConnected()) {


                    if (detailOrder.equals("")) {
                        //   Toast.makeText(OrderMapActivity.this, "Enter Order", Toast.LENGTH_SHORT).show();
                        checkOrderBox("Please Enter Your Order");

                    } else {
                        //    Toast.makeText(OrderMapActivity.this, "Sendddddddddddddddd", Toast.LENGTH_SHORT).show();
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_order_map_activity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        // actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPureWhite));
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_order_map);
        navigationView.setNavigationItemSelectedListener(this);

        profileDetailView = navigationView.getHeaderView(0);
        circleProfileDetailImage = (CircleImageView) profileDetailView.findViewById(R.id.profile_detail_circle_iamge);
        txtProfileDetailName = (TextView) profileDetailView.findViewById(R.id.txt_profile_detail_name);
        txtProfileDetailPhone = (TextView) profileDetailView.findViewById(R.id.txt_profile_detail_phone);
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



        ////////////////////////////////////////////////////////////
  //    Map One
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // e code map on ka haiiiiiii
            checkLocationPermission();
        }*/

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);



        //  mo = new MarkerOptions().position(new LatLng(0,0)).title("My Current Location");
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            requestPermissions(PERMISSION, PERMISSION_ALL);
        } else requestLocation();
        if (!isLocationEnabled()) {
            showAlert(1);
            // Toast.makeText(this, "Your Location is Of Please Enabel It", Toast.LENGTH_SHORT).show();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isPermissionGranted() {

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "isPermissionGranted:  Permission Is Granted");
            return true;
        } else {
            Log.d(TAG, "isPermissionGranted: Permission Not Granted");
            return false;
        }
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(0, 420, 0, 0);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            showAlert(1);

            return;
        }
        mMap.setMyLocationEnabled(true);

    }


    @Override
    public void onLocationChanged(Location location) {
        LatLng myCordinate = new LatLng(location.getLatitude() , location.getLongitude());

        myLatCord = location.getLatitude();
        myLongCord = location.getLongitude();

        float zoomLevel = 16.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myCordinate,zoomLevel));

        //   mMap.moveCamera(CameraUpdateFactory.newLatLng(myCordinate));  // Current Location but not zom
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void requestLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        locationManager.requestLocationUpdates(provider, 5000, 5, OrderMapActivity.this);
       // locationManager.requestLocationUpdates();

        //  locationManager.requestLocationUpdates(provider, 1000, 0, OrderMapActivity.this);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        // new Code of map


/*


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.


        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }


        setContentView(R.layout.activity_order_map);
        Toolbar toolbar = findViewById(R.id.toolbar_order_map_activity);
        //setSupportActionBar(toolbar);
        storeUser = new UserSharPrefer(OrderMapActivity.this); // initialize store variable of sp
        if (storeUser.getName().equals("mNull") && storeUser.getUserAddress().equals("mNull")) {
            startActivity(new Intent(OrderMapActivity.this, CustomPhoneAuthActivity.class));
            finish();
            System.exit(0);
        }



        // for google map
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!isLocationEnabled()) {

           */
/* varTest = 1;
            Log.d(TAG, "onCreate: is location test " + varTest);*//*


            showAlert(1);
        */
/*    Log.d(TAG, "onCreate: 1");*//*


            return;
        }
    //    Log.d(TAG, "onCreate: 2");


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

   //     Log.d(TAG, "onCreate: 3");

      */
/*  Log.d(TAG, "onCreate: "+ varTest);
        if(varTest == 1){
            Log.d(TAG, "onCreate: Tthis is my varTest");


        }*//*





        connectionDetector = new ConnectionDetector(this);

        editDetailOrder = findViewById(R.id.edit_order_map);
        btnOrderMap = findViewById(R.id.btn_send_order_map);

        btnOrderMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String detailOrder = editDetailOrder.getText().toString();
                // check for iqbal town


                if(!(myLatCord >31.480000 && myLatCord < 31.529999 && myLongCord > 74.250000 && myLongCord <74.299999)){
                    checkOrderBox("Sorry... \nOur Service Not Availabe At your Area");
                    return;
                }

                if (connectionDetector.CheckConnected()) {


                    if (detailOrder.equals("")) {
                        //   Toast.makeText(OrderMapActivity.this, "Enter Order", Toast.LENGTH_SHORT).show();
                        checkOrderBox("Please Enter Your Order");

                    } else {
                        //    Toast.makeText(OrderMapActivity.this, "Sendddddddddddddddd", Toast.LENGTH_SHORT).show();
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_order_map_activity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        // actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPureWhite));
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_order_map);
        navigationView.setNavigationItemSelectedListener(OrderMapActivity.this);

        profileDetailView = navigationView.getHeaderView(0);
        circleProfileDetailImage = (CircleImageView) profileDetailView.findViewById(R.id.profile_detail_circle_iamge);
        txtProfileDetailName = (TextView) profileDetailView.findViewById(R.id.txt_profile_detail_name);
        txtProfileDetailPhone = (TextView) profileDetailView.findViewById(R.id.txt_profile_detail_phone);
        txtProfileDetailName.setText(storeUser.getName()); // get text from sp and sett in profiile
        txtProfileDetailPhone.setText(storeUser.getUserPhone());


// cz "myImgPath" is not store in UserProfileAcitivity
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        String ss = sp.getString("myImgPath", "mNull");

        loadImageFromStorage(ss);  // comment just for some testing ..

        ////////////////////////////////////////////////////////////
*/
/*      Map One
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }*//*


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

   //     locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);



        */
/*//*
/  mo = new MarkerOptions().position(new LatLng(0,0)).title("My Current Location");
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            requestPermissions(PERMISSION, PERMISSION_ALL);
        } else requestLocation();*//*



    }

        private boolean isLocationEnabled() {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        }



    */
/**
     * Saves the state of the map when the activity is paused.
     *//*

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }



    */
/**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     *//*

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.setPadding(0, 420, 0, 0);




        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        if (isLocationEnabled()) {
            // Get the current location of the device and set the position of the map.
            getDeviceLocation();
        }else{
            showAlert(1);
            return;
        }
    }



    */
/**
     * Gets the current location of the device, and positions the map's camera.
     *//*

    private void getDeviceLocation() {
        */
/*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         *//*

        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.

                            if (!isLocationEnabled()) {
                                Log.d(TAG, "onComplete: with in getDeviceLocation Method");
                                showAlert(1);

                                return;
                            }

                            mLastKnownLocation = task.getResult();

                            myLatCord =   task.getResult().getLatitude();
                            myLongCord =   task.getResult().getLongitude();

                            Log.d(TAG, "onComplete: " + myLati + " // " + myLong);

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    */
/**
     * Prompts the user for permission to use the device location.
     *//*

    private void getLocationPermission() {
        */
/*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         *//*


       */
/* if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }*//*



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            mLocationPermissionGranted = false;

        }
        else
            mLocationPermissionGranted = true;

    }

    */
/**
     * Handles the result of the request for location permissions.
     *//*

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
       */
/* mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }*//*


        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission is Granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        mLocationPermissionGranted = true;

                        mMap.setMyLocationEnabled(true);

                    }
                } else  // permission is denied
                {
                   // Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    showAlert(1);
                }
                return;
        }




        updateLocationUI();
    }



    */
/**
     * Updates the map's UI settings based on whether the user has granted location permission.
     *//*

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
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
*/



///////////////////////////////////////////////////////////////////////////////////////////



/// old google map api codee  ....  not confirm

    /*

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isPermissionGranted() {

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "isPermissionGranted:  Permission Is Granted");
            return true;
        } else {
            Log.d(TAG, "isPermissionGranted: Permission Not Granted");
            return false;
        }
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(0, 420, 0, 0);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // edar ye ni karna .. ni ty paht jani hai
          *//*  Intent intent = getIntent();
            finish();
            startActivity(intent);*//*

            showAlert(1);

            return;
        }
        mMap.setMyLocationEnabled(true);

    }


    @Override
    public void onLocationChanged(Location location) {
        LatLng myCordinate = new LatLng(location.getLatitude() , location.getLongitude());

        myLatCord = location.getLatitude();
        myLongCord = location.getLongitude();

        float zoomLevel = 16.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myCordinate,zoomLevel));

        //   mMap.moveCamera(CameraUpdateFactory.newLatLng(myCordinate));  // Current Location but not zom
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void requestLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        locationManager.requestLocationUpdates(provider, 5000, 5, OrderMapActivity.this);
        // locationManager.requestLocationUpdates();

        //  locationManager.requestLocationUpdates(provider, 1000, 0, OrderMapActivity.this);
    }

*/






























    ///////////////////////////////////////////////////////////////////////////////////////////////

    // else code then map importan code

    private void showAlert(final int status){
        String message , title , btnText;
        if(status ==1){
            message = "Your Location is Off "; // Please Enable Location" + "use this app
            title = "Enabel Location";
            btnText = "Location Setting";
        }
        else {
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
                        if(status ==1){
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

////////////////////////////////////////////////////////////////////////
   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission is Granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (client == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);

                    }
                } else  // permission is denied
                {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    showAlert(1);
                }
                return;
        }

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // mMap.setPadding(L,t,r,b);
        mMap.setPadding(0, 420, 0, 0);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        //    buildGoogleApiClient();  mOne
            mMap.setMyLocationEnabled(true);
        }


    }*/

//////////////////////////////////////
    // MAp One Code

   /*
    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }

    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        currentLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));
        if (client != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }
    }*/

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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
        if(id == R.id.id_home_menu){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_order_map_activity);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
        }
        else if(id == R.id.id_call_order_menu){
            startActivity(new Intent(OrderMapActivity.this , CallOrderActivity.class));
        }
        else if(id == R.id.id_my_profile_menu1){
            startActivity(new Intent(OrderMapActivity.this, UserProfileDetailActivity.class));


        }
        else if(id == R.id.id_history_menu){
        //    Log.d(TAG, "onNavigationItemSelected: Hidtoryyyyy");
            startActivity(new Intent(OrderMapActivity.this , HistoryActivity.class));
        }
        else if(id == R.id.id_log_out_menu){

            logOutMsg("Are You Sure want to Log Out");

          /*  myLogOut();
         //   startActivity(new Intent(OrderMapActivity.this , SplashScreen.class));
            startActivity(new Intent(OrderMapActivity.this , CustomPhoneAuthActivity.class));
            finish();*/
        }

        else if(id == R.id.id_about_us_menu){
            startActivity(new Intent(OrderMapActivity.this , AboutUsActivity.class));
        }

        return false;
    }

    private void myLogOut(){
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

    @Override
    protected void onRestart() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        super.onRestart();
    }



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
                        startActivity(new Intent(OrderMapActivity.this , CustomPhoneAuthActivity.class));
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






}


