package com.example.boardgamecollector

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.size
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var dbHandler: MyDBHandler
    lateinit var SV: ScrollView
    lateinit var gamesTable: TableLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHandler = MyDBHandler(this, null, null, 1)
        SV = findViewById(R.id.SV)
        gamesTable = findViewById(R.id.tableGames)
        showCurrencies(this)
//        button.setOnClickListener {
//            importUser("ronaldsf")
//            addNewGame(102794)
//            setLocalization("HOME", 102794)
//            deleteGameALL(102794)
//            search("kawerna")
//        }
    }

    override fun onResume() {
        super.onResume()
        showCurrencies(this)
    }

    fun showCurrencies(context: Context, order: String = "order by originalTitle"){

        var gamesArray = dbHandler.getGames(order)
        var tmp = TableLayout(this)
        var id = 0
        gamesTable.removeViews(0, gamesTable.size)
        for(g in gamesArray) {
            val tv = TextView(this)
            Log.i("spr", g.ranking.toString())
            tv.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
            tv.gravity = Gravity.CENTER
            tv.setPadding(20, 15, 20, 15)
            tv.text = g.ranking.toString()

            val iv = ImageView(this)
            Picasso.get().load(g.url).into(iv)
            iv.setPadding(20, 15, 20, 15)

            val tv2 = TextView(this)
            tv2.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
            tv2.text = g.originalTitle
            tv2.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
            tv2.inputType = InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE
            val u = null

            tv2.setPadding(20, 15, 20, 15)

            val tv3 = TextView(this)
            tv3.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
            tv3.text = g.year.toString()


            val tr = TableRow(this)
            val trParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT)
            tr.setPadding(10, 0, 10, 0)
            tr.layoutParams = trParams


            tr.addView(tv)
            tr.addView(iv)
            tr.addView(tv2)
            tr.addView(tv3)
            tr.tag = id
            tr.id = id
            id ++

            tr.setOnClickListener{ v ->

                val i = Intent(this@MainActivity, GameInfo::class.java)
                i.putExtra("game", g)
                startActivityForResult(i, 100)
            }

            gamesTable.addView(tr, trParams)
        }
    }

    fun onClickDate(v: View) {
        showCurrencies(this, "order by year")
    }

    fun onClickRanking(v: View){
        showCurrencies(this, "order by ranking")
    }

    fun onClickTitle(v: View) {
        showCurrencies(this)
    }

    fun onClickBGC(v: View) {
        val i = Intent(this@MainActivity, BGC::class.java)
        startActivityForResult(i, 101)
    }

    fun onClickDelete(v: View) {
        dbHandler.delete(this)
        Toast.makeText(this, "DataBase removed", Toast.LENGTH_SHORT).show()
        dbHandler = MyDBHandler(this, null, null, 1)
        showCurrencies(this)
    }

}