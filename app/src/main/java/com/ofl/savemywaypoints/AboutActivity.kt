package com.ofl.savemywaypoints

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.app.NavUtils
import android.view.MenuItem
import android.widget.TextView

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class AboutActivity : AppCompatActivity() {
    private val mHideHandler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_about)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // initialize the screen
        val versionTitle = findViewById<TextView>(R.id.about_swmp) as TextView
        versionTitle.setText(R.string.about_smwp);

        val versionVersion = findViewById<TextView>(R.id.about_version) as TextView
        val versionName = BuildConfig.VERSION_NAME
        versionVersion.setText("Version: " + versionName);

        val versionCopyright = findViewById<TextView>(R.id.about_copyright) as TextView
        versionCopyright.setText(R.string.about_copyright);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
