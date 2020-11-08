package fr.istic.mob.networkLR.data

import androidx.room.*
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity
data class Connection (
                      @PrimaryKey(autoGenerate = true) var id: Int,
                      @ColumnInfo(name = "id_GraphCon") var idGraphCon: Int,
                      @Embedded(prefix = "start_") var startSmartObject: SmartObject,
                      @Embedded(prefix = "end_") var endSmartObject: SmartObject,
                    ) {
}