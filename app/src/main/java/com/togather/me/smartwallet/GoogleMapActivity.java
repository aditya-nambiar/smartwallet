package com.togather.me.smartwallet;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapActivity extends FragmentActivity { //implements OnMarkerClickListener, OnMarkerDragListener {

    private static LatLng place;

    private static LatLng fromPosition = null;
    private static LatLng toPosition = null;

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        addGoogleMap();
        // addLines();
        addMarkers();
    }

    private void addGoogleMap() {
        // check if we have got the googleMap already
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
           // googleMap.setOnMarkerClickListener(this);
           // googleMap.setOnMarkerDragListener(this);
        }

    }

    private void addMarkers() {
        if (googleMap != null) {

           place = new LatLng(getIntent().getDoubleExtra("latitude", 0.0), getIntent().getDoubleExtra("longitude", 0.0));


//            // a draggable marker with title and snippet
//            googleMap.addMarker(new MarkerOptions().position(TIMES_SQUARE)
//                    .title("Race Start").snippet("Race Start: 9:00 AM CST")
//                    .draggable(true));
//
//            // marker with custom color
//            googleMap.addMarker(new MarkerOptions()
//                    .position(BROOKLYN_BRIDGE)
//                    .title("First Pit Stop")
//                    .icon(BitmapDescriptorFactory
//                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//
//            // marker with opacity
//            googleMap.addMarker(new MarkerOptions().position(LOWER_MANHATTAN)
//                    .title("Second Pit Stop").snippet("Best Time: 6 Secs")
//                    .alpha(0.4f));

            // marker using custom image
            googleMap.addMarker(new MarkerOptions()
                    .position(place)
                    .title(getIntent().getStringExtra("desp"))
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.mipmap.map_amarker)));

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    place, 13));

        }
    }

    /*
    private void addLines() {
        if (googleMap != null) {
            googleMap.addPolyline((new PolylineOptions())
                    .add(TIMES_SQUARE, BROOKLYN_BRIDGE, LOWER_MANHATTAN,
                            TIMES_SQUARE).width(5).color(Color.BLUE)
                    .geodesic(true));
            // move camera to zoom on map
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    LOWER_MANHATTAN, 13));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i("GoogleMapActivity", "onMarkerClick");
        Toast.makeText(getApplicationContext(),
                "Marker Clicked: " + marker.getTitle(), Toast.LENGTH_LONG)
                .show();
        return false;
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        // do nothing during drag
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        toPosition = marker.getPosition();
        Toast.makeText(
                getApplicationContext(),
                "Marker " + marker.getTitle() + " dragged from " + fromPosition
                        + " to " + toPosition, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        fromPosition = marker.getPosition();
        Log.d(getClass().getSimpleName(), "Drag start at: " + fromPosition);
    }

    */
}