package com.example.boardgamecollector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_show_localizations.*

class ShowLocalizations : AppCompatActivity() {
    lateinit var dbHandler: MyDBHandler
    var id: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_localizations)
        dbHandler = MyDBHandler(this, null, null, 1)

        val i = getIntent()
        id = i.getIntExtra("id", -1)
        textView7.text = "Current localization:"
        showLoc()
    }


    fun onClickChange(v: View){
        setLocalization(getLoc.text.toString(), id)
        showLoc()
    }

    fun onClickDelete(v: View) {
        dbHandler.deleteLocalization(id)
        showLoc()
        Toast.makeText(this, "Deleted localization of id=$id", Toast.LENGTH_LONG).show()
    }

    fun showLoc() {
        val loc = dbHandler.findLocalization(id)
        textView8.text = loc
    }

    fun setLocalization(loc: String, idGame: Int) {
        var localization = Localization(idGame, loc)
        dbHandler.changeLocalization(localization)
        Toast.makeText(this, "Changed localization of id=$idGame", Toast.LENGTH_LONG).show()
    }
}