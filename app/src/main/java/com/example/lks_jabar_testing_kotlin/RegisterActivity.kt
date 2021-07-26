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

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnRegister = findViewById<Button>(R.id.btn_register)
        val edtUsername = findViewById<EditText>(R.id.edt_username)
        val edtPassword = findViewById<EditText>(R.id.edt_password)
        val edtCOnfirmPassword = findViewById<EditText>(R.id.edt_confirm_password)

        btnRegister.setOnClickListener {
            if (edtPassword.text.toString() != edtCOnfirmPassword.text.toString()){
                Toast.makeText(this, "Password Tidak Sama", Toast.LENGTH_SHORT).show()
            }
            else{
                val url = URL("http://116.193.191.179:3000/register")

                thread {
                    with(url.openConnection() as HttpURLConnection) {
                        val username = edtUsername.text.toString()
                        val password = edtPassword.text.toString()

                        doOutput = true
                        doInput = true
                        requestMethod = "POST"
                        addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                        with (outputStream.bufferedWriter()) {
                            write("username=${Uri.encode(username)}&password=${Uri.encode(password)}")
                            flush()

                        }
                        val result = inputStream.bufferedReader().readText()

                        // Fungsi Jika Berhasil
                        runOnUiThread {
                            Log.d("data", result)
                            val intent = Intent(this@RegisterActivity,MainActivity::class.java)
                            intent.putExtra("status","berhasil Register")
                            startActivity(intent)
                        }
                    }
                }
            }

        }
    }
}