package com.meteo

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.io.IOException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.JsonReader
import java.io.StringReader

class MainActivity : AppCompatActivity() {

    lateinit var swipeContainer: SwipeRefreshLayout
    lateinit var adapter: MyItemRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swipeContainer = findViewById(R.id.swipeContainer)
        var recyclerView = findViewById<RecyclerView>(R.id.rvItems)


        swipeContainer.setOnRefreshListener {
            //requête quand on rafrait ( pousse vers le haut ) le swipeContainer
            AsyncRequete().run()
            //fin de l'anim pour le rafraichissement
            swipeContainer.setRefreshing(false)
        }

        //affichage de la view (item) dans activity_main
        adapter = MyItemRecyclerViewAdapter(this)
        recyclerView.setLayoutManager( LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // parametres pour les couleurs ( anim de rafraichissement )
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
    }


}
class  AsyncRequete() {
    private  val client = OkHttpClient()

    fun run() {
        val request = Request.Builder()
                .url("http://192.168.0.16") //ip locale
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException(" $response")

                    //headers

                    //TODO afficher une notif en cas d'erreur

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    //corps de la reponse en json
                    val  rep = response.body!!.string()
                    var temp: String? = null
                    var pression: String? = null
                    var humid: String? = null
                    var alti: String? = null

                    //parse du json
                    JsonReader(StringReader(rep)).use { reader ->
                        reader.beginObject() {
                            temp
                            pression
                            humid
                            alti


                        }

                        //remplacement du texte dans les textviews
                        val temperXML = (MyItemRecyclerViewAdapter(MainActivity()) as Activity).findViewById<TextView>(R.id.temper)
                        val pressionXML = (MyItemRecyclerViewAdapter(MainActivity()) as Activity).findViewById<TextView>(R.id.pression)
                        val humidXML = (MyItemRecyclerViewAdapter(MainActivity()) as Activity).findViewById<TextView>(R.id.humid)
                        val altiXML = (MyItemRecyclerViewAdapter(MainActivity()) as Activity).findViewById<TextView>(R.id.altitude)
                        temperXML.setText(temp)
                        pressionXML.setText(pression)
                        humidXML.setText(humid)
                        altiXML.setText(alti)
                    }
                }
            }
        })
    }

}