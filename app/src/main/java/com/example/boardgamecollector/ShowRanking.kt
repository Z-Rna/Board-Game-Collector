package com.example.boardgamecollector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ScrollView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_show_ranking.*

class ShowRanking : AppCompatActivity() {
    lateinit var dbHandler: MyDBHandler
    var id: Int = -1
    var gbc: Int = -1
    lateinit var sV: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_ranking)
        dbHandler = MyDBHandler(this, null, null, 1)

        sV = findViewById(R.id.listRanking)

        val i = getIntent()
        id = i.getIntExtra("id", -1)
        gbc = i.getIntExtra("bgc", -1)
        setView()

    }

    fun setView() {
        val arrayRanking = dbHandler.getRankingarray(id)
        var arr = ArrayList<String>()

        for (a in arrayRanking) {
            arr.add("Position: ${a.position} \nDate: ${a.date}")
        }
        val ad = ArrayAdapter(this, android.R.layout.simple_list_item_1, arr)

        sV.adapter = ad
    }

    fun onClickUpdate(v: View) {
        search()
    }

    fun search() {
        val parser = XMLParser()
        parser.globalURL("thing?id=" + gbc.toString() + "&stats=1")
        Log.i("show url", parser.globalURL)

        parser.done.observe(this){
            if(it == true){
                val (game, dump1, dump2) = parser.parseGame(this)
                if (game == null) {
                    Thread.sleep(5)
                    search()
                }

                var ran = dbHandler.findRanking(id)
                if (ran != null && game.ranking != ran.position) {
                    dbHandler.addRanking(Ranking(id, game.ranking))
                    setView()
                    Toast.makeText(this, "New rank=${game.ranking}", Toast.LENGTH_LONG).show()
                }
                else Toast.makeText(this, "Old rank=${game.ranking}", Toast.LENGTH_LONG).show()

            }
        }

        parser.run(this)
    }
}