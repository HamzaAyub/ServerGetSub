package pk.getsub.www.servergetsub.deliveryman;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import pk.getsub.www.servergetsub.R;
import pk.getsub.www.servergetsub.UserSharPrefer;

public class DeliveryManDisplay extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "HTAG";
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    private GoogleMap mMap;
    private UserSharPrefer sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_man_display);
        sp = new UserSharPrefer(this);

        Toolbar toolbar = findViewById(R.id.toolbar_deliveryman_display);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //   mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(0.0, 0.0);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        LatLng sydney = new LatLng(0.0, 0.0);
        mMap = googleMap;
        mMap.setMaxZoomPreference(16);

        /*mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.phone)));*/


        //loginToFirebase();
        subscribeToUpdates();
    }


    private void subscribeToUpdates() {
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(getString(R.string.firebase_path)+ "/" + "5");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("locations");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);
               /* if(dataSnapshot.getKey().equals("status")){
                    Log.d(TAG, "onChildAdded:///////////////: cancel value : " + dataSnapshot.getValue());

                }*/
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void setMarker(DataSnapshot dataSnapshot) {
        Log.d(TAG, " Key from Sp : " + sp.getDeliveryManId());
        String key = dataSnapshot.getKey();
        //if(key.equals(sp.getNotiMessage())) {
        if (key.equals(sp.getDeliveryManId())) {
            Log.d(TAG, "setMarker: key match  " + key);
            // String key = sp.getNotiMessage();
            //Log.d(TAG, "setMarker: Notification Message " + key );
            // Log.d(TAG, "setMarker: key : " +key);
            HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
            double lat = Double.parseDouble(value.get("latitude").toString());
            double lng = Double.parseDouble(value.get("longitude").toString());
            String status_cancel = value.get("status").toString();
            Log.d(TAG, "STATUS cancel : " + status_cancel);

            if (status_cancel.equals("true")) { // true means delivery boy stop sharing location
                Log.d(TAG, " status_cancel is true so you can not see location");
                // sp.setNotiMessage("firebase");
                sp.setDeliveryManId("firebase");
                // return to back activity
            }

       /*     Log.d(TAG, "setMarker: latitude : " + lat);
            Log.d(TAG, "setMarker:  longitude : " + lng);*/
            LatLng location = new LatLng(lat, lng);
            if (!mMarkers.containsKey(key)) {
                mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(key).position(location)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo32))));
            } else {
                mMarkers.get(key).setPosition(location);
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : mMarkers.values()) {
                builder.include(marker.getPosition());
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300))

            ;
        } else {
            Log.d(TAG, "setMarker: key nottttttttttttttt ");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
