package fr.istic.mob.networkLR

class Connection(obj1: ConnectedObject, obj2: ConnectedObject) {

    private var obj1: ConnectedObject = obj1
    private var obj2: ConnectedObject = obj2

    fun getObj1Connection(): ConnectedObject {
        return this.obj1
    }

    fun getObj2Connection(): ConnectedObject {
        return this.obj2
    }
}