package com.hanapp;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String path = "/sdcard/CSV_Files/";
    String fileName = "read_locations.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
        mMap = googleMap;

        LatLng local = new LatLng(14.556, 121.055);
        mMap.addMarker(new MarkerOptions().position(local).title("My Location"));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(local,16));

        CsvFileInOut locations_csv = new CsvFileInOut(path, fileName);
        int csv_index = Integer.parseInt(locations_csv.read_loc("index").get(0));
        for (int ind=0; ind<csv_index; ind++){
            double loc_latitude = Double.parseDouble(locations_csv.read_loc("latitude").get(ind));
            double loc_longitude = Double.parseDouble(locations_csv.read_loc("longitude").get(ind));
            String loc_name = locations_csv.read_loc("name").get(ind);
            String price = locations_csv.read_loc("price").get(ind);
            LatLng loc_coordinate = new LatLng(loc_latitude, loc_longitude);
            mMap.addMarker(new MarkerOptions().position(loc_coordinate).title(loc_name).snippet(price)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
    }
}
