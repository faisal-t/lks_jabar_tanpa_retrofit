package com.example.lks_jabar_testing_kotlin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class PostUpdateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_update)

        val btnAddPost = findViewById<Button>(R.id.btn_post_edit)
        val edtName = findViewById<EditText>(R.id.edt_menu_name)
        val edtDescription = findViewById<EditText>(R.id.edt_menu_desc)
        val edtPrice = findViewById<EditText>(R.id.edt_menu_price)

        val status = intent.getStringExtra("status")

        edtName.setText(intent.getStringExtra("name") ?: null)
        edtDescription.setText(intent.getStringExtra("description") ?: null)
        edtPrice.setText(intent.getStringExtra("price") ?: null)

        val idMenu = intent.getStringExtra("id") ?: null
        val jenis = intent.getStringExtra("jenis") ?: null

        if (jenis == "post"){
            btnAddPost.setOnClickListener {
                val url = URL("http://116.193.191.179:3000/menu")

                thread {
                    with(url.openConnection() as HttpURLConnection) {
                        val name = edtName.text.toString()
                        val desc = edtDescription.text.toString()
                        val price = edtPrice.text.toString()


                        doOutput = true
                        doInput = true
                        requestMethod = "POST"
                        addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                        addRequestProperty("Authorization", MainActivity.token.toString())
                        with (outputStream.bufferedWriter()) {
                            write("name=${Uri.encode(name)}&description=${Uri.encode(desc)}&price=${Uri.encode(price)}")
                            flush()

                        }

                        if (responseCode == 200){
                            val result = inputStream.bufferedReader().readText()

                            // Fungsi Jika Berhasil
                            runOnUiThread {
                                Log.d("data", result)
                                val intent = Intent(this@PostUpdateActivity, ActivityMenu::class.java)
                                intent.putExtra("status","berhasil tambah Menu")
                                startActivity(intent)
                            }
                        }

                        else{
                            runOnUiThread{
                                Toast.makeText(this@PostUpdateActivity, "Error Harap Hubungi admin", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }


                    }
                }
            }
        }
        else {
            val idMenu = intent.getStringExtra("id")
            btnAddPost.setOnClickListener {
                val url = URL("http://116.193.191.179:3000/menu/$idMenu")

                thread {
                    with(url.openConnection() as HttpURLConnection) {

                        val name = edtName.text.toString()
                        val desc = edtDescription.text.toString()
                        val price = edtPrice.text.toString()



                        requestMethod = "PUT"
                        addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                        addRequestProperty("Authorization", MainActivity.token.toString())
                        with(outputStream.bufferedWriter()) {
                            write(
                                "name=${Uri.encode(name)}&description=${Uri.encode(desc)}&price=${
                                    Uri.encode(
                                        price
                                    )
                                }"
                            )
                            flush()

                        }
                        val result = inputStream.bufferedReader().readText()

                        // Fungsi Jika Berhasil
                        runOnUiThread {
                            Log.d("data", result)
                            val intent = Intent(this@PostUpdateActivity, ActivityMenu::class.java)
                            intent.putExtra("status", "berhasil Edit Menu")
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}