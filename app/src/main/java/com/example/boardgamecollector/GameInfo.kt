package com.example.boardgamecollector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_game_info.*

class GameInfo : AppCompatActivity() {
    lateinit var dbHandler: MyDBHandler
    lateinit var game: Game
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_info)

        game = (intent.getSerializableExtra("game") as? Game)!!
        dbHandler = MyDBHandler(this, null, null, 1)

        setInfo()
    }

    fun onClickLocalization(v: View) {
        val i = Intent(this@GameInfo, ShowLocalizations::class.java)
        i.putExtra("id", game.id)
        startActivityForResult(i, 200)
    }

    fun onClickRanking(v: View) {
        if(game.bgc > 0 && game.ranking > 0) {
            val i = Intent(this@GameInfo, ShowRanking::class.java)
            i.putExtra("id", game.id)
            i.putExtra("bgc", game.bgc)
            startActivityForResult(i, 201)
        }
        else if(game.bgc > 0 && game.ranking == 0) Toast.makeText(this, "No ranking here!", Toast.LENGTH_SHORT).show()
        else Toast.makeText(this, "No BGG key here!", Toast.LENGTH_SHORT).show()
    }

    fun setInfo() {
        textBGC.text = game.bgc.toString()
        textID.text = game.id.toString()
        textTitle.text = game.title
        textOrginalTitle.text = game.originalTitle
        textYear.text = game.year.toString()
        textDescription.text = game.descrition
        textOrderDate.text = game.orderDate
        textAddingGame.text = game.addingDate
        textCost.text = game.cost.toString()
        textSCD.text = game.SCD.toString()
        textEAN.text = game.EAN
        textProductionCode.text = game.productionCode
        textRanking.text = game.ranking.toString()
        textType.text = game.type
        textComment.text = game.comment
        Picasso.get().load(game.url).into(imageView)

        editTitle.setText(game.title)
        editOrginalTitle.setText(game.originalTitle)
        editYear.setText(game.year.toString())
        editDate.setText(game.orderDate)
        editComment.setText(game.cost.toString())
        editCost.setText(game.cost.toString())
        editSCD.setText(game.SCD.toString())
        editEAN.setText(game.EAN)
        editProductionCode.setText(game.productionCode)

        val i = if (game.type == "base") 0 else 1
        spinner.setSelection(i)
        editComment.setText(game.comment)
        editURL.setText(game.url)
    }

    fun onClickDelete(v: View) {
        deleteGameALL(game.id)
    }

    fun onClickEdit(v: View) {
        game.title = editTitle.text.toString()
        game.originalTitle = editOrginalTitle.text.toString()
        game.year = editYear.text.toString().toInt()
        game.descrition = exitDescription.text.toString()
        game.orderDate = editDate.text.toString()
        game.cost = editCost.text.toString().toInt()
        game.SCD = editSCD.text.toString().toInt()
        game.EAN = editEAN.text.toString()
        game.productionCode = editProductionCode.text.toString()
        game.type = spinner.selectedItem.toString()
        game.ranking = if (game.type != "base" || game.bgc == 0) 0 else game.ranking
        game.comment = editComment.text.toString()
        game.url = if( editURL.text.toString() == null) "" else game.url

        dbHandler.editGame(game)
        setInfo()
    }

    fun deleteGameALL(idGame: Int) {
        dbHandler.deleteGame(idGame)
        dbHandler.deleteGameDesigner(idGame)
        dbHandler.deleteGameArtist(idGame)
        dbHandler.deleteRanking(idGame)
        dbHandler.deleteLocalization(idGame)
        Toast.makeText(this, "Deleted game id=$idGame", Toast.LENGTH_SHORT).show()
        finish()
    }
}