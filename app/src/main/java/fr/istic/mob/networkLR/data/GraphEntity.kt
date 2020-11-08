package fr.istic.mob.networkLR.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings

@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity
data class  GraphEntity (
    @PrimaryKey var id: Int,
    @Ignore
   var  smartObjects: ArrayList<SmartObject>
){
    constructor(): this(0, ArrayList()) {}
}