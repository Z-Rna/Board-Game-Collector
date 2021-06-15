package com.example.boardgamecollector

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Game : Serializable {
    var id: Int = 0
    var title: String? = null
    var originalTitle: String? = null
    var year: Int = 0
    var descrition: String? = null
    var orderDate: String? = null
    var addingDate: String? = null
    var cost: Int = 0
    var SCD: Int = 0
    var EAN: String? = null
    var productionCode: String? = null
    var ranking: Int = 0
    var type: String = "addition"
    var comment: String? = null
    var url: String? = null
    var bgc: Int = 0

    constructor() {
        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE).also { addingDate = it }
    }

    constructor(s: String) {
        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE).also { addingDate = it }
        this.originalTitle = s
    }

    constructor(id: Int?, title: String?, originalTitle: String?, year: Int?, description: String?, orderDate: String?, addingDate: String?,
                cost: Int?, SDC: Int?, EAN: String?, productionCode: String?, ranking: Int?, type: String?, comment: String?, url: String?, bgc: Int = 0) {
        if (id != null) {
            this.id = id
        }
        this.title = title
        this.originalTitle = originalTitle
        if (year != null) {
            this.year = year
        }
        this.descrition = description
        this.orderDate = orderDate
        this.addingDate = addingDate
        if (cost != null) {
            this.cost = cost
        }
        if (SDC != null) {
            this.SCD = SDC
        }
        this.EAN = EAN
        this.productionCode = productionCode
        if (ranking != null) {
            this.ranking = ranking
        }
        if (type != null) {
            this.type = type
        }
        this.comment = comment
        this.url = url
        this.bgc = bgc
    }

}