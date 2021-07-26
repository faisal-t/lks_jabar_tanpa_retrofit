package com.example.lks_jabar_testing_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class ActivityMenu : AppCompatActivity() {

    lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val btnTambahMenu = findViewById<FloatingActionButton>(R.id.fab)
        recyclerView = findViewById(R.id.rv_menu)

        refresh()

        btnTambahMenu.setOnClickListener {
            val intent = Intent(this,PostUpdateActivity::class.java)
            intent.putExtra("jenis","post")
            startActivity(intent)
        }
    }

    fun refresh() {
        val url = URL("http://116.193.191.179:3000/menu")

        thread {
            with(url.openConnection() as HttpURLConnection) {
                addRequestProperty("Authorization", MainActivity.token.toString())
                val result = inputStream.bufferedReader().readText()

                val a = JSONArray(result)

                if (responseCode == 200){
                    runOnUiThread{
                        recyclerView.adapter = ItemAdapter(this@ActivityMenu,a)
                        recyclerView.layoutManager = LinearLayoutManager(this@ActivityMenu)
                    }
                }
                else{
                    runOnUiThread {
                        Toast.makeText(this@ActivityMenu, "Error Harap Hubungi admin", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}