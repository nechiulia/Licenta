package com.example.teammanagement.activities;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.NewLocation;
import com.example.teammanagement.database.JDBCController;
import com.example.teammanagement.dialogs.LocationMarkerDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ImageButton ibtn_back;
    private ImageView iv_search;
    private AutoCompleteTextView et_searchText;

    private static final String TAG = "MapsActivity";

    private JDBCController jdbcController;
    private Connection c;

    private Map<Integer,String> program = new HashMap<>();
    private List<NewLocation> listLocations = new ArrayList<>();
    private Address address;

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
        jdbcController= JDBCController.getInstance();
        c=jdbcController.openConnection();

        ibtn_back=findViewById(R.id.maps_ibtn_back);
        et_searchText=findViewById(R.id.maps_et_searchBar);
        iv_search=findViewById(R.id.maps_iv_searchIcon);

        et_searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE){
                    hideKeyboard(MapsActivity.this);
                    geoLocate();
                }
                return false;
            }
        });

        ibtn_back.setOnClickListener(clickBack());
        iv_search.setOnClickListener(clickSearchIcon());

        selectLocations();

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
                    Constants.DEFAULT_ZOOM);
            LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
            addMarkerFunction(latLng);
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
        moveCamera(new LatLng(bucharest.latitude,bucharest.longitude), 11F);
        for(NewLocation newLocation:listLocations){
            LatLng latLng = new LatLng(newLocation.getLatitude(),newLocation.getLongitude());
            addMarkerFunction(latLng);
        }
        mMap.setOnMarkerClickListener(clickMarker());
    }

    private void moveCamera(final LatLng latLng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        MarkerOptions options = new MarkerOptions().position(latLng);
        mMap.addMarker(options);
    }

    public void addMarkerFunction(LatLng latLng){
        MarkerOptions options = new MarkerOptions().position(latLng);
        mMap.addMarker(options);
    }

    public void selectLocations(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT * FROM LOCATII WHERE STARE=0")){
                while(r.next()){
                    NewLocation currentLocation = new NewLocation();
                    currentLocation.setLocationID(r.getInt(1));
                    currentLocation.setLocationName(r.getString(2));
                    currentLocation.setPostalCode(r.getString(3));
                    currentLocation.setAddress(r.getString(4));
                    currentLocation.setLatitude(r.getDouble(5));
                    currentLocation.setLongitude(r.getDouble(6));
                    currentLocation.setReservation(r.getByte(7));
                    currentLocation.setState(r.getInt(8));
                    currentLocation.setUserID(r.getInt(9));
                    listLocations.add(currentLocation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private void openDialog(){
        Bundle args = new Bundle();
        args.putSerializable(Constants.MAP_LONGITUDE,address.getLongitude());
        args.putSerializable(Constants.MAP_LATITUDE,address.getLatitude());

        LocationMarkerDialog locationMarkerDialog = new LocationMarkerDialog();
        locationMarkerDialog.show(getSupportFragmentManager(),getString(R.string.location_marker_dialog_tag));

        locationMarkerDialog .setArguments(args);
        locationMarkerDialog .setCancelable(false);
        address=null;
    }


    private GoogleMap.OnMarkerClickListener clickMarker() {
        return new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                hideKeyboard(MapsActivity.this);
                if(address!= null) {
                    openDialog();
                }
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private View.OnClickListener clickSearchIcon() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_searchText.getText().length() > 0){
                    hideKeyboard(MapsActivity.this);
                    geoLocate();
                }
            }
        };
    }


}
