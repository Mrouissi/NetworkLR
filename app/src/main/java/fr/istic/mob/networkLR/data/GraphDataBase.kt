package fr.istic.mob.networkLR.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [GraphEntity::class, SmartObject::class, Connection::class],exportSchema = false, version = 3)
abstract class GraphDataBase:RoomDatabase() {
    companion object {

        @Volatile private var INSTANCE: GraphDataBase? = null

        fun getInstance(context: Context): GraphDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                GraphDataBase::class.java, "graph.db")
                .fallbackToDestructiveMigration()
                .build()
    }
    abstract fun graphDao(): GraphDao


}