package fr.istic.mob.networkLR

class Graph {
    private var listConnectedObject: ArrayList<ConnectedObject> = ArrayList()

    fun addObject(touchX: Float, touchY: Float) {
        listConnectedObject.add(ConnectedObject(listConnectedObject.size.toString(), touchX, touchY))
    }

    fun removeLastObject() {
        listConnectedObject.removeLast()
    }

    fun getListConnectedObject(): ArrayList<ConnectedObject> {
        return listConnectedObject
    }

}