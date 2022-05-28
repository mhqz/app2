package com.example.app2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ie.equalit.ouinet.Config

class MainActivity : AppCompatActivity() {
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
    }
}