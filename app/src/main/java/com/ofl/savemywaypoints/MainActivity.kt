package com.ofl.savemywaypoints

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.ActionBar
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.google.android.gms.maps.model.*
import java.lang.Exception
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

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
        setTitle(R.string.title_activity_main)

        // create database if not already existing
        mDB = WPDataDbHelper(this, null, 1)

        // check / create permissions
        getLocationPermission()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //
        // add listener to buttons:
        //

        // save wp to database
        val btSaveWP = findViewById<ImageButton>(R.id.main_bt_saveWP)
        btSaveWP.setOnClickListener {
            getDeviceLocation()
        }

        // mark wps on map
        val btMarkWP = findViewById<ImageButton>(R.id.main_bt_markWP)
        btMarkWP.setOnClickListener {
            if (mDB.getCount() == 0L) {
                Toast.makeText(this, "Sorry, no WPs to show so far", Toast.LENGTH_LONG).show()
            } else {
                markWPonMap()
            }
        }

        // mark share wps
        val btShareWP = findViewById<ImageButton>(R.id.main_bt_shareWP)
        btShareWP.setOnClickListener {
            // make sure we have some wps to export
            if (mDB.getCount() == 0L) {
                Toast.makeText(this, "Sorry, no WPs to export so far", Toast.LENGTH_LONG).show()
            } else {
                exportData()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()


        // create list of current wps
        if (id == R.id.action_listWP) {
            val intent = Intent(this, ListOfWPActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "")
            }
            startActivity(intent)
            return true
        }

        //
        // Clear DB of old wps
        //
        if (id == R.id.action_deleteWP) {
            deleteEntries()
            return true
        }

        //
        // show the version info screen
        //
        if (id == R.id.action_about) {
            val intent = Intent(this, AboutActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "")
            }
            startActivity(intent)
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
                    val mLong =  location.longitude
                    val mLat =  location.latitude

                    // create current date
                    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    formatter.timeZone = TimeZone.getTimeZone("UTC")
                    val currentDate = Date()
                    val dateStr = formatter.format(currentDate)

                    // add wp to database
                    mDB.addWP(dateStr, mLong, mLat)

                    // print current position with toast
                    val dec = DecimalFormat("#,###.000")
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
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.title_activity_main)))

        } catch (e: Exception){
            e.printStackTrace()
        }
    }
    //
    // Show current wps on map
    //
    private fun markWPonMap() {

        val wpList = mDB.getAllWPasList()
        val builder = LatLngBounds.Builder()

        for (wp in wpList) {
            val marker = MarkerOptions().position(LatLng(wp.lat, wp.lon)).title("");
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            mMap.addMarker(marker)
            builder.include(marker.position)
        }

        val bounds = builder.build()

        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels

        val padding = (height * 0.10).toInt()

        val cu = CameraUpdateFactory.newLatLngBounds(bounds,width, height, padding)

        mMap.animateCamera(cu)


    }

    private fun deleteEntries() {

        val builder = AlertDialog.Builder(this);

        builder.setTitle("Please confirm!");
        builder.setMessage("You are about to delete all current waypoints.\n\nDo you really want to proceed ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes") { dialog, which ->
            mDB.deleteAllWP()
            Toast.makeText(this, "Waypoints deleted", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No") { dialog, which ->
            Toast.makeText(this, "No Waypoints deleted :-)", Toast.LENGTH_LONG).show()
        }
        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }
}

