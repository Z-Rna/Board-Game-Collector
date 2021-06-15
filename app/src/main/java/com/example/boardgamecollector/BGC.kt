package com.example.boardgamecollector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_b_g_c.*

class BGC : AppCompatActivity() {
    lateinit var box: EditText
    lateinit var box2: EditText
    lateinit var dbHandler: MyDBHandler
    lateinit var mapValues: HashMap<String, String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_g_c)
        box = findViewById(R.id.giveNick)
        box2 = findViewById(R.id.givetTitle)
        dbHandler = MyDBHandler(this, null, null, 1)
    }

    fun onClickImport(v: View) {
        val text = box.text.toString()
        if (text != ""){
            importUser(text)
        }
    }

    fun onClickImportTitles(v: View) {
        val text = box2.text.toString()
        if(text != "") {
            search(text)
        }
    }


    fun onClickImportGame(v: View) {
        val value = spinner2.selectedItem.toString()
        val key = mapValues.filter { it.value == value }.keys.first()

        if (key == "0") {
            dbHandler.addGame(Game(box2.text.toString()))
            Toast.makeText(this, "Added new empty game", Toast.LENGTH_LONG).show()
        }
        else {
            addNewGame(key.toInt())
        }
    }


    fun search(gameName: String) {
        val parser = XMLParser()
        parser.globalURL("search?query=" + gameName + "&type=boardgame")
        Log.i("show url", parser.globalURL)

        parser.done.observe(this) {
            if (it == true) {
                val map = parser.parseFindGame(this)
                if (map == null) {
                    Thread.sleep(5)
                    search(gameName)
                }
                mapValues = map
                var adapterArray = ArrayList<String>()
                for (m in map) {
                    adapterArray.add(m.value)
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, adapterArray)
                spinner2.adapter = adapter

            }
        }

        parser.run(this)
    }

    fun importUser(user: String) {
        val parser = XMLParser()
        parser.globalURL("collection?username=" + user + "&stats=1")
        Log.i("show url", parser.globalURL)

        parser.done.observe(this) {
            if (it == true){
                val list = parser.parseUser(this)

                if (list == null){
                    Thread.sleep(5)
                    importUser(user)
                }

                for (l in list) {
                    addNewGame(l.toInt(), false)
                }
                Toast.makeText(this, "Imported user: $user", Toast.LENGTH_SHORT).show()
            }
        }

        parser.run(this)
    }

    fun addNewGame(idGame: Int, showTost: Boolean = true) {
        val parser = XMLParser()
        parser.globalURL("thing?id=" + idGame.toString() + "&stats=1")
        Log.i("show url", parser.globalURL)

        parser.done.observe(this){
            if(it == true){
                val (game, designersArray, artistsArray) = parser.parseGame(this)
                if (game == null) {
                    Thread.sleep(5)
                    addNewGame(idGame, false)
                }
                addGameToDB(game)

                addRankingToDB(game.id, game.ranking)
                addArtistsToDB(artistsArray, game.id)
                addDesignersToDB(designersArray, game.id)
                if (showTost) Toast.makeText(this, "Added game id=${game.originalTitle}", Toast.LENGTH_LONG).show()
            }
        }

        parser.run(this)
    }

    fun addGameToDB(game: Game) {
        var find = dbHandler.findGame(game.bgc)
        if(find == null ) {
            dbHandler.addGame(game)
        }
        find = dbHandler.findGame(game.bgc)
        game.id = find!!.id
    }

    fun addArtistsToDB(artists: ArrayList<Artist>, idGame: Int) {
        for (art in artists) {
            val find = dbHandler.findArtist(art.id)
            if(find == null) {
                dbHandler.addArtist(art)
                dbHandler.addGameArtist(idGame, art.id)
            }
        }
    }

    fun addDesignersToDB(designers: ArrayList<Designer>, idGame: Int) {
        for (design in designers) {
            val find = dbHandler.findDesigner(design.id)
            if(find == null) {
                dbHandler.addDesigner(design)
                dbHandler.addGameDesigner(idGame, design.id)
            }
        }
    }

    fun addRankingToDB(idGames: Int, position: Int) {
        dbHandler.addRanking(Ranking(idGames, position))
    }
}