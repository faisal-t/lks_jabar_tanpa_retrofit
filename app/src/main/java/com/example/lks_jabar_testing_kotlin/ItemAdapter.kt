package com.example.lks_jabar_testing_kotlin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class ItemAdapter (val activity: ActivityMenu, val menu : JSONArray) : RecyclerView.Adapter<ItemAdapter.MyViewHolder>() {



    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val txtMenuId : TextView? = itemView.findViewById(R.id.txt_menu_id)
        val txtMenuName : TextView? = itemView.findViewById(R.id.txt_menu_name)
        val txtMenuDescription : TextView? = itemView.findViewById(R.id.txt_menu_description)
        val txtMenuPrice : TextView? = itemView.findViewById(R.id.txt_menu_price)
        val btnDelete : ImageButton? = itemView.findViewById(R.id.btn_delete)
        val btnEdit : ImageButton? = itemView.findViewById(R.id.btn_edit)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemAdapter.MyViewHolder, position: Int) {
        val item = menu.getJSONObject(position)

        holder.txtMenuId?.text = item["ID"].toString()
        holder.txtMenuName?.text = item["name"].toString()
        holder.txtMenuDescription?.text = item["description"].toString()
        holder.txtMenuPrice?.text = item["price"].toString()

        //untuk button edit
        holder.btnEdit?.setOnClickListener {
            val intent = Intent(activity,PostUpdateActivity::class.java)
            intent.putExtra("id",holder.txtMenuId?.text.toString())
            intent.putExtra("name",holder.txtMenuName?.text.toString())
            intent.putExtra("description",holder.txtMenuDescription?.text.toString())
            intent.putExtra("price",holder.txtMenuPrice?.text.toString())
            intent.putExtra("jenis","update")
            activity.startActivity(intent)
        }

        //untuk button delete
        holder.btnDelete?.setOnClickListener {
            val id = item["ID"].toString()
            val url = URL("http://116.193.191.179:3000/menu/$id")

            thread {
                with(url.openConnection() as HttpURLConnection) {




                    doInput = true
                    requestMethod = "DELETE"
                    addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                    addRequestProperty("Authorization", MainActivity.token.toString())
                    with (outputStream.bufferedWriter()) {
                        flush()

                    }

                    if (responseCode == 200){
                        activity.runOnUiThread{
                            Toast.makeText(activity, "Berhasil Hapus Data", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    else{
                        activity.runOnUiThread{
                            Toast.makeText(activity, "Error Harap Hubungi admin", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }


                    activity.refresh()
                }
            }
        }
    }

    override fun getItemCount(): Int = menu.length()

}