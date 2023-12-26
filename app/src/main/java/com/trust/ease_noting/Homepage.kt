package com.trust.ease_noting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class Homepage : AppCompatActivity() {
    lateinit var editText:EditText
    lateinit var btn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)
        editText = findViewById(R.id.edittext)
        btn = findViewById(R.id.meaningbtn)


        btn.setOnClickListener{

            val myfragment = word_meaning()
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainframe,myfragment)
                .commit()
            val word = editText.text.toString().trim()
            val wordapi = WordApi()
            if(word.isEmpty()){
                Toast.makeText(this,"Enter a word",Toast.LENGTH_SHORT).show()
            } else if(word.contains("\\s".toRegex())){
                Toast.makeText(this,"Enter single word only",Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    try{
                        val definition = wordapi.getWordDefinition(word)
                        withContext(Dispatchers.Main) {
                            val textView = myfragment.view?.findViewById<TextView>(R.id.meanfrag)
                            textView?.text = ("Definition of $word:\n$definition")
                        }
                    } catch (e: IOException){
                        //handling network error here
                    }
                }
            }
        }

    } //Oncreate ended here
    } //class ended here
