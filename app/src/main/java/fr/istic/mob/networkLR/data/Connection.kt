package fr.istic.mob.networkLR.data

import androidx.room.TypeConverters
data class Connection(var startSmartObject: SmartObject,var endSmartObject: SmartObject? , var color :Int , var name : String, var episseur : Float)
