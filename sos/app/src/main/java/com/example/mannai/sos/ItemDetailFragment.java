package com.example.mannai.sos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mannai.sos.dummy.DummyContent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    Context context;
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Log.i("DBX","after click: "+mItem.dob);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {

            Log.i("DBX","view: "+mItem.dob);
            //((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.location);

            ((TextView) rootView.findViewById(R.id.age)).setText(mItem.dob);
            ((TextView) rootView.findViewById(R.id.condition)).setText(mItem.condition);
            ((TextView) rootView.findViewById(R.id.contact)).setText(mItem.contact);
            ((TextView) rootView.findViewById(R.id.blood)).setText(mItem.bloodType);
            ((TextView) rootView.findViewById(R.id.mendication)).setText(mItem.medications);
            ((TextView) rootView.findViewById(R.id.allergies)).setText(mItem.allergies);
            ((TextView) rootView.findViewById(R.id.weight)).setText(mItem.weight);
            ((TextView) rootView.findViewById(R.id.remarks)).setText(mItem.remarks);
            setMap(mItem.latitude,mItem.longitude);
        }

        return rootView;
    }

    double lat,lng;
    GoogleMap googleMap;

    void setMap(double lat,double lng ){
        this.lat=lat;
        this.lng=lng;
        //googleMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        MapsInitializer.initialize(getActivity().getApplicationContext());


               // .icon(BitmapDescriptorFactory)
                        //.fromResource(R.drawable.ic_mobileedge_navpoint)));



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        setUpMap();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+lat+","+lng+"?q="+lat+","+lng));
        startActivity(intent);
        return true;
    }

    void setUpMap(){
        this.googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat,lng)));
               // .title("MyLocation"));
        // Move the camera instantly  with a zoom of 15.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom( new LatLng( lat,lng), 15));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lng);
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                //context.startActivity(intent);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+lat+","+lng+"?q="+lat+","+lng));
                startActivity(intent);
            }
        });
        // Zoom in, animating the camera.
        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(12), 1000, null);

    }
}
