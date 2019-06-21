package com.SozioTech.posletics

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var drawer: DrawerLayout
    private var toolbar: Toolbar? = null
    private val ERROR_DIALOG_REQUEST = 9001
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout)
        var toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        initilizeMap()
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val lg = LatLng(51.479074, 7.218194)
        moveCamera(lg, 15F)
        Log.d("SiamakLOg:", "onMapReady")
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

            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE)
            }

        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE)
        }
    }
    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this@MapActivity)

    }
    private fun moveCamera(latlang: LatLng, zoom: Float) {
        Log.d("SiamakLog", "zoom")
        val lg1 = LatLng(51.479074, 7.218194)
        val lg2 = LatLng(51.479090, 7.218194)
        val lg3 = LatLng(51.479080, 7.218194)
        val lg4 = LatLng(51.479060, 7.218194)
        mMap?.addMarker(MarkerOptions().position(lg1))
        mMap?.addMarker(MarkerOptions().position(lg2))
        mMap?.addMarker(MarkerOptions().position(lg3))
        mMap?.addMarker(MarkerOptions().position(lg4))
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latlang, zoom))
    }
}

