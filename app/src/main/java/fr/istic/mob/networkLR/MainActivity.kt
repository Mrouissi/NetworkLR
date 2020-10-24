package fr.istic.mob.networkLR

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.istic.mob.networkLR.data.Connection
import fr.istic.mob.networkLR.data.Graph
import fr.istic.mob.networkLR.data.SmartObject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = MainActivity::class.simpleName
        var mGraph = Graph(arrayListOf(), arrayListOf())
        var objectId = 1

    }

    var StartTouchTime: Long = 0
    var moveMode = false
    var writeMode = false

    var addConnection = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        var startX = 0
        var startY = 0
        var downX = 0
        var downY = 0
        activity_main_custom_view.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_MOVE) {
                    startX = event.x.toInt()
                    startY = event.y.toInt()
                    if (moveMode) {
                        var currentSmartObject = mGraph.smartObject?.find { smartObject ->
                            smartObject.positionX == startX || smartObject.positionX <= startX + 45
                        }
                        currentSmartObject?.let {
                            it.positionX = startX
                            it.positionY = startY
                            currentSmartObject = null
                        }
                        activity_main_custom_view.invalidate()
                    }
                    return true
                }
                if (event.action == MotionEvent.ACTION_DOWN) {
                    downX = event.x.toInt()
                    downY = event.y.toInt()
                    Log.e("down", startX.toString() + "===" + startY.toString())
                    StartTouchTime = System.currentTimeMillis()

                    return true
                }
                if (event.action == MotionEvent.ACTION_UP) {
                    val endX = event.x.toInt()
                    val endY = event.y.toInt()
                    Log.e("ACTION_UP", endX.toString() + "===" + endY.toString())

                    val time = System.currentTimeMillis() - StartTouchTime
                    if(addConnection) {
                        val firstSmartObject = mGraph.smartObject?.find { smartObject ->
                            smartObject.positionX == downX || smartObject.positionX <= downX + 45
                        }
                        val secondSmartObject = mGraph.smartObject?.find { smartObject ->
                            ((smartObject.positionX == endX || smartObject.positionX <= endX + 45)
                                    && smartObject!=firstSmartObject)
                        }
                        if (firstSmartObject != null && secondSmartObject != null) {
                            mGraph.connection?.add(Connection(firstSmartObject, secondSmartObject))
                            activity_main_custom_view.invalidate()
                        }

                    }
                    if (time > 1000) {
                        if (writeMode) {
                                val rnd = Random()
                                val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                                Log.e("createx", "$endX----$endY")
                                mGraph.smartObject?.add(SmartObject(objectId++.toString(), color, endX, endY))
                                activity_main_custom_view.invalidate()
                            }


                        }
                        return true
                    }



                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val title = item.title

        Log.i(TAG, "$title Clicked")
        if (id == R.id.action_init_network) {
            mGraph.smartObject?.clear()
            mGraph.connection?.clear()
            Toast.makeText(this, "Reset Done", Toast.LENGTH_LONG).show()
            objectId = 0
            activity_main_custom_view.invalidate()
            return true
        }
        if (id == R.id.action_add_object) {
            writeMode = !writeMode
            if (writeMode) {
                moveMode=false
                addConnection=false
                Toast.makeText(this, "Add object mode enable", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Add object mode disabled", Toast.LENGTH_LONG).show()

            }
            return true
        }
        if (id == R.id.action_add_connection) {
            addConnection = !addConnection
            if (addConnection) {
                moveMode=false
                writeMode=false
                Toast.makeText(this, "Add connection mode enable", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Add connection mode disabled", Toast.LENGTH_LONG).show()

            }
            return true
        }
        if (id == R.id.action_move) {
            moveMode = !moveMode
            if (moveMode) {
                writeMode=false
                addConnection=false
                Toast.makeText(this, "Move object mode enable", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Move object  mode disabled", Toast.LENGTH_LONG).show()

            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
