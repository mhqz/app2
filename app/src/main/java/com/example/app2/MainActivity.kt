package com.example.app2

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ie.equalit.ouinet.Config
import ie.equalit.ouinet.Ouinet
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var ouinet: Ouinet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}