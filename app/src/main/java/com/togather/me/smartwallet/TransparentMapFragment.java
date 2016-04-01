package com.togather.me.smartwallet;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout.LayoutParams;
import com.google.android.gms.maps.MapFragment;

public class TransparentMapFragment extends MapFragment {

    public TransparentMapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view,
                             Bundle savedInstance) {
        View layout = super.onCreateView(inflater, view, savedInstance);

        FrameLayout frameLayout = new FrameLayout(getActivity());
        frameLayout.setBackgroundColor(getResources().getColor(
                android.R.color.transparent));
        ((ViewGroup) layout).addView(frameLayout, new ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        return layout;
    }

    public static TransparentMapFragment newInstance(String abc) {
        TransparentMapFragment tsf = new TransparentMapFragment();
        return tsf;
    }
}