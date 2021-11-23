package org.kvpbldsck.notes.activities

import android.app.SearchManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import org.kvpbldsck.notes.R
import org.kvpbldsck.notes.db.DbManager
import org.kvpbldsck.notes.models.Note

class MainActivity : AppCompatActivity() {

    private var listNotes = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadQuery("%")
    }

    override fun onResume() {
        super.onResume()
        loadQuery("%")
    }

    private fun loadQuery(query: String) {
        val dbManager = DbManager(this)
        val projections = arrayOf("ID", "Title", "Description")
        val selectionArgs = arrayOf(query, query)
        val cursor = dbManager.query(projections, "Title like ? or Description like ?", selectionArgs, "Title")
        listNotes.clear()
        if (cursor.moveToFirst()) {
            do {
                val idColumnIndex = cursor.getColumnIndex("ID")
                val id = cursor.getInt(idColumnIndex)

                val titleColumnIndex = cursor.getColumnIndex("Title")
                val titleFromDb = cursor.getString(titleColumnIndex)

                val descriptionColumnIndex = cursor.getColumnIndex("Description")
                val description = cursor.getString(descriptionColumnIndex)

                listNotes.add(Note(id, titleFromDb, description))

            } while (cursor.moveToNext())
        }

        //adapter
        val myNotesAdapter = MyNotesAdapter(this, listNotes)
        //set adapter
        getNotesListView().adapter = myNotesAdapter

        //get total number of tasks from ListView
        val total = getNotesListView().count
        //actionbar
        val mActionBar = supportActionBar
        if (mActionBar != null) {
            //set to actionbar as subtitle of actionbar
            mActionBar.subtitle = "You have $total note(s) in list..."
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        //searchView
        val sv: SearchView = menu!!.findItem(R.id.app_bar_search).actionView as
                SearchView

        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                loadQuery("%$query%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadQuery("%$newText%")
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_developer_info -> {
                startActivity(Intent(this, DeveloperInfoActivity::class.java))
            }
            R.id.addNote -> {
                startActivity(Intent(this, AddNoteActivity::class.java))
            }
            R.id.showHelp -> {
                val intent = Intent(this, HelpActivity::class.java)
                intent.putExtra("helpMessageTitle", getString(R.string.help_main_title))
                intent.putExtra("helpMessage", getString(R.string.help_main))
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class MyNotesAdapter(context: Context, private var listNotesAdapter: ArrayList<Note>) : BaseAdapter() {

        private var context: Context? = context

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            //inflate layout row.xml
            val myView = layoutInflater.inflate(R.layout.row, null)
            val myNote = listNotesAdapter[position]
            myView.getTitleTextView().text = myNote.nodeName
            myView.getDescriptionTextView().text = myNote.nodeDes

            myView.getDeleteButton().setOnClickListener {
                val dbManager = DbManager(this.context!!)
                val selectionArgs = arrayOf(myNote.nodeID.toString())
                dbManager.delete("ID=?", selectionArgs)
                loadQuery("%")
            }

            myView.setOnClickListener { goToUpdateFun(myNote) }

            myView.getCopyButton().setOnClickListener {
                val title = myView.getTitleTextView().text.toString()
                val desc = myView.getDescriptionTextView().text.toString()

                val s = title + "\n" + desc
                copyTextToClipboard(s)
                Toast.makeText(this@MainActivity, "Copied...", Toast.LENGTH_SHORT).show()
            }

            myView.getShareButton().setOnClickListener {
                //get title
                val title = myView.getTitleTextView().text.toString()
                //get description
                val desc = myView.getDescriptionTextView().text.toString()
                //concatenate
                val s = title + "\n" + desc
                //share intent
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, s)
                startActivity(Intent.createChooser(shareIntent, s))
            }

            return myView
        }

        override fun getItem(position: Int): Any {
            return listNotesAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }

        private fun copyTextToClipboard(text: String) {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", text)
            clipboardManager.setPrimaryClip(clipData)
        }

    }

    private fun goToUpdateFun(myNote: Note) {
        val intent = Intent(this, AddNoteActivity::class.java)
        intent.putExtra("ID", myNote.nodeID) //put id
        intent.putExtra("name", myNote.nodeName) //ut name
        intent.putExtra("des", myNote.nodeDes) //put description
        startActivity(intent) //start activity
    }

    private fun getNotesListView() = findViewById<ListView>(R.id.notesListView)
    private fun View.getTitleTextView() = this.findViewById<TextView>(R.id.titleTextView)
    private fun View.getDescriptionTextView() = this.findViewById<TextView>(R.id.descriptionTextView)
    private fun View.getDeleteButton() = this.findViewById<ImageButton>(R.id.deleteButton)
    private fun View.getCopyButton() = this.findViewById<ImageButton>(R.id.copyButton)
    private fun View.getShareButton() = this.findViewById<ImageButton>(R.id.shareButton)
}