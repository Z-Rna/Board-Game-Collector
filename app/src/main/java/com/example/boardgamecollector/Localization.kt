package com.example.boardgamecollector

class Localization {
    var idGames: Int = 0
    var whereIs: String? = null

    constructor(idGames: Int, whereIs: String) {
        this.idGames = idGames
        this.whereIs = whereIs
    }
}