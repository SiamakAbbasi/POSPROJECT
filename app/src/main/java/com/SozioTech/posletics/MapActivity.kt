package com.SozioTech.posletics

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var drawer: DrawerLayout
    private var toolbar: Toolbar? = null
    private val ERROR_DIALOG_REQUEST = 9001
    private val TAG = "MainActivity"
    fun onButtonClick(v: View) {
        val myIntent = Intent(baseContext, MainActivity::class.java)
        startActivity(myIntent)
    }
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
        val lg = LatLng(51.441318653573965, 7.264961193145723)
        moveCamera(lg, 16F)
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
        val lg1 = LatLng(51.44191383305735, 7.26578731352237)
        val lg2 = LatLng(51.44191383305731, 7.26578731352231)
        val lg3 = LatLng(51.441412277826984, 7.265814135612459)
        val lg4 = LatLng(51.441417299301124, 7.2650631117088289)
        val lg5 = LatLng(51.44141951867437574, 7.265763173641176)
        val lg6 = LatLng(51.44287471390129, 7.268874536098451)
        val lg7 = LatLng(51.442607224091915, 7.268810163082094)
        val lg8 = LatLng(51.441151049949328, 7.268799434246034)
        val lg9 = LatLng(51.44392675075671, 7.27045720654894)
        val lg10 = LatLng(51.44462220090753, 7.27395480710436)
        val lg11 = LatLng(51.44432797329013, 7.27421229916979)
        mMap?.addMarker(
            MarkerOptions().position(latlang).icon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_YELLOW
                )
            ).title("MyPos")
        )
        mMap?.addMarker(MarkerOptions().position(lg1))
        mMap?.addMarker(MarkerOptions().position(lg2))
        mMap?.addMarker(MarkerOptions().position(lg3))
        mMap?.addMarker(MarkerOptions().position(lg4))
        mMap?.addMarker(MarkerOptions().position(lg5))
        mMap?.addMarker(MarkerOptions().position(lg6).icon(
            BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_YELLOW
            )
        ).title("MyPos"))
        mMap?.addMarker(MarkerOptions().position(lg7))
        mMap?.addMarker(MarkerOptions().position(lg8))
        mMap?.addMarker(MarkerOptions().position(lg9))
        mMap?.addMarker(MarkerOptions().position(lg10).icon(
            BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_YELLOW
            )
        ).title("MyPos"))
        mMap?.addMarker(MarkerOptions().position(lg11))
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latlang, zoom))

    }
    }

