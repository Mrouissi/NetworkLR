package fr.istic.mob.networkLR

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

    fun removeLastConnection() {
        listConnection.removeLast()
    }

    fun getListConnectedObject(): ArrayList<ConnectedObject> {
        return listConnectedObject
    }

    fun getListConnection(): ArrayList<Connection> {
        return listConnection
    }

    fun containConnection(o1: ConnectedObject, o2: ConnectedObject): Boolean {
        for (co in listConnection) {
            if (co.contains(o1, o2)) {
                return true
            }
        }
        return false
    }

    override fun toString(): String {
        return "Graph [listConnectedObject: ${this.listConnectedObject}, listConnection: ${this.listConnection}]"
    }

}