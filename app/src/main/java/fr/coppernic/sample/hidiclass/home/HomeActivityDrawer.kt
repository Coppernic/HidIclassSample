package fr.coppernic.sample.hidiclass.home

import android.app.AlarmManager
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import fr.coppernic.sample.hidiclass.App
import fr.coppernic.sample.hidiclass.R
import fr.coppernic.sample.hidiclass.model.Tag
import fr.coppernic.sample.hidiclass.settings.SettingsActivity
import fr.coppernic.sdk.hid.iclassProx.Card
import kotlinx.android.synthetic.main.activity_main_drawer.*
import kotlinx.android.synthetic.main.app_bar_main_activity_drawer.*
import kotlinx.android.synthetic.main.content_main_activity_drawer.*
import javax.inject.Inject

class HomeActivityDrawer : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, HomeView {

    @Inject
    lateinit var presenter: HomePresenter

    var reading = false

    lateinit var adapter: TagAdapter

    private var tg: ToneGenerator? = null
    private var alarmManager: AlarmManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_drawer)
        setSupportActionBar(toolbar)

        App.appComponents.inject(this)

        fab.setOnClickListener{
            reading = if(!reading){
                presenter.read()
                fab.setImageResource(R.drawable.ic_stop_white_24dp)
                true
            }else {
                presenter.stop()
                fab.setImageResource(R.drawable.ic_nfc_white_24dp)
                false
            }
        }


        tg = ToneGenerator(AudioManager.STREAM_SYSTEM,
                ToneGenerator.MAX_VOLUME - ToneGenerator.MAX_VOLUME * 10 / 100)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        // Creates a vertical Layout Manager
        tag_recycler_view.layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, (tag_recycler_view.layoutManager as LinearLayoutManager).orientation)
        adapter = TagAdapter(ArrayList())
        tag_recycler_view.addItemDecoration(itemDecoration)
        tag_recycler_view.adapter = adapter
        tag_recycler_view.itemAnimator = DefaultItemAnimator()
        tag_recycler_view.adapter = adapter
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_clear -> {
                adapter.clear()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onStart() {
        super.onStart()
        presenter.setUp(this)
    }

    override fun onPause() {
        super.onPause()
        if(reading){
            reading = false
            presenter.stop()
            fab.setImageResource(R.drawable.ic_nfc_white_24dp)
        }
    }

    override fun onDestroy() {
        tg?.release()
        tg = null
        super.onDestroy()
    }

    override fun displayTags(card: Card) {
        adapter.updateItem(Tag(card))
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showFab(visible: Boolean) {
        if(visible){
            fab.show()
        } else{
            fab.hide()
        }
    }

    override fun playSound() {
        tg?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
    }
}
