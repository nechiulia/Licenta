package com.example.teammanagement.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.location.places.Places;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ImageButton ibtn_back;
    private AutoCompleteTextView et_searchText;
    private static final String TAG = "MapsActivity";

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initComponents();

    }

    public void initComponents(){
        ibtn_back=findViewById(R.id.maps_ibtn_back);
        et_searchText=findViewById(R.id.maps_et_searchBar);

        et_searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE){
                    geoLocate();
                }
                return false;
            }
        });


        ibtn_back.setOnClickListener(clickBack());

    }

    private void geoLocate(){
        String searchText = et_searchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);

        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchText,1);
        }catch (IOException ex){
            ex.printStackTrace();
        }
        if(list.size() != 0){
            Address address = list.get(0);
            Log.d(TAG,"geoLocate found a location "+address.toString());
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),Constants.DEFAULT_ZOOM,address.getAddressLine(0));
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

            builder.setTitle("Eroare căutare locație")
                    .setMessage("Nu s-a găsit nicio locație cu această denumire")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            Dialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }


    }

    private View.OnClickListener clickBack(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
            }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng bucharest = new LatLng(44.4268, 26.1025);
        mMap.addMarker(new MarkerOptions().position(bucharest).title("București"));
        moveCamera(new LatLng(bucharest.latitude,bucharest.longitude), Constants.DEFAULT_ZOOM,"București");
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        MarkerOptions options = new MarkerOptions().position(latLng).title(title);
        mMap.addMarker(options);
    }


    public void getPlace(){
/*
        PendingResult<PlaceBuffer> placeBufferPendingResult = Places.GeoDataApi.getPlaceById(m)*/

    }

}
