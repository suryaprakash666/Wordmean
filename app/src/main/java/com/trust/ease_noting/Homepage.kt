package com.trust.ease_noting

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.trust.ease_noting.classes.FileSaving
import com.trust.ease_noting.classes.WordApi
import com.trust.ease_noting.fragments.word_meaning
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class Homepage : AppCompatActivity() {
    //Variable Declaration
    private lateinit var editText:EditText
    private lateinit var savebutton: MenuItem
    private lateinit var favbutton:MenuItem
    private lateinit var meanbutton:MenuItem
    private var textTosave:String = ""
    private lateinit var drawerlayout:DrawerLayout
    private lateinit var drawerToggle:ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)
        editText = findViewById(R.id.edittext)


        val mynavview =findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val mymenu = mynavview?.menu

        savebutton = mymenu!!.findItem(R.id.savepage)
        favbutton = mymenu.findItem(R.id.favourite)
        meanbutton = mymenu.findItem(R.id.meaning)

        var isFragmentOpen = false

        //Adding clickListener On BottomNavigation View's Item(Extract Meaning)
        meanbutton.setOnMenuItemClickListener {
            if (isFragmentOpen) {
                // Close the fragment and update state
                supportFragmentManager.popBackStack()
                isFragmentOpen = false
                // Update button text or appearance (e.g., "Get Meaning")
                meanbutton.title = "Get Meaning"
            } else {
                // Open the fragment, fetch word definition, and update state
                val myfragment = word_meaning()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainframe, myfragment)
                    .addToBackStack(null) // Allow back navigation
                    .commit()
                isFragmentOpen = true
                // Update button text or appearance (e.g., "Close Meaning")
                meanbutton.title = "Close Current Meaning"

                val word = editText.text.toString().trim()
                val wordapi = WordApi()

                if (word.isEmpty()) {
                    Toast.makeText(this, "Enter a word", Toast.LENGTH_SHORT).show()
                } else if (word.contains("\\s".toRegex())) {
                    Toast.makeText(this, "Enter single word only", Toast.LENGTH_SHORT).show()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val definition = wordapi.getWordDefinition(word)
                            withContext(Dispatchers.Main) {
                                val textView = myfragment.view?.findViewById<TextView>(R.id.meanfrag)
                                textView?.text = ("Definition of $word:\n$definition")
                            }
                        } catch (e: IOException) {
                            // Handling network error here
                        }
                    }
                }
            }
            true
        }


        //Adding clickListener On BottomNavigation View's Item(Savepage)
        savebutton.setOnMenuItemClickListener {
            val savetostorage = FileSaving(this, this)
            textTosave = editText.text.toString()
            savetostorage.savePageToStorage(textTosave)
            true
        }

        //Adding clickListener On BottomNavigation View's Item()
        favbutton.setOnMenuItemClickListener {
            true
        }


    } //Oncreate ended here

    @Deprecated("Deprecated")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FileSaving.REQUEST_CODE_PICK_DIRECTORY && resultCode == Activity.RESULT_OK) {

            val selectedDirectoryUri = data?.data
            if (selectedDirectoryUri != null) {
                val fileSaving = FileSaving(this, this)
                fileSaving.handleDirectorySelectionResult(selectedDirectoryUri, textTosave)
            }
        }
    }
    } //class ended here
