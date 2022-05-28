package com.example.app2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ie.equalit.ouinet.Config
import ie.equalit.ouinet.Ouinet
import okhttp3.OkHttpClient
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var ouinet: Ouinet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val get = findViewById<Button>(R.id.get)
        get.setOnClickListener{ getURL(get) }

        var config = Config.ConfigBuilder(this)
            .setCacheType("bep5-http")
            .setTlsCaCertStorePath("file:///android_asset/cacert.pem")
            .setCacheHttpPubKey(BuildConfig.CACHE_PUB_KEY)
            .setInjectorCredentials(BuildConfig.INJECTOR_CREDENTIALS)
            .setInjectorTlsCert(BuildConfig.INJECTOR_TLS_CERT)
            .build()

        ouinet = Ouinet(this, config)
        ouinet.start()

        Executors.newFixedThreadPool(1).execute(Runnable { this.updateOuinetState() } as Runnable)
    }

    private fun updateOuinetState() {
        val ouinetState = findViewById<View>(R.id.status) as TextView
        while (true) {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            val state = ouinet.state.toString()
            runOnUiThread { ouinetState.text = "State: $state" }
        }
    }

    fun getURL(view: View?) {
        val editText = findViewById<View>(R.id.url) as EditText
        val logViewer = findViewById<View>(R.id.log_viewer) as TextView
        val url = editText.text.toString()
        val toast = Toast.makeText(this, "Loading: $url", Toast.LENGTH_SHORT)
        toast.show()

        val client: OkHttpClient = getOuinetHttpClient()
    }

    private fun getOuinetHttpClient(): OkHttpClient {
        return try {
            val builder = OkHttpClient.Builder()

            // Proxy to ouinet service
            val ouinetService = Proxy(Proxy.Type.HTTP, InetSocketAddress("127.0.0.1", 8888))
            builder.proxy(ouinetService)
            return builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}