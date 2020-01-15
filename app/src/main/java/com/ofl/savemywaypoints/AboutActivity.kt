package com.ofl.savemywaypoints

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.app.NavUtils
import android.view.MenuItem
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_version.*

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

        // initilize the screen
        val versionTitle = findViewById<TextView>(R.id.version_swmp) as TextView
        versionTitle.setText(R.string.about_smwp);

        val versionVersion = findViewById<TextView>(R.id.version_version) as TextView
        versionVersion.setText("Version: " + R.string.about_version);

        val versionCopyright = findViewById<TextView>(R.id.version_copyright) as TextView
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
