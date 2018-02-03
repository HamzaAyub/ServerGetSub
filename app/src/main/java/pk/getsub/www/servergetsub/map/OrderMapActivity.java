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
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;
import pk.getsub.www.servergetsub.R;
import pk.getsub.www.servergetsub.UserSharPrefer;
import pk.getsub.www.servergetsub.checkaddress.CheckAddressActivity;
import pk.getsub.www.servergetsub.checkinternet.ConnectionDetector;
import pk.getsub.www.servergetsub.phoneauth.CustomPhoneAuthActivity;
import pk.getsub.www.servergetsub.splashscreen.SplashScreen;

public class OrderMapActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener,
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

    private double myLongCord;
    private double myLatCord;


/*
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
        }

        connectionDetector = new ConnectionDetector(this);
        if (connectionDetector.CheckConnected()) {
            //    Toast.makeText(this, "Connecteedd", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onCreate: Connectedd");


            editDetailOrder = findViewById(R.id.edit_order_map);
            btnOrderMap = findViewById(R.id.btn_send_order_map);

            btnOrderMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String detailOrder = editDetailOrder.getText().toString();
                    if (detailOrder.equals("")) {
                        Toast.makeText(OrderMapActivity.this, "Enter Order", Toast.LENGTH_SHORT).show();
                      checkOrderBox("Please Enter Your Order");

                    } else {
                    //    Toast.makeText(OrderMapActivity.this, "Sendddddddddddddddd", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OrderMapActivity.this, CheckAddressActivity.class).putExtra("myOrder", detailOrder));

                    }
                }
            });

            btnCallOrder = findViewById(R.id.btn_call_order_map);
            btnCallOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:03328469195"));

                    if (ActivityCompat.checkSelfPermission(OrderMapActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);

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

            ////////////////////////////////////////////////////////////


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }


            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


        } // Connector dectetor if
        else {
            Toast.makeText(this, "Nottttttttt", Toast.LENGTH_SHORT).show();
            showMessage("Check Your Internet");
          //  Log.d(TAG, "onCreate: Not Connected ");
        }
    }

    */
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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
            System.exit(0);
        }

        connectionDetector = new ConnectionDetector(this);

        editDetailOrder = findViewById(R.id.edit_order_map);
        btnOrderMap = findViewById(R.id.btn_send_order_map);

        btnOrderMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String detailOrder = editDetailOrder.getText().toString();
                // check for iqbal town

                double startLat = 31.480000 ;
                double endLat = 31.529999;
                double startLong = 74.250000;
                double endLong = 74.299999;
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

        ////////////////////////////////////////////////////////////
/*      Map One
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        locationManager.requestLocationUpdates(provider, 1000, 0, OrderMapActivity.this);

        //  locationManager.requestLocationUpdates(provider, 5000, 5, OrderMapActivity.this);
    }


    private void showAlert(final int status){
        String message , title , btnText;
        if(status ==1){
            message = "Your Location is Off \n Please Enable Location" + "use this app";
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
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
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
         //   Toast.makeText(this, "My History", Toast.LENGTH_SHORT).show();
        }
        else if(id == R.id.id_log_out_menu){


            myLogOut();
         //   startActivity(new Intent(OrderMapActivity.this , SplashScreen.class));
            startActivity(new Intent(OrderMapActivity.this , CustomPhoneAuthActivity.class));
            finish();
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

        try {
            File f = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            circleProfileDetailImage.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "loadImageFromStorage: " + e.toString());
        }

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

    // area check message






}


