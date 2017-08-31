package com.example.davidalaw.bsllearningtool.mFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.davidalaw.bsllearningtool.R;
import com.example.davidalaw.bsllearningtool.mModel_Controller.MainPageAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * View class to instantiate the google maps API
 */
public class RegionalMapFragment extends Fragment implements OnMapReadyCallback {

    private final String TAG = RegionalMapFragment.class.getSimpleName();

    private Class fragmentClass = null;

    private GoogleMap mGoogleMap;

    /**
     * Instantiates a new Regional map fragment.
     */
    public RegionalMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_regional_map, container, false);
        String TITLE_HANDLER = "Regional Differences";
        getActivity().setTitle(TITLE_HANDLER); //change title in toolbar

        //initialise Map fragment.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true); //Allow user to adjust Zoom controls

        //Add marker
        AddMarkers();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom
                (new LatLng(54.5210815,-4.02099609), 5))); //UK coordinates zoom map camera 5x


        //Marker click action listener - replace RegionalMapFragment with RegionalSignListFragment
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                //Open Sign List Fragment
                Fragment fragment = null;
                fragmentClass = null;
                fragmentClass = RegionalSignListFragment.class;

                try {
                    //get selected region name and pass it to RegionalSignListFragment Class
                    fragment = (Fragment) fragmentClass.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("Region", marker.getTitle());
                    fragment.setArguments(bundle);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
            }
        });
    }

    /**
     * Add markers.
     */
    private void AddMarkers() {
        MainPageAdapter mainPageAdapter = new MainPageAdapter();
        mainPageAdapter.listAllLongitude(getContext());
        mainPageAdapter.listAllLanitude(getContext());
        mainPageAdapter.listAllRegions(getContext());
        MarkerOptions options = new MarkerOptions();

        for(int i = 0; i < mainPageAdapter.getRegionCount(getContext()); i++)
        {
            LatLng pp = new LatLng(Float.valueOf(mainPageAdapter.getLongitude(i)), Float.valueOf(mainPageAdapter.getLatitudeList(i)));
            options.position(pp).title(mainPageAdapter.getRegionName(i));
            mGoogleMap.addMarker(options);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    public interface OnFragmentInteractionListener {

    }
}
