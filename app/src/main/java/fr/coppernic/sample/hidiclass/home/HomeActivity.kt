package fr.coppernic.sample.hidiclass.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import fr.coppernic.sample.hidiclass.App
import fr.coppernic.sample.hidiclass.R
import fr.coppernic.sample.hidiclass.model.Tag
import fr.coppernic.sdk.hid.iclassProx.Card
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject




class HomeActivity : AppCompatActivity(), HomeView {

    @Inject
    lateinit var presenter: HomePresenter

    var reading = false

    lateinit var adapter: TagAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App.appComponents.inject(this)

        // Creates a vertical Layout Manager
        tag_recycler_view.layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, (tag_recycler_view.layoutManager as LinearLayoutManager).orientation)
        adapter = TagAdapter(ArrayList())
        tag_recycler_view.addItemDecoration(itemDecoration)
        tag_recycler_view.adapter = adapter
        tag_recycler_view.itemAnimator = DefaultItemAnimator()
        tag_recycler_view.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            adapter.clear()
            return true
        }

        return super.onOptionsItemSelected(item)

    }

    override fun onStart() {
        super.onStart()
        fabReadId.setOnClickListener{
            reading = if(!reading){
                presenter.read()
                fabReadId.setImageResource(R.drawable.ic_stop_white_24dp)
                true
            }else {
                presenter.stop()
                fabReadId.setImageResource(R.drawable.ic_nfc_white_24dp)
                false
            }
        }
        presenter.setUp(this)
    }

    override fun onPause() {
        super.onPause()
        if(reading){
            reading = false
            presenter.stop()
            fabReadId.setImageResource(R.drawable.ic_nfc_white_24dp)
        }
    }

    override fun displayTags(card: Card) {
        adapter.updateItem(Tag(card))
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showFab(visible: Boolean) {
        if(visible){
            fabReadId.show()
        } else{
            fabReadId.hide()
        }
    }
}
