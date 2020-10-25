package fr.istic.mob.networkLR

import android.graphics.Path

class Graph {
    private var listConnectedObject: ArrayList<ConnectedObject> = ArrayList()
    private var listConnection: ArrayList<Connection> = ArrayList()
    var tempConnection: Connection? = null

    fun addObject(touchX: Float, touchY: Float) {
        listConnectedObject.add(ConnectedObject(listConnectedObject.size.toString(), touchX, touchY))
    }

    fun removeLastObject() {
        listConnectedObject.removeLast()
    }

    fun getListConnectedObject(): ArrayList<ConnectedObject> {
        return listConnectedObject
    }

    fun getListConnection(): ArrayList<Connection> {
        return listConnection
    }

}