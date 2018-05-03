package org.wit.hillforts.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import kotlinx.android.synthetic.main.activity_hillfort.view.*
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.card_hillfort.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.*
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel
import org.wit.placemark.R

class HillfortsListActivity : AppCompatActivity(), HillfortListener {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        app = application as MainApp

        toolbarMain.title = title
        setSupportActionBar(toolbarMain)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        loadHillforts()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadHillforts()
        super.onActivityResult(requestCode, resultCode, data)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult<HillfortActivity>(AppCompatActivity.RESULT_OK)
            R.id.item_map -> startActivity<HillfortMapActivity>()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onHillfortClick(hillfort: HillfortModel) {
        startActivityForResult(intentFor<HillfortActivity>().putExtra("placemark_edit", hillfort), 201)
    }
    private fun loadHillforts() {
        async(UI) {
            showHillforts(app.hillforts.findAll())
        }
    }

    fun showHillforts (hillforts: List<HillfortModel>) {
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter.notifyDataSetChanged()
    }
    override fun onPlacemarkLongClick(placemark: HillfortModel) {
        val title = ctx.getString(R.string.dialog_title_delete)
        val message = ctx.getString(R.string.dialog_desc_delete)

        alert(message, title) {
            positiveButton(ctx.getString(android.R.string.ok)) {
                app.hillforts.delete(placemark)
                loadHillforts()
            }
            negativeButton(ctx.getString(android.R.string.no)) { }
        }.show()
    }
}