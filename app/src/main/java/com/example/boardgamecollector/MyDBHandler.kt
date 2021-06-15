package com.example.boardgamecollector

import android.content.Context
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import java.io.Serializable
import java.time.LocalDate

class MyDBHandler(contex: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper(contex, DATABASE_NAME, factory, DATABASE_VERSION), Serializable {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "gamesDB8.db"

        val TABLE_ARTISTS = "artists"
        val COLUMN_ID_ARTISTS = "_id"
        val COLUMN_NAME_ARTISTS = "name"

        val TABLE_DESIGNERS = "designers"
        val COLUMN_ID_DESIGNERS = "_id"
        val COLUMN_NAME_DESIGNERS = "name"

        val TABLE_LOCALIZATIONS = "localizations"
        val COLUMN_ID_GAMES_LOCALIZATIONS = "idGames"
        val COLUMN_WHERE_LOCALIZATIONS = "whereIs"


        val TABLE_GAMES = "games"
        val COLUMN_ID_GAMES = "_id"
        val COLUMN_TITLE_GAMES = "title"
        var COLUMN_ORIGINAL_TITLE_GAMES = "originalTitle"
        var COLUMN_YEAR_GAMES = "year"
        var COLUMN_DESCRIPTION_GAMES = "descriptions"
        var COLUMN_ORDER_DATE_GAMES = "orderDate"
        var COLUMN_ADDING_DATE_GAMES = "addingDate"
        var COLUMN_COST_GAMES = "cost"
        var COLUMN_SCD_GAMES = "SCD"
        var COLUMN_EAN_GAMES = "EAN"
        var COLUMN_PRODUCTION_CODE_GAMES = "productionCode"
        var COLUMN_RANKING_GAMES = "ranking"
        var COLUMN_TYPE_GAMES = "type"
        var COLUMN_COMMENT_GAMES = "comment"
        var COLUMN_PICTURE_URL_GAMES = "url"
        var COLUMN_ID_BGC = "bgc"

        var TABLE_GAMES_DESIGNERS = "gamesDesigners"
        var COLUMN_ID_GAMES_GAMES_DESIGNERS = "idGames"
        var COLUMN_ID_DESIGNERS_GAMES_DESIGNERS = "idDesigners"

        var TABLE_GAMES_ARTISTS = "gamesArtists"
        var COLUMN_ID_GAMES_GAMES_ARTISTS = "idGames"
        var COLUMN_ID_ARTISTS_GAMES_ARTISTS = "idArtists"

        var TABLE_RANKING = "ranking"
        var COLUMN_ID_GAME_RANKING = "idGame"
        var COLUMN_POSITION_RANKING = "position"
        var COLUMN_DATE_RANKING = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_ARTISTS = ("CREATE TABLE " +
                TABLE_ARTISTS + " (" +
                COLUMN_ID_ARTISTS + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME_ARTISTS + " TEXT" +
                ")")
        db!!.execSQL(CREATE_TABLE_ARTISTS)

        val CREATE_TABLE_DESIGNERS = ("CREATE TABLE " +
                TABLE_DESIGNERS + " (" +
                COLUMN_ID_DESIGNERS + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME_DESIGNERS + " TEXT" +
                ")")
        db!!.execSQL(CREATE_TABLE_DESIGNERS)

        val CREATE_GAMES_TABLE = ("CREATE TABLE " +
                TABLE_GAMES + "(" +
                COLUMN_ID_GAMES + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE_GAMES + " TEXT," +
                COLUMN_ORIGINAL_TITLE_GAMES + " TEXT," +
                COLUMN_YEAR_GAMES + " INTEGER," +
                COLUMN_DESCRIPTION_GAMES + " TEXT," +
                COLUMN_ORDER_DATE_GAMES + " TEXT," +
                COLUMN_ADDING_DATE_GAMES + " TEXT," +
                COLUMN_COST_GAMES + " INTEGER," +
                COLUMN_SCD_GAMES + " INTEGER," +
                COLUMN_EAN_GAMES + " TEXT," +
                COLUMN_PRODUCTION_CODE_GAMES + " TEXT," +
                COLUMN_RANKING_GAMES + " INTEGER," +
                COLUMN_TYPE_GAMES + " TEXT, " +
                COLUMN_COMMENT_GAMES + " TEXT, " +
                COLUMN_PICTURE_URL_GAMES + " TEXT, " +
                COLUMN_ID_BGC + " TEXT" +
                ")")
        db!!.execSQL(CREATE_GAMES_TABLE)

        val CREATE_TABLE_GAMES_DESIGNERS = ("CREATE TABLE " +
                TABLE_GAMES_DESIGNERS + "(" +
                COLUMN_ID_GAMES_GAMES_DESIGNERS + " INTEGER, " +
                COLUMN_ID_DESIGNERS_GAMES_DESIGNERS + " INTEGER, " +
                " FOREIGN KEY (" + COLUMN_ID_GAMES_GAMES_DESIGNERS + ") REFERENCES " + TABLE_GAMES + "(" + COLUMN_ID_GAMES + ")," +
                " FOREIGN KEY (" + COLUMN_ID_DESIGNERS_GAMES_DESIGNERS + ") REFERENCES " + TABLE_DESIGNERS + "(" + COLUMN_ID_DESIGNERS + ")" +
                ")")

        db!!.execSQL(CREATE_TABLE_GAMES_DESIGNERS)

        val CREATE_TABLE_TABLE_GAMES_ARTISTS = ("CREATE TABLE " +
                TABLE_GAMES_ARTISTS + " (" +
                COLUMN_ID_GAMES_GAMES_ARTISTS + " INTEGER, " +
                COLUMN_ID_ARTISTS_GAMES_ARTISTS + " INTEGER, " +
                " FOREIGN KEY (" + COLUMN_ID_GAMES_GAMES_ARTISTS + ") REFERENCES " + TABLE_GAMES + "(" + COLUMN_ID_GAMES + ")," +
                " FOREIGN KEY (" + COLUMN_ID_ARTISTS_GAMES_ARTISTS + ") REFERENCES " + TABLE_ARTISTS + "(" + COLUMN_ID_ARTISTS + ")" +
                ")")

        db!!.execSQL(CREATE_TABLE_TABLE_GAMES_ARTISTS)

        val CREATE_TABLE_LOCALIZATION = ("CREATE TABLE " +
                TABLE_LOCALIZATIONS + " (" +
                COLUMN_ID_GAMES_LOCALIZATIONS + " INTEGER, " +
                COLUMN_WHERE_LOCALIZATIONS + " TEXT, " +
                " FOREIGN KEY (" + COLUMN_ID_GAMES_LOCALIZATIONS + ") REFERENCES " + TABLE_GAMES + "(" + COLUMN_ID_GAMES + ")" +
                ")")
        db!!.execSQL(CREATE_TABLE_LOCALIZATION)

        val CREATE_TABLE_POSITION = ("CREATE TABLE " +
                TABLE_RANKING + " (" +
                COLUMN_ID_GAME_RANKING + " INTEGER, " +
                COLUMN_POSITION_RANKING + " INTEGER, " +
                COLUMN_DATE_RANKING + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                " FOREIGN KEY (" + COLUMN_ID_GAME_RANKING + ") REFERENCES " + TABLE_GAMES + "(" + COLUMN_ID_GAMES + ")" +
                ")")
        db!!.execSQL(CREATE_TABLE_POSITION)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_ARTISTS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_DESIGNERS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_RANKING")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_LOCALIZATIONS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES_ARTISTS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES_DESIGNERS")
        }
        onCreate(db)
    }

    fun delete(contex: Context) {
        contex.deleteDatabase(DATABASE_NAME)
    }

    fun addGame(game: Game) {
        val values = ContentValues()
//        if (game.id != 0) values.put(COLUMN_ID_GAMES, game.id)
        values.put(COLUMN_TITLE_GAMES, game.title)
        values.put(COLUMN_ORIGINAL_TITLE_GAMES, game.originalTitle)
        values.put(COLUMN_YEAR_GAMES, game.year)
        values.put(COLUMN_DESCRIPTION_GAMES, game.descrition)
        values.put(COLUMN_ORDER_DATE_GAMES, game.orderDate)
        values.put(COLUMN_ADDING_DATE_GAMES, game.addingDate)
        values.put(COLUMN_COST_GAMES, game.cost)
        values.put(COLUMN_SCD_GAMES, game.SCD)
        values.put(COLUMN_EAN_GAMES, game.EAN)
        values.put(COLUMN_PRODUCTION_CODE_GAMES, game.productionCode)
        values.put(COLUMN_RANKING_GAMES, game.ranking)
        values.put(COLUMN_TYPE_GAMES, game.type)
        values.put(COLUMN_COMMENT_GAMES, game.comment)
        values.put(COLUMN_PICTURE_URL_GAMES, game.url)
        values.put(COLUMN_ID_BGC, game.bgc)

        val db = this.writableDatabase
        db.insert(TABLE_GAMES, null, values)
        db.close()
    }

    fun editGame(game: Game) {
        val values = ContentValues()
//        values.put(COLUMN_ID_GAMES, game.id)
        values.put(COLUMN_TITLE_GAMES, game.title)
        values.put(COLUMN_ORIGINAL_TITLE_GAMES, game.originalTitle)
        values.put(COLUMN_YEAR_GAMES, game.year)
        values.put(COLUMN_DESCRIPTION_GAMES, game.descrition)
        values.put(COLUMN_ORDER_DATE_GAMES, game.orderDate)
        values.put(COLUMN_ADDING_DATE_GAMES, game.addingDate)
        values.put(COLUMN_COST_GAMES, game.cost)
        values.put(COLUMN_SCD_GAMES, game.SCD)
        values.put(COLUMN_EAN_GAMES, game.EAN)
        values.put(COLUMN_PRODUCTION_CODE_GAMES, game.productionCode)
        values.put(COLUMN_RANKING_GAMES, game.ranking)
        values.put(COLUMN_TYPE_GAMES, game.type)
        values.put(COLUMN_COMMENT_GAMES, game.comment)
        values.put(COLUMN_PICTURE_URL_GAMES, game.url)
        values.put(COLUMN_ID_BGC, game.bgc)

        val db = this.writableDatabase
        db.update(TABLE_GAMES, values, "$COLUMN_ID_GAMES = ?", arrayOf(game.id.toString()))
        db.close()
    }

    fun findGame(id: Int): Game? {
        val query = "select * from $TABLE_GAMES where $COLUMN_ID_BGC = $id ;"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var game: Game? = null
        if (cursor.moveToFirst()) {
            game = Game(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7),
                    cursor.getInt(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getInt(11),
                    cursor.getString(12),
                    cursor.getString(13),
                    cursor.getString(14),
                    cursor.getInt(15)
            )
        }
        db.close()
        return game
    }

    fun deleteGame(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_GAMES, "$COLUMN_ID_GAMES = ?", arrayOf(id.toString()))
        db.close()
    }

    fun getGames(sort: String = ""): ArrayList<Game> {
        val query = "select * from $TABLE_GAMES $sort;"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var gamesArray = ArrayList<Game>()
        while (cursor.move(1)) {
            var game = Game(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7),
                    cursor.getInt(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getInt(11),
                    cursor.getString(12),
                    cursor.getString(13),
                    cursor.getString(14),
                    cursor.getInt(15)
            )
            gamesArray.add(game)
        }
        db.close()
        return gamesArray
    }

    fun addDesigner(designer: Designer) {
        val values = ContentValues()
        values.put(COLUMN_ID_DESIGNERS, designer.id)
        values.put(COLUMN_NAME_DESIGNERS, designer.name)
        val db = this.writableDatabase
        db.insert(TABLE_DESIGNERS, null, values)
        db.close()
    }

    fun addGameDesigner(IDGame: Int, IDDesigner: Int) {
        val values = ContentValues()
        values.put(COLUMN_ID_GAMES_GAMES_DESIGNERS, IDGame)
        values.put(COLUMN_ID_DESIGNERS_GAMES_DESIGNERS, IDDesigner)
        val db = this.writableDatabase
        val query = "select * from $TABLE_GAMES_DESIGNERS where $COLUMN_ID_GAMES_GAMES_DESIGNERS = $IDGame and $COLUMN_ID_DESIGNERS_GAMES_DESIGNERS = $IDDesigner;"
        val cursor = db.rawQuery(query, null)
        if (!cursor.moveToFirst())
            db.insert(TABLE_GAMES_DESIGNERS, null, values)
        db.close()
    }

    fun deleteGameDesigner(idGames: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_GAMES_DESIGNERS, "$COLUMN_ID_GAMES_GAMES_DESIGNERS = ?", arrayOf(idGames.toString()))
        db.close()
    }

    fun findDesigner(id: Int): Designer? {
        val query = "select * from $TABLE_DESIGNERS where $COLUMN_ID_DESIGNERS = $id;"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var designer: Designer? = null
        if (cursor.moveToFirst()) {
            designer = Designer(
                    cursor.getInt(0),
                    cursor.getString(1 )
            )
        }
        return designer
    }

    fun addArtist(artist: Artist) {
        val values = ContentValues()
        values.put(COLUMN_ID_ARTISTS, artist.id)
        values.put(COLUMN_NAME_ARTISTS, artist.name)
        val db = this.writableDatabase
        db.insert(TABLE_ARTISTS, null, values)
        db.close()
    }

    fun addGameArtist(IDGame: Int, IDArtist: Int) {
        val values = ContentValues()
        values.put(COLUMN_ID_GAMES_GAMES_ARTISTS, IDGame)
        values.put(COLUMN_ID_ARTISTS_GAMES_ARTISTS, IDArtist)
        val db = this.writableDatabase
        val query = "select * from $TABLE_GAMES_ARTISTS where $COLUMN_ID_GAMES_GAMES_ARTISTS = $IDGame and $COLUMN_ID_ARTISTS_GAMES_ARTISTS = $IDArtist;"
        val cursor = db.rawQuery(query, null)
        if (!cursor.moveToFirst())
            db.insert(TABLE_GAMES_ARTISTS, null, values)
        db.close()
    }

    fun deleteGameArtist(idGames: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_GAMES_ARTISTS, "$COLUMN_ID_GAMES_GAMES_DESIGNERS = ?", arrayOf(idGames.toString()))
        db.close()
    }

    fun findArtist(id: Int): Artist? {
        val query = "select * from $TABLE_ARTISTS where $COLUMN_ID_ARTISTS = $id;"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var artist: Artist? = null
        if (cursor.moveToFirst()) {
            artist = Artist(
                    cursor.getInt(0),
                    cursor.getString(1 )
            )
        }
        return artist
    }

    fun findRanking(idGames: Int): Ranking? {
        val query = "select * from $TABLE_RANKING where $COLUMN_ID_GAME_RANKING = $idGames;"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var ranking: Ranking? = null
        if(cursor.moveToLast()) {
            ranking = Ranking(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getString(2)
            )
            Log.i("rank: ", cursor.getString(0) + " " +  cursor.getString(1))
        }
        db.close()
        return ranking
    }

    fun getRankingarray(idGames: Int): ArrayList<Ranking> {
        val query = "select * from $TABLE_RANKING where $COLUMN_ID_GAME_RANKING = $idGames;"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var gamesArray = ArrayList<Ranking>()
        while (cursor.move(1)) {
            gamesArray.add(Ranking(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getString(2)
            ))
        }
        db.close()
        return gamesArray
    }

    fun addRanking(ranking: Ranking) {
        val values = ContentValues()
        values.put(COLUMN_ID_GAME_RANKING, ranking.idGame)
        values.put(COLUMN_POSITION_RANKING, ranking.position)
        val db = this.writableDatabase
        db.insert(TABLE_RANKING, null, values)
        db.close()
    }

    fun deleteRanking(idGames: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_RANKING, "$COLUMN_ID_GAME_RANKING = ?", arrayOf(idGames.toString()))
        db.close()
    }

    fun findLocalization(idGames: Int): String {
        val query = "select * from $TABLE_LOCALIZATIONS where $COLUMN_ID_GAMES_LOCALIZATIONS = $idGames;"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var ret = ""
        if(cursor.moveToFirst()) {
            ret = cursor.getString(1 )
        }
        return ret
    }

    fun changeLocalization(localization: Localization) {
        val values = ContentValues()
        values.put(COLUMN_ID_GAMES_LOCALIZATIONS, localization.idGames)
        values.put(COLUMN_WHERE_LOCALIZATIONS, localization.whereIs)
        val db = this.writableDatabase

        val query = "select * from $TABLE_LOCALIZATIONS where $COLUMN_ID_GAMES_LOCALIZATIONS = ${localization.idGames};"
        val cursor = db.rawQuery(query, null)

        if(cursor.moveToFirst()) {
            db.update(TABLE_LOCALIZATIONS, values, "$COLUMN_ID_GAMES_LOCALIZATIONS = ?", arrayOf(localization.idGames.toString()))
        }
        else {
            db.insert(TABLE_LOCALIZATIONS, null, values)
        }
        db.close()
    }

    fun deleteLocalization(idGames: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_LOCALIZATIONS, "$COLUMN_ID_GAMES_LOCALIZATIONS = ?", arrayOf(idGames.toString()))
        db.close()
    }



}