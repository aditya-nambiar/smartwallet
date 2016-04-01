package com.togather.me.smartwallet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapDialogFragment extends DialogFragment {

    private View view;

    private GoogleMap mMap;
    private double lat;
    private double lon;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity()
                );
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.map_layout, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        // Creating Full Screen
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        //initializeViews();

    }

    public  void initializeViews() {

        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the
        // map.

        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            System.out.println("activity is" + getActivity());
            mMap = ((TransparentMapFragment) getActivity().getFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            mMap.getUiSettings().setZoomControlsEnabled(false);

            if (isGoogleMapsInstalled()) {
                if (mMap != null) {
                    setUpMap();
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("installGoogleMaps");
                builder.setCancelable(false);
                builder.setPositiveButton("install", getGoogleMapsListener());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    private void setUpMap() {

        lat = 28.6100;
        lon = 77.2300;

        final LatLng position = new LatLng(lon, lat);
        mMap.clear();
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.addMarker(new MarkerOptions().position(position).snippet(""));
    }

    public boolean isGoogleMapsInstalled() {
        try {
            getActivity().getPackageManager().getApplicationInfo(
                    "com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public android.content.DialogInterface.OnClickListener getGoogleMapsListener() {
        return new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.google.android.apps.maps"));
                startActivity(intent);

                // Finish the fragment so they can't circumvent the check
                if (getActivity() != null) {
                    Fragment fragment = (getActivity().getFragmentManager()
                            .findFragmentByTag(MapFragment.class.getName()));
                    FragmentTransaction ft = getActivity().getFragmentManager()
                            .beginTransaction();
                    ft.remove(fragment);
                    ft.commitAllowingStateLoss();
                }
            }

        };
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        if (getActivity() != null) {
            try {
                Fragment fragment = (getActivity().getFragmentManager()
                        .findFragmentById(R.id.map));
                FragmentTransaction ft = getActivity().getFragmentManager()
                        .beginTransaction();
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            } catch (Exception e) {

            }
        }

    }

}