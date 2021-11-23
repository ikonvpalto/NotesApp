package org.kvpbldsck.notes.activities

import android.app.SearchManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import org.kvpbldsck.notes.R
import org.kvpbldsck.notes.db.DbManager

class AddNoteActivity : AppCompatActivity() {

    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        try {
            val bundle = intent.extras!!
            id = bundle.getInt("ID", 0)
            if (id != 0) {
                getTitleEditor().setText(bundle.getString("name"))
                getDescriptionEditor().setText(bundle.getString("des"))
            }
        } catch (ex: Exception) {
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    fun addFunc(view: View) {
        val dbManager = DbManager(this)

        val values = ContentValues()
        values.put("Title", getTitleEditor().text.toString())
        values.put("Description", getDescriptionEditor().text.toString())

        val isNoteAddedSuccessfully =
            if (id == 0) {
                dbManager.insert(values) > 0
            } else {
                val selectionArgs = arrayOf(id.toString())
                dbManager.update(values, "ID=?", selectionArgs) > 0
            }

        if (isNoteAddedSuccessfully) {
            Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(
                this, "Error adding note...",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.showHelp -> {
                val intent = Intent(this, HelpActivity::class.java)
                intent.putExtra("helpMessageTitle", getString(R.string.help_add_note_title))
                intent.putExtra("helpMessage", getString(R.string.help_add_note))
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun closeNote(view: View) {
        finish()
    }

    private fun getTitleEditor() = findViewById<EditText>(R.id.titleEditor)
    private fun getDescriptionEditor() = findViewById<EditText>(R.id.descriptionEditor)
}