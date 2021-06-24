package com.example.booksworld.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.volley.toolbox.Volley
import com.example.booksworld.R
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var bookName: TextView
    lateinit var bookAuthor: TextView
    lateinit var bookRating: TextView
    lateinit var bookPrice : TextView
    lateinit var bookImage : ImageView
    lateinit var bookDescription : TextView

    var bookId : String? ="0"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        bookName = findViewById(R.id.txtDescriptionBookName)
        bookAuthor = findViewById(R.id.txtDescriptionAuthor)
        bookRating = findViewById(R.id.txtDescriptionRating)
        bookPrice = findViewById(R.id.txtDashBoardRecyclerPrice)
        bookDescription = findViewById(R.id.imgDescriptionBook)
        bookDescription = findViewById(R.id.txtDescriptionAbout)

        setToolBar()

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

        //TODO complete it bhoiii


    }

    private fun setToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"
    }
}