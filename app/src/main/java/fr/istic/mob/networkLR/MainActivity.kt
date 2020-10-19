package fr.istic.mob.networkLR

import android.content.DialogInterface
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
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
    private val graph = Graph()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        graphView.setGraph(this.graph)

        /** LISTENERS **/

        graphView.setOnLongClickListener(View.OnLongClickListener {
            this.addNewObject(this.lastTouchX, this.lastTouchY)
            return@OnLongClickListener true
        })

        graphView.setOnTouchListener(View.OnTouchListener { view, ev ->
            this.lastTouchX = ev.x
            this.lastTouchY = ev.y
            view.performClick()
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

    fun reinitializeGraph(item: MenuItem) {
        graph.getListConnectedObject().clear()
        graphView.invalidate()
    }

    fun changeLanguage(item: MenuItem) {
        val newLocale: Locale
        if (Locale.getDefault().language == "fr") {
            newLocale = Locale("en")
        } else {
            newLocale = Locale("fr")
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

}