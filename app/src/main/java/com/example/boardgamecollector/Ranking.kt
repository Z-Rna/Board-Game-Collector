package com.example.boardgamecollector

import java.text.FieldPosition

class Ranking {
    var idGame: Int = 0
    var position: Int = 0
    var date: String? = null

    constructor(idDame: Int, position: Int, date: String? = null) {
        this.idGame = idDame
        this.position = position
        this.date = date
    }

}