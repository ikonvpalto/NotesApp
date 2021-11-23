package org.kvpbldsck.notes.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import org.kvpbldsck.notes.R

class HelpActivity : AppCompatActivity() {
    private var helpMessage = ""
    private var helpMessageTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        try {
            val bundle = intent.extras!!
            helpMessage = bundle.getString("helpMessage", "")
            helpMessage = helpMessage.replace("\\\n", System.getProperty("line.separator")!!)
            helpMessageTitle = bundle.getString("helpMessageTitle", "")
            getHelpMessageView().text = helpMessage
            getHelpMessageHeaderView().text = helpMessageTitle
        } catch (ex: Exception) {
        }
    }

    private fun getHelpMessageHeaderView() = findViewById<TextView>(R.id.helpMessageHeaderTextView)
    private fun getHelpMessageView() = findViewById<TextView>(R.id.helpMessageTextView)

    fun closeHelp(view: View) {
        finish()
    }
}