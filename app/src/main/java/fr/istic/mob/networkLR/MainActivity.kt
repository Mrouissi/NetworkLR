package fr.istic.mob.networkLR

import android.content.DialogInterface
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Path
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    var lastTouchX = 0f
    var lastTouchY = 0f

    private val objHeight = 100f
    private val objWidth = 125f
    private val graph = Graph()

    private var modeAddObject = true
    private var modeAddConnection = false
    private var modeEdit = false


    private var obj: ConnectedObject? = null
    private var onObject = false
    private var onMove = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        graphView.setGraph(this.graph)

        /** LISTENERS **/

        graphView.setOnLongClickListener(View.OnLongClickListener {
            if (modeAddObject) {
                getClickObject()

                if (!onObject) {
                    this.addNewObject(this.lastTouchX, this.lastTouchY)
                    return@OnLongClickListener true
                } else {
                    return@OnLongClickListener false
                }
            } else {
                return@OnLongClickListener false
            }
        })

        graphView.setOnTouchListener(View.OnTouchListener { view, ev ->
            when (ev.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    this.lastTouchX = ev.x
                    this.lastTouchY = ev.y
                    if (!onMove) { obj = getClickObject() }
                }

                MotionEvent.ACTION_MOVE -> {
                    onMove = true
                    this.lastTouchX = ev.x
                    this.lastTouchY = ev.y
                    if (modeAddObject || modeEdit) {
                        this.modifyObject(obj, this.lastTouchX, this.lastTouchY)
                    }
                    if (modeAddConnection && obj != null) {
                        this.tempConnection(obj!!, this.lastTouchX, this.lastTouchY)
                    }
                }

                MotionEvent.ACTION_UP -> {
                    if (modeAddConnection) {
                        if(getClickObject() != null && !this.graph.getListConnection().contains(Connection(obj!!, getClickObject()!!))) {
                            this.graph.getListConnection().add(Connection(obj!!, getClickObject()!!))
                        }
                        graph.tempConnection = null
                    }
                    onMove = false
                    obj = null
                    onObject = false
                    view.performClick()
                }
            }

            return@OnTouchListener false
        })
    }

    private fun addNewObject(x: Float, y: Float) {
        graph.addObject(x, y)
        graphView.invalidate()
        MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
            .setView(R.layout.dialog_name_object)

            .setNeutralButton(R.string.cancel_obj) { dialog, _ ->
                graph.removeLastObject()
                graphView.invalidate()
                dialog.dismiss()
            }
            .setPositiveButton(R.string.create_obj) { dialog, _ ->
                val text= (dialog as? AlertDialog)?.findViewById<EditText>(R.id.objName)?.text?.toString()
                if (text != null && text != "") {
                    graph.getListConnectedObject().last().name = text
                }
                dialog.dismiss()
            }
            .setOnCancelListener(DialogInterface.OnCancelListener {
                graph.removeLastObject()
                graphView.invalidate()
            })
            .show()

    }

    private fun modifyObject(obj: ConnectedObject?, x: Float, y: Float) {
        when {
            x < 0 -> obj?.x = 0f
            x > graphView.width -> obj?.x = graphView.width.toFloat()
            else -> obj?.x = x
        }

        when {
            y < 0 -> obj?.y = 0f
            y > graphView.height -> obj?.y = graphView.height.toFloat()
            else -> obj?.y = y
        }

        graphView.invalidate()
    }

    private fun tempConnection(obj: ConnectedObject, x: Float, y: Float) {
        graph.tempConnection = Connection(obj, ConnectedObject("tempObj", x, y))
        graphView.invalidate()
    }

    fun reinitializeGraph(item: MenuItem) {
        graph.getListConnectedObject().clear()
        graph.getListConnection().clear()
        graph.tempConnection = null
        graphView.invalidate()
    }

    fun changeLanguage(item: MenuItem) {
        val newLocale: Locale = if (Locale.getDefault().language == "fr") {
            Locale("en")
        } else {
            Locale("fr")
        }
        Locale.setDefault(newLocale)

        val res: Resources = applicationContext.resources
        val conf: Configuration = res.configuration

        conf.apply {
            setLocale(newLocale)
            setLayoutDirection(newLocale)
        }

        applicationContext.createConfigurationContext(conf)

    }

    private fun getClickObject(): ConnectedObject?  {
        for (obj in graph.getListConnectedObject()) {
            if ((this.lastTouchX >= (obj.x - this.objWidth/2) && this.lastTouchX <= (obj.x + this.objWidth/2)) &&
                (this.lastTouchY >= (obj.y - this.objHeight/2) && this.lastTouchY <= (obj.y + this.objHeight/2))) {
                onObject = true
                return obj
            }
        }
        return null
    }


    fun setModeAddObject(item: MenuItem) {
        this.modeAddConnection = false
        this.modeEdit = false
        this.modeAddObject = true
    }

    fun setModeAddConnection(item: MenuItem) {
        this.modeAddObject = false
        this.modeEdit = false
        this.modeAddConnection = true
    }

    fun setModeEdit(item: MenuItem) {
        this.modeAddObject = false
        this.modeAddConnection = false
        this.modeEdit = true
    }

}