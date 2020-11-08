package fr.istic.mob.networkLR.data

import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.RoomWarnings


@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)



class Graph(i: Int, arrayListOf: List<SmartObject>, arrayListOf1: List<Connection>) {
    @Embedded
    lateinit var graphEntity: GraphEntity

    @Relation(parentColumn = "id", entityColumn = "id_GraphS")
    var smartObject: ArrayList<SmartObject>? =  ArrayList()

    @Relation(parentColumn = "id", entityColumn = "id_GraphCon")
    var connection: ArrayList<Connection>? =  ArrayList()
}