package org.kvpbldsck.notes.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import org.kvpbldsck.notes.R

class DeveloperInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer_info)

        val mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar.title = "Developer"
        }
    }

    fun close(view: View) {
        finish()
    }
}