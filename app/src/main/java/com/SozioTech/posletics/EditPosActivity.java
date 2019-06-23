package com.SozioTech.posletics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EditPosActivity extends AppCompatActivity implements OnMapReadyCallback {
ListView listView;
    String mTitle[]={"First_Hashtag","Second_Hashtag","Third_Hashtag"};
    String mHashtagNum[]={"5","9","12"};
    private static final String APIKEY = "AIzaSyCi-INq5LUJQ75WRIpqA3eSe-1m5qogjiI";
@Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pos);
        Bundle mapViewBundle=null;
    if ( savedInstanceState!=null){
        mapViewBundle=savedInstanceState.getBundle(APIKEY);
    }
        mMapView = (MapView) findViewById(R.id.mapMyPosShow);
    if (mMapView != null) {
        mMapView.onCreate(mapViewBundle);
        mMapView.onResume();
        mMapView.getMapAsync(this);
    }

        listView =findViewById(R.id.HashtagListView);
        //
    MyAdapter adapter =new MyAdapter(this,mTitle,mHashtagNum);
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
        LatLng lg = new LatLng(51.441318653573965, 7.264961193145723);
        mMap.addMarker(new MarkerOptions().position(lg).icon(
                BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN
                )
        ).snippet("thats veryfied but not enough").title("Unpublished"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lg, 17F));
    }

    class MyAdapter extends ArrayAdapter<String>{
    Context context ;
    String rTitle[];
    String rHashtagNum[];
    MyAdapter  (Context c,String title[],String[] hashNum){
        super(c,R.layout.listview_rowitem,R.id.row_hashtag_num,title);
    this.context=c;
    this.rHashtagNum=hashNum;
    this.rTitle=title;
    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=layoutInflater.inflate(R.layout.listview_rowitem,parent,false);
            TextView MyTextViewNum=row.findViewById(R.id.row_hashtag_num);
            TextView MyTextViewTitle=row.findViewById(R.id.row_hashtag_title);

            //
            MyTextViewNum.setText(rHashtagNum[position]);
            MyTextViewTitle.setText(rTitle[position]);
            return row;
        }
    }
}
