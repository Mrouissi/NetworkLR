package fr.istic.mob.networkLR

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.istic.mob.networkLR.data.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = MainActivity::class.simpleName

        var mGraph = Graph(0, arrayListOf(), arrayListOf())
        var objectId = 1
var graph = GraphEntity(0 , arrayListOf())    }

    var StartTouchTime: Long = 0
    var moveMode = false
    var writeMode = false

    var addConnection = false
    var screen = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
lateinit  var  mGraphDataBase :GraphDataBase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mGraphDataBase= GraphDataBase.getInstance(this)
        initViews()

        screen = takeScreenshotOfView(window.decorView.findViewById(android.R.id.content), 100, 100) ;
        sendEmail("mariem.rouiissi@gmail.com","test" , screen)
getdata.setOnClickListener {
    GlobalScope.launch { // launch a new coroutine in background and continue
        mGraphDataBase.graphDao().getGraph(graph.id)

    }
}
        btnShow.setOnClickListener {
            GlobalScope.launch { // launch a new coroutine in background and continue
               mGraphDataBase.graphDao().insertGraph(graph)
                mGraphDataBase.graphDao().insertSmartObject(graph.smartObjects)
                mGraphDataBase.graphDao().insertGraphs(graph)
            }

        }
        print(screen)

    }
    private fun sendEmail(recipient: String, subject: String, screen: Bitmap) {
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mIntent.putExtra("image", screen )


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

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
                        var currentSmartObject = graph.smartObjects?.find { smartObject ->
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
                    if (addConnection) {
                        val firstSmartObject = graph.smartObjects?.find { smartObject ->
                            smartObject.positionX == downX || smartObject.positionX <= downX + 45
                        }
                        val secondSmartObject = graph.smartObjects?.find { smartObject ->
                            ((smartObject.positionX == endX || smartObject.positionX <= endX + 45)
                                    && smartObject != firstSmartObject)
                        }
                        if (firstSmartObject != null && secondSmartObject != null) {
                            mGraph.connection?.add(Connection(0,0,firstSmartObject, secondSmartObject))
                            activity_main_custom_view.invalidate()
                        }

                    }
                    if (time > 1000) {
                        if (writeMode) {
                            val rnd = Random()
                            val color: Int = Color.argb(
                                255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(
                                    256
                                )
                            )
                            Log.e("createx", "$endX----$endY")
                            graph.smartObjects?.add(
                                SmartObject(
                                    objectId++,
                                    0,
                                    color,
                                    endX,
                                    endY
                                )
                            )
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
    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu.setHeaderTitle("Context Menu")
        menu.add(0, v.id, 0, "Upload")
        menu.add(0, v.id, 0, "Search")
    }

    fun takeScreenshotOfView(view: View, height: Int, width: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return bitmap

    }

}
