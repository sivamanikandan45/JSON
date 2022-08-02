package com.example.json

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn=findViewById<Button>(R.id.fetch_btn)
        btn.setOnClickListener {
            GlobalScope.launch {
                doNetworkOperation()
            }
        }
    }

    private suspend fun doNetworkOperation() {

        withContext(Dispatchers.IO){
            println("Requesting....")
            val url = "https://api.agify.io/?name=meelad"
            val connection = URL(url).openConnection() as HttpURLConnection
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            var line = reader.readLine()
            var string = ""
            while (line != null) {
                string += line
                line = reader.readLine()
            }
            if (string.isNotEmpty()) {
                println("GOt response")
                val jsonObject = JSONTokener(string).nextValue() as JSONObject
                val name = jsonObject.getString("name")
                val age = jsonObject.getString("age")
                val count = jsonObject.getString("count")
                println(name + "\t" + age + "\t" + count)
                withContext(Dispatchers.Main){
                    println("Updating UI")
                    val textView = findViewById<TextView>(R.id.textView)
                    textView.text = name + "\t" + age + "\t" + count
                }

            }
        }

    }
}