package fr.coppernic.sample.hidiclass.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.coppernic.sample.hidiclass.App
import fr.coppernic.sample.hidiclass.R

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App.appComponents.inject(this)
    }
}
