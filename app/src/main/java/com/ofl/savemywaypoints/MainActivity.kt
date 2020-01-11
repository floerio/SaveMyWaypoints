package com.ofl.savemywaypoints

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val AUTHORITY = "com.ofl.savemywaypoints.fileprovider"

    private lateinit var mMap: GoogleMap

    private lateinit var mDB: WPDataDbHelper
    private var mLocationPermissionGranted: Boolean = false
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    var PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: Int = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setup google maps
        setContentView(R.layout.activity_main)

        // create database if not already existing
        mDB = WPDataDbHelper(this, null, 1)

        // check / create permissions
        getLocationPermission()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.mymenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()

        if (id == R.id.action_saveWP) {

            // get current position and save it to db
            getDeviceLocation()

            return true
        }
        if (id == R.id.action_listWP) {
            val intent = Intent(this, ListOfWPActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "SCHAU SCHAU")
            }
            startActivity(intent)

            Toast.makeText(this, "List Waypoints", Toast.LENGTH_LONG).show()
            return true
        }

        if (id == R.id.action_exportWP) {
            exportData()
            Toast.makeText(this, "Export Waypoints", Toast.LENGTH_LONG).show()
            return true
        }

        if (id == R.id.action_deleteWP) {
            mDB.deleteAllWP()
            Toast.makeText(this, "Waypoints deleted", Toast.LENGTH_LONG).show()
            return true
        }
        return super.onOptionsItemSelected(item)

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Hamburg and move the camera
        val hamburg = LatLng(53.6, 9.9)
        mMap.addMarker(MarkerOptions().position(hamburg).title("Marker in Hamburg"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hamburg))
        mMap.setMaxZoomPreference(15.0f)
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.setMyLocationEnabled(true)
        mMap.uiSettings.isMyLocationButtonEnabled = true

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                }
            }
        }
    }
    private fun getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        if (mLocationPermissionGranted) {

            mFusedLocationProviderClient.lastLocation.addOnSuccessListener {
                location : Location? ->
                if (location != null) {
                    var mLong =  location.longitude
                    var mLat =  location.latitude

                    // create current date
                    val date = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val formattedDate = date.format(formatter)
                    mDB.addWP(formattedDate, mLong, mLat)
                    val dec = DecimalFormat("#,###.00")
                    Toast.makeText(this, "WP saved: Lon: " + dec.format(mLong) + "  Lat: " + dec.format(mLat), Toast.LENGTH_LONG).show()
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(mLat, mLong)))
                }

            }
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext,ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )

        }
    }

    //
    // Let the user send the data to another application on the phone
    //
    private fun exportData() {

        val myFile = GPXFile(this)
        val wpList = mDB.getAllWPasList()
        myFile.addPoints(wpList)
        val f =  myFile.createFile()

        try {
            val contentUri = FileProvider.getUriForFile(this, AUTHORITY, f)

            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/*"
            sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.trip_book_share)))

        } catch (e: Exception){
            e.printStackTrace()
        }
    }
}

