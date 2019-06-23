package com.SozioTech.posletics;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.gms.maps.SupportMapFragment;


public class MyPoseFragment extends Fragment implements OnMapReadyCallback {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mypos, container, false);
        return mView;
    }

    private GoogleMap mMap;
    MapView mMapView;
    View mView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.mapMyPos);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng lg = new LatLng(51.441318653573965, 7.264961193145723);
        moveCamera(lg, 16F);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent myIntent = new Intent(getContext(), EditPosActivity.class);
                startActivity(myIntent);
            }


        });
    }



    private void moveCamera(LatLng latlang, Float zoom) {
        LatLng lg4 = new LatLng(51.441417299301124, 7.2650631117088289);
        LatLng lg1 = new LatLng(51.44191383305735, 7.26578731352237);
        LatLng lg2 = new LatLng(51.44191383305731, 7.26578731352231);
        LatLng lg3 = new LatLng(51.441412277826984, 7.265814135612459);
        LatLng lg5 = new LatLng(51.44141951867437574, 7.265763173641176);
        LatLng lg6 = new LatLng(51.44287471390129, 7.268874536098451);
        LatLng lg7 = new LatLng(51.442607224091915, 7.268810163082094);
        LatLng lg8 = new LatLng(51.441151049949328, 7.268799434246034);
        LatLng lg9 = new LatLng(51.44392675075671, 7.27045720654894);
        LatLng lg10 = new LatLng(51.44462220090753, 7.27395480710436);
        LatLng lg11 = new LatLng(51.44432797329013, 7.27421229916979);
        mMap.addMarker(new MarkerOptions().position(lg8).icon(
                BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_RED
                )
                ).snippet("there is no enough verification").title("UnVerified")
        );
        mMap.addMarker(new MarkerOptions().position(lg5).icon(
                BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN
                )
                ).snippet("Thats my POS").title("AwsomePos")
        );
        mMap.addMarker(new MarkerOptions().position(lg1).icon(
                BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN
                )
                ).snippet("Thats my POS").title("AwsomePos")
        );
        mMap.addMarker(new MarkerOptions().position(lg6).icon(
                BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_YELLOW
                )
        ).snippet("thats veryfied but not enough").title("Unpublished"));
        mMap.addMarker(new MarkerOptions().position(lg10).icon(
                BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_YELLOW
                )
        ).snippet("thats veryfied but not enough").title("Unpublished"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlang, zoom));

    }
}
