package fr.istic.mob.networkLR.data

import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.RoomWarnings


@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)



data class Graph(var name:String, var smartObject: ArrayList<SmartObject>?, val connection: ArrayList<Connection>?)
