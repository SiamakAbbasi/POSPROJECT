package com.SozioTech.posletics

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList

class MapActivity : AppCompatActivity(), OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener,
    GoogleMap.OnMarkerClickListener {

    private lateinit var drawer: DrawerLayout
    private var toolbar: Toolbar? = null
    private val ERROR_DIALOG_REQUEST = 9001
    private val TAG = "MainActivity"
    private var locationManager: LocationManager? = null
    internal lateinit var dataproperties: String
    internal lateinit var ListOfPosModel: ArrayList<JsonDataModelPos>
    private lateinit var context: Context;
    val arrayMarkersInfo = ArrayList<String>()
    val arrayMarkers = ArrayList<Marker>()
    var selectedMarkers = ArrayList<Int>()
    var userId: Int = 0;
    lateinit var txtSearchControl: AutoCompleteTextView

    fun onButtonClick(v: View) {//GO button

        var postData = JSONObject()
        try {
            postData.put("user_id", userId)
            postData.put("route", selectedMarkers)
            SendDeviceDetails().execute("https://posletics.herokuapp.com/api/route", postData.toString())
            for (marker in arrayMarkers) {
                marker.setIcon(
                    BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_RED
                    )
                )
            }
        } catch (e: JSONException) {
            e.printStackTrace();
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        var markerId: Int = marker?.tag as Int
        var itemId = markerId
        var isnew: Boolean = true
        var selecteditem = 0;

        var count: Int = 0
        selectedMarkers.forEach {
            if (it == itemId) {
                isnew = false
                selecteditem = count;
            }
            count++
        }
        if (isnew) {
            selectedMarkers.add(markerId)
            marker.setIcon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_BLUE
                )
            )
            marker?.title=Constants.MARKERTITLE
            Toast.makeText(applicationContext, "selected", Toast.LENGTH_SHORT).show()

        } else {
            selectedMarkers.remove(markerId)
            marker.setIcon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_RED
                )
            )
            Toast.makeText(applicationContext, "deselected", Toast.LENGTH_SHORT).show()
        }

        return false
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        when (menuItem.itemId) {
            R.id.nav_mypos -> {
                var myPosFragmet = MyPoseFragment()
                var bundle = Bundle()
                bundle.putString(Constants.USERID, userId.toString())
                myPosFragmet.arguments = bundle
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    myPosFragmet
                ).commit()
            }
            R.id.nav_overview -> {
                val myIntent = Intent(baseContext, MapActivity::class.java)
                myIntent.putExtra(Constants.USERID, userId)
                startActivity(myIntent)
            }
        }

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        userId = intent.extras!!.getInt(Constants.USERID)
        context = this
        this.txtSearchControl = findViewById(R.id.txtSearch)
        txtSearchControl.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
            var itemcount: Int = ListOfPosModel?.size!!
            var lat: Double = 12.192839
            var lng: Double = 12.123123
            var jsonArray = JSONArray()
            val selectedItem = adapterView.getItemAtPosition(i).toString()
            ListOfPosModel.forEach {

                if (it.hashtags != null && it.hashtags.length() > 0) {
                    val hashtagcount: Int = it.hashtags?.length()!!
                    for (i in 0 until (hashtagcount)) {
                        var jsonObjectHashtag: JSONObject = it.hashtags.get(i) as JSONObject
                        var name: String = jsonObjectHashtag.get("name") as String
                        var iditem = 0;
                        if (name.trim().toUpperCase().equals(selectedItem.trim().toUpperCase())) {
                            iditem = it.id
                            lat = it.lat.toDouble()
                            lng = it.lng.toDouble()
                            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 18F))
                            arrayMarkers.forEach {
                                if (it.tag == iditem) {
                                    it.showInfoWindow()
                                }
                            }
                        }
                    }
                }
            }
        })
        toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar);
        toolbar?.title = "Posletic"

        drawer = findViewById(R.id.drawer_layout)

        var toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView: NavigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this)
        initilizeMap()
        val imgDeviceLocation: FloatingActionButton = findViewById(R.id.deviceLocation)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        imgDeviceLocation?.setOnClickListener {
            // Create persistent LocationManager reference
            try {
                // Request location updates
                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener);
            } catch (ex: SecurityException) {
                Log.d("myTag", "Security Exception, no location available");
            }

        }

    }

    private fun getJsonPos(url: String) {
        // get json string from url
        val url = URL("<api call>")
        var urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.setRequestMethod("GET")
        urlConnection.connect()

        if (urlConnection != null) {
            try {
            } catch (e: JSONException) {
                TODO("ConnectionError") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val lg1 = LatLng(location.latitude, location.longitude)
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(lg1, 15F))

//            thetext.setText("" + location.longitude + ":" + location.latitude);
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

    }

    fun isServiceOK(): Boolean {
        var available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this@MapActivity)
        return if (available == ConnectionResult.SUCCESS) {
            true
        } else {
            var dialog: Dialog =
                GoogleApiAvailability.getInstance().getErrorDialog(this@MapActivity, available, ERROR_DIALOG_REQUEST)
            false
        }
    }

    fun initilizeMap() {
        Log.d("SiamakLOg:", "initilizeMap")
        getLocationPermission()

        //       val intent = Intent(this@MapActivity, MapAccessActivity::class.java)
        //      startActivity(intent)
    }

    // Get Ferom MapAccessActivity
    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this@MapActivity)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        var jsonParserClass = JsonParser()
        dataproperties = "";
        ListOfPosModel = ArrayList<JsonDataModelPos>()
        jsonParserClass.execute()

        val lg = LatLng(51.441318653573965, 7.264961193145723)
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        mMap?.isMyLocationEnabled = true
        Log.d("SiamakLOg:", "onMapReady")

        mMap?.setOnMarkerClickListener(this)
        mMap?.setOnInfoWindowClickListener(
            GoogleMap.OnInfoWindowClickListener {
                val myIntent = Intent(baseContext, EditPosActivity::class.java)
                myIntent.putExtra(Constants.MYPOSACTIVITY, Constants.YESORNO.NO.ordinal)
            myIntent.putExtra(Constants.TAGID, it?.tag as Int)
                myIntent.putExtra(Constants.USERID, userId)
                startActivity(myIntent)//To change body of created functions use File | Settings | File Templates.
            }

        )
