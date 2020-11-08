package fr.istic.mob.networkLR.data

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(



)
data class SmartObject(
    @PrimaryKey var id: Int,
    var idGraphS: Int,
    @ColumnInfo(name = "color_name") var color: Int,
    @ColumnInfo(name = "positionX_name") var positionX: Int,
    @ColumnInfo(name = "positionY_name") var positionY: Int
){
    constructor(): this(0,0,0,0,0) {}
}