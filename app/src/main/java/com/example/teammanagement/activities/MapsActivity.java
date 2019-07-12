package com.example.teammanagement.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Location;
import com.example.teammanagement.dialogs.LocationMarkerDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ImageButton ibtn_back;
    private AutoCompleteTextView et_searchText;
    private static final String TAG = "MapsActivity";
    private Map<Integer,String> program = new HashMap<>();
    private Address address;
    Location l1;

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
        mMap.clear();

        String searchText = et_searchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);

        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchText,1);
        }catch (IOException ex){
            ex.printStackTrace();
        }
        if(list.size() != 0){
            address = list.get(0);
            Log.d(TAG,"geoLocate found a location "+address.toString());
            moveCamera(new LatLng(address.getLatitude(),
                    address.getLongitude()),
                    Constants.DEFAULT_ZOOM,address.getAddressLine(0));
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

            builder.setTitle(getString(R.string.maps_alertDialog_errorLocation_title_hint))
                    .setMessage(getString(R.string.maps_alertDialog_errorLocation_message_hint))
                    .setNeutralButton(getString(R.string.maps_alertDialog_errorLocation_positiveButton_hint), new DialogInterface.OnClickListener() {
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng bucharest = new LatLng(44.4268, 26.1025);
        moveCamera(new LatLng(bucharest.latitude,bucharest.longitude), Constants.DEFAULT_ZOOM,"București");
        mMap.setOnMarkerClickListener(clickMarker());
    }

    private void moveCamera(final LatLng latLng, float zoom, String title){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        MarkerOptions options = new MarkerOptions().position(latLng).title(title);
        mMap.addMarker(options);

    }


    private void openDialog(){
        Bundle args = new Bundle();
        args.putSerializable(Constants.MAP_LOCATION_POSTALCODE,address.getPostalCode());

        LocationMarkerDialog locationMarkerDialog = new LocationMarkerDialog();
        locationMarkerDialog.show(getSupportFragmentManager(),getString(R.string.location_marker_dialog_tag));

        locationMarkerDialog .setArguments(args);
        locationMarkerDialog .setCancelable(false);
    }


    private GoogleMap.OnMarkerClickListener clickMarker() {
        return new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                openDialog();
                return false;
            }
        };
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



}
