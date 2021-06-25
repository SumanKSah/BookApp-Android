package com.example.booksworld.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.booksworld.R
import com.example.booksworld.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {

    lateinit var bookName: TextView
    lateinit var bookAuthor: TextView
    lateinit var bookRating: TextView
    lateinit var bookPrice : TextView
    lateinit var bookImage : ImageView
    lateinit var bookDescription : TextView
    lateinit var progressBar: RelativeLayout
    lateinit var toolbar: Toolbar
    private var bookId : String? ="0"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        toolbar = findViewById(R.id.toolBarDescription)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"


        bookName = findViewById(R.id.txtDescriptionBookName)
        bookAuthor = findViewById(R.id.txtDescriptionAuthor)
        bookRating = findViewById(R.id.txtDescriptionRating)
        bookPrice = findViewById(R.id.txtDashBoardRecyclerPrice)
        bookImage = findViewById(R.id.imgDescriptionBook)
        bookDescription = findViewById(R.id.descriptionDetail)
        progressBar = findViewById(R.id.progressBarDescription)
        progressBar.visibility = View.VISIBLE


        if(intent != null)
        {
            bookId = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(this,"Some Unexpected error Occurred!!",Toast.LENGTH_SHORT).show()
        }
        if(bookId == "0")
        {
            finish()
            Toast.makeText(this,"Some Unexpected error Occurred!!",Toast.LENGTH_SHORT).show()
        }

        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id",bookId)

        if (ConnectionManager().checkConnectivity(this)) {

            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                try {
                    progressBar.visibility = View.GONE
                    val success = it.getBoolean("success")
                    if(success) {

                        val jsonBookData = it.getJSONObject("book_data")
                        bookName.text=jsonBookData.getString("name")
                        bookAuthor.text = jsonBookData.getString("author")
                        bookDescription.text = jsonBookData.getString("description")
                        bookPrice.text = jsonBookData.getString("price")
                        bookRating.text = jsonBookData.getString("rating")

                        Picasso.get().load(jsonBookData.getString("image")).error(R.drawable.default_book).into(bookImage)
                    } else {
                        Toast.makeText(this, "Some Json Error Occurred!!!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e : Exception) {
                    Toast.makeText(this, "Some Unexpected Error Occurred!!", Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener {
                Toast.makeText(this, "Volley Error Occurred!!", Toast.LENGTH_SHORT).show()

            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "5a411ee382252c"
                    return headers
                }

            }
            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Error")
            dialog.setMessage("No internet Connection Found!!!")
            dialog.setPositiveButton("Open Settings"){text, listener ->
                val settingIntent  = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                this.finish()
            }
            dialog.setNegativeButton("Exit"){text,listener ->
                ActivityCompat.finishAffinity(this)
            }
            dialog.create()
            dialog.show()
        }
        }

    }

