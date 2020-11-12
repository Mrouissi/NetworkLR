package fr.istic.mob.networkLR

import android.graphics.Path

class Connection(private var obj1: ConnectedObject, private var obj2: ConnectedObject): Path() {

    lateinit var name: String

    init {
        setLastPoint(obj1.x, obj1.y)
        lineTo(obj2.x, obj2.y)
    }

    fun getObj1Connection(): ConnectedObject {
        return this.obj1
    }

    fun getObj2Connection(): ConnectedObject {
        return this.obj2
    }

    fun onUpdateObj() {
        reset()
        setLastPoint(obj1.x, obj1.y)
        lineTo(obj2.x, obj2.y)
    }

    fun onDeleteObj() {
        reset()
    }

    fun contains(o1: ConnectedObject, o2: ConnectedObject): Boolean {
        return (obj1 == o1 && obj2 == o2) || (obj1 == o2 && obj2 == o1)
    }
}