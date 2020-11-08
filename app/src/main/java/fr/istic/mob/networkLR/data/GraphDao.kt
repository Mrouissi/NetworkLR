package fr.istic.mob.networkLR.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import org.jetbrains.annotations.NotNull

@Dao
interface  GraphDao {
    @Transaction
    @Insert()
    fun insertGraph(graphEntity: GraphEntity)
    @Transaction
    @NotNull
    @Insert()
    fun insertSmartObject(smartObjects: ArrayList<SmartObject>)
    @NotNull
    @Transaction
    @Insert()
    fun insertConnection(connections: ArrayList<Connection>)
    fun insertGraphs(graph: GraphEntity) {
        val smarts: ArrayList<SmartObject>? = graph.smartObjects
        if (smarts != null) {
            for (i in smarts.indices) {
                smarts[i].idGraphS =graph.id
            }
        }
        if (smarts != null) {
            insertSmartObject(smarts)
        }
        insertGraph(graph)
    }

    @Query("SELECT * FROM GraphEntity WHERE id =:id")
    fun getGraph(id: Int): GraphEntity

  /*  @Query("SELECT * FROM SmartObject ")
        fun getSmartsList(): ArrayList<SmartObject>

    fun getGraphs(id: Int): GraphEntity? {
        var graph: GraphEntity = getGraph(id)
        var smarts: ArrayList<SmartObject> = getSmartsList()
        graph.smartObjects = smarts
        return graph
    }*/


   /* @Transaction
    @Query("Select * from GraphEntity")
    fun getGraphList():List<Graph>*/
    /*@Query("SELECT * FROM GraphEntity WHERE id = :id")
    fun getGraphByID(id:Int):Graph*/

}
