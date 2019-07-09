package com.SozioTech.posletics;

import android.content.Context;
import android.media.Image;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditPosActivity extends AppCompatActivity implements OnMapReadyCallback {
    ListView listView;
    String mTitle[] = {"First_Hashtag", "Second_Hashtag", "Third_Hashtag"};
    String mHashtagNum[] = {"5", "9", "12"};
    private static final String APIKEY = "AIzaSyCi-INq5LUJQ75WRIpqA3eSe-1m5qogjiI";
    private int showVotes;
    private int PosId;
    Context context;
    public String dataproperties = "";
    @NotNull
    public ArrayList ListOfPosModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_edit_pos);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(APIKEY);
        }
        showVotes = getIntent().getExtras().getInt(Constants.MYPOSACTIVITY);
        PosId=getIntent().getExtras().getInt(Constants.TAGID);
        EditText editTextHashtag = (EditText) findViewById(R.id.txtHashtag);
        Button buttonAddHashtag = (Button) findViewById(R.id.btnAddHashtag);


        ListView lstHashtags = (ListView) findViewById(R.id.HashtagListView);


        if (showVotes == Constants.YESORNO.YES.ordinal()) {//if is not my pos can't create hashtag
            editTextHashtag.setVisibility(View.VISIBLE);
            buttonAddHashtag.setVisibility(View.VISIBLE);

        } else {
            editTextHashtag.setVisibility(View.INVISIBLE);
            buttonAddHashtag.setVisibility(View.INVISIBLE);


        }
        mMapView = (MapView) findViewById(R.id.mapMyPosShow);
        if (mMapView != null) {
            mMapView.onCreate(mapViewBundle);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        listView = findViewById(R.id.HashtagListView);
        //
        MyAdapter adapter = new MyAdapter(this, mTitle, mHashtagNum);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private GoogleMap mMap;
    MapView mMapView;
    View mView;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        ListOfPosModel = new ArrayList<JsonDataModelPos>();
        JsonParser jsonclass = new JsonParser();
        jsonclass.execute();
//        LatLng lg = new LatLng(51.441318653573965, 7.264961193145723);
//        mMap.addMarker(new MarkerOptions().position(lg).icon(
//                BitmapDescriptorFactory.defaultMarker(
//                        BitmapDescriptorFactory.HUE_GREEN
//                )
//        ).snippet("thats veryfied but not enough").title("Unpublished"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lg, 17F));
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rTitle[];
        String rHashtagNum[];

        MyAdapter(Context c, String title[], String[] hashNum) {
            super(c, R.layout.listview_rowitem, R.id.row_hashtag_num, title);
            this.context = c;
            this.rHashtagNum = hashNum;
            this.rTitle = title;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.listview_rowitem, parent, false);
            TextView MyTextViewNum = row.findViewById(R.id.row_hashtag_num);
            TextView MyTextViewTitle = row.findViewById(R.id.row_hashtag_title);
            ImageView imgPlus = (ImageView) row.findViewById(R.id.row_img_plus);
            ImageView imgMinus = (ImageView) row.findViewById(R.id.row_img_minus);
            if (showVotes == Constants.YESORNO.YES.ordinal()) {//if is not my pos can't create hashtag
                imgPlus.setVisibility(View.INVISIBLE);
                imgMinus.setVisibility(View.INVISIBLE);
            } else {
                imgPlus.setVisibility(View.VISIBLE);
                imgMinus.setVisibility(View.VISIBLE);
            }
            //
            MyTextViewNum.setText(rHashtagNum[position]);
            MyTextViewTitle.setText(rTitle[position]);
            return row;
        }

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
                    if (  jsonDataModelPos.id.equals(PosId)  ){

                        jsonDataModelPos.user_id = (Integer) jsonObject.get("user_id");
                        jsonDataModelPos.upvotes = (Integer) jsonObject.get("upvotes");
                        jsonDataModelPos.lat = (String) jsonObject.get("lat");
                        jsonDataModelPos.lng = (String) jsonObject.get("lng");
                        jsonDataModelPos.hashtags = (JSONArray) jsonObject.get("hashtags");
                        lat = Double.parseDouble(jsonDataModelPos.lat);
                        lng = Double.parseDouble(jsonDataModelPos.lng);
                        if (jsonDataModelPos.hashtags != null && jsonDataModelPos.hashtags.length() > 0) {
                            //name = (jsonDataModelPos.hashtags.get(0) as JSONObject).get("name") as String
                        }
                        mMap.addMarker(
                                new MarkerOptions().position(new LatLng(lat, lng))

                        );
                    }

                    ListOfPosModel.add(jsonDataModelPos);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16F));
        }
    }
}