//        mMap?.setOnMapLongClickListener(GoogleMap.OnMapLongClickListener { latLng ->
//
//        })
    }

    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val LOCATION_PERMISSION_REQUEST_CODE = 1234
    private var mLocationPermissionGranted: Boolean? = false
    private var mMap: GoogleMap? = null

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.d("SiamakLOg:", "onRequestPermissionsResult")
        mLocationPermissionGranted = false
        when (requestCode) {

            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    for (i in grantResults.indices) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false
                            return
                        }
                    }

                }
                mLocationPermissionGranted = true
                //initialize our map
                Log.d("SiamakLOg:", "initMap")

            }
        }
    }

    private fun getLocationPermission() {
        Log.d("SiamakLOg:", "getLocationPermission")
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            if (ContextCompat.checkSelfPermission(
                    this.applicationContext,
                    COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mLocationPermissionGranted = true
                initMap()
                mMap?.isMyLocationEnabled = true
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE)
            }

        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE)
        }
    }


    private inner class JsonParser : AsyncTask<Void, Void, JSONArray>() {
        override fun onPostExecute(result: JSONArray?) {
            super.onPostExecute(result)
            var itemcount: Int = result?.length()!!
            var lat: Double = 12.192839
            var lng: Double = 12.123123
            var jsonArray = JSONArray()

            for (i in 0 until (itemcount)) {
                var jsonDataModelPos = JsonDataModelPos()
                var jsonObject: JSONObject = result.get(i) as JSONObject
                jsonDataModelPos.id = jsonObject.get("id") as Int
                jsonDataModelPos.user_id = jsonObject.get("user_id") as Int
                jsonDataModelPos.upvotes = jsonObject.get("upvotes") as Int
                jsonDataModelPos.lat = jsonObject.get("lat") as String
                jsonDataModelPos.lng = jsonObject.get("lng") as String
                jsonDataModelPos.hashtags = jsonObject.get("hashtags") as JSONArray
                lat = jsonDataModelPos.lat.toDouble()
                lng = jsonDataModelPos.lng.toDouble()
                var title: String = Constants.MARKERTITLE;
                if (jsonDataModelPos.hashtags != null && jsonDataModelPos.hashtags.length() > 0) {
                   // title = (jsonDataModelPos.hashtags.get(0) as JSONObject).get("name") as String
                    val hashtagcount: Int = jsonDataModelPos.hashtags?.length()!!
                    for (i in 0 until (hashtagcount)) {
                        var jsonObjectHashtag: JSONObject = jsonDataModelPos.hashtags.get(i) as JSONObject
                        var name: String = jsonObjectHashtag.get("name") as String
                        arrayMarkersInfo.add(name)
                    }
                }
                var findMarker: Marker;
                findMarker = mMap?.addMarker(
                    MarkerOptions().position(LatLng(lat, lng))
                        .title(title)
                )!!
                findMarker?.tag = jsonDataModelPos.id
                arrayMarkers.add(findMarker)
                ListOfPosModel.add(jsonDataModelPos)
            }

            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 15F))
            if (arrayMarkersInfo != null && arrayMarkersInfo.size > 0) {
                ArrayAdapter<String>(
                    context,
                    android.R.layout.simple_list_item_1,
                    arrayMarkersInfo
                ).also { adapter -> txtSearchControl.setAdapter(adapter) }
            }

        }


        internal var dataprop = ""
        internal var dataModelPoslist: List<JsonDataModelPos>? = null

        fun FetchDataPos(urladdress: String): List<JsonDataModelPos>? {

            return null
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }


        override fun doInBackground(vararg voids: Void): JSONArray? {
            try {
                var handler = HttpHandler();
                var url = "https://posletics.herokuapp.com/api/pos"
                dataprop = handler.makeServiceCall(url);
                dataproperties = dataprop;
                var jsonArray = JSONArray(dataproperties);
                return jsonArray

            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace();
            }
            return null
        }

        private fun moveCamera(latlang: LatLng, zoom: Float) {

            Log.d("SiamakLog", "zoom")


        }

        // return JSON String
    }


    private inner class SendDeviceDetails : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String {

            var data = ""

            var httpURLConnection: HttpURLConnection? = null
            try {

                httpURLConnection = URL(params[0]).openConnection() as HttpURLConnection
                httpURLConnection.requestMethod = "POST"

                httpURLConnection.doOutput = true

                val wr = DataOutputStream(httpURLConnection.outputStream)
                wr.writeBytes("PostData=" + params[1])
                wr.flush()
                wr.close()
                Log.i("STATUS", httpURLConnection.responseCode.toString())
                Log.i("MSG", httpURLConnection.responseMessage)
//                val `in` = httpURLConnection.inputStream
//                val inputStreamReader = InputStreamReader(`in`)
//
//                var inputStreamData = inputStreamReader.read()
//                while (inputStreamData != -1) {
//                    val current = inputStreamData.toChar()
//                    inputStreamData = inputStreamReader.read()
//                    data += current
//                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpURLConnection?.disconnect()
            }

            return data
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            Toast.makeText(applicationContext, "the data are sent.", Toast.LENGTH_SHORT).show()
            selectedMarkers = ArrayList<Int>()

            Log.e(
                "TAG",
                result
            ) // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }

}


