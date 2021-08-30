 package com.dasankit40.memeshare

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


class MainActivity : AppCompatActivity() {

       var memeurl: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         loadMeme()
    }
      private fun loadMeme() {
          val loading : ProgressBar = findViewById(R.id.progress)
          loading.visibility = View.VISIBLE

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener {
               memeurl = it.getString("url")
                var image: ImageView= findViewById(R.id.memeImageView)

                Glide.with(this).load(memeurl).listener(object :RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,

                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean
                    {
                         loading.visibility= View.VISIBLE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                      loading.visibility= View.GONE
                        return false
                    }
                }).into(image)
                 },
                Response.ErrorListener {
                    Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
            }
        )

// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    fun shareMeme(view: View) {
   val intent= Intent(Intent.ACTION_SEND)
        intent.type= "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Check this meme")
      val chooser= Intent.createChooser(intent,"Share meme using...")
        startActivity(chooser)

    }

    fun nextMeme(view: View) {
     loadMeme()
    }

    fun downloadmeme(view: View) {
         val request= DownloadManager.Request(Uri.parse(memeurl))
             .setTitle("meme")
             .setDescription("Download meme")
             .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
             .setAllowedOverMetered(true)
        val dm =getSystemService(Context.DOWNLOAD_SERVICE)as DownloadManager
        dm.enqueue(request)

    }
}