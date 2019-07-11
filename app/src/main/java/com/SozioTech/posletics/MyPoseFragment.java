package com.SozioTech.posletics;

import android.content.Intent;
import android.os.AsyncTask;
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
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;


public class MyPoseFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mypos, container, false);

        String strUserId=  this.getArguments().getString(Constants.USERID);
        userId= Integer.parseInt(strUserId);
        return mView;
    }
   public int userId=0;
    private GoogleMap mMap;
    MapView mMapView;
    View mView;
    public String dataproperties = "";
    @NotNull
    public ArrayList ListOfPosModel;

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
        //  moveCamera(lg, 16F);
        dataproperties = "";
        ListOfPosModel = new ArrayList<JsonDataModelPos>();
        JsonParser jsonclass = new JsonParser();
        jsonclass.execute();
        mMap.setOnMarkerClickListener(this);

//        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//
//            }
//
//
//        });

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
                        BitmapDescriptorFactory.HUE_VIOLET
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

    @Override
    public boolean onMarkerClick(Marker marker) {

        Intent myIntent = new Intent(getContext(), EditPosActivity.class);
        myIntent.putExtra(Constants.MYPOSACTIVITY,Constants.YESORNO.YES.ordinal());
        myIntent.putExtra(Constants.TAGID,(int) marker.getTag());
        myIntent.putExtra(Constants.USERID, userId);
        startActivity(myIntent);
        return false;
    }

    private class JsonParser extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://posletics.herokuapp.com/api/pos";
            String jsonStr = sh.makeServiceCall(url);
            dataproperties = jsonStr;
            if (jsonStr != null) {
                try {
                    // Getting JSON Array node
                    JSONArray jsonArray = new JSONArray(dataproperties);
                    return jsonArray;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
            Integer itemcount = result.length();
            JSONArray hashtagArr;
            Double lat = 12.192839;
            Double lng = 12.123123;
            for (int i = 0; i < itemcount; i++) {
                JsonDataModelPos jsonDataModelPos = new JsonDataModelPos();
                try {
                    JSONObject jsonObject = (JSONObject) result.get(i);
                    jsonDataModelPos.id = (Integer) jsonObject.get("id");
                    jsonDataModelPos.user_id = (Integer) jsonObject.get("user_id");
                    jsonDataModelPos.upvotes = (Integer) jsonObject.get("upvotes");
                    jsonDataModelPos.lat = (String) jsonObject.get("lat");
                    jsonDataModelPos.lng = (String) jsonObject.get("lng");
                    jsonDataModelPos.hashtags = (JSONArray) jsonObject.get("hashtags");
                    if (jsonDataModelPos.user_id == userId) {
                        String name = "PosLetic";
                        if (jsonDataModelPos.hashtags != null && jsonDataModelPos.hashtags.length() > 0) {
                            name = (String) ((JSONObject) jsonDataModelPos.hashtags.get(0)).get("name");
                        }
                        lat = Double.parseDouble(jsonDataModelPos.lat);
                        lng = Double.parseDouble(jsonDataModelPos.lng);
                        if (jsonDataModelPos.upvotes > 1 && jsonDataModelPos.upvotes < 4) {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(name).icon(
                                    BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_YELLOW
                                    )
                            )).setTag(jsonDataModelPos.id);
                        }
                        if (jsonDataModelPos.upvotes < 1) {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(name).icon(
                                    BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_VIOLET
                                    )
                            )).setTag(jsonDataModelPos.id);
                        }
                        if (jsonDataModelPos.upvotes > 3) {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(name).icon(
                                    BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_GREEN
                                    )
                            )).setTag(jsonDataModelPos.id);
                        }
                    }
//                            mMap.addMarker(
//                                    new MarkerOptions().position(new LatLng(lat, lng))
//
//                            );
                    ListOfPosModel.add(jsonDataModelPos);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16F));
        }
    }
}
