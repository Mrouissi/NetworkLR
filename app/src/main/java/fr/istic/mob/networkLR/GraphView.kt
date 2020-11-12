package fr.istic.mob.networkLR

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View

class GraphView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var graph: Graph = Graph()
    private val paintObj: Paint = Paint()
    private val paintEtiquette: Paint = Paint()
    private val paintText: Paint = Paint()
    private val paintConnection: Paint = Paint()
    private val objHeight = 75f
    private val objWidth = 75f

    private val etqMargin = 10f

    override fun onDraw(canvas: Canvas) {

        paintObj.apply {
            color = Color.RED
            style = Paint.Style.FILL
        }

        paintEtiquette.apply {
            color = Color.DKGRAY
            style = Paint.Style.FILL
        }

        paintText.apply {
            color = Color.WHITE
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
            textSize = 50f
        }

        paintConnection.apply {
            color = Color.GREEN
            style = Paint.Style.STROKE
            strokeWidth = 5f
        }

        for (obj in this.graph.getListConnectedObject()) {
            val rectF = this.connectedObjectToRectF(obj)
            val etiquette = this.createEtiquetteObj(obj.name, rectF)
            canvas.drawRoundRect(rectF, 10f, 10f, paintObj)
            canvas.drawRect(etiquette, paintEtiquette)
            canvas.drawText(
                obj.name,
                etiquette.centerX(),
                etiquette.centerY()+etiquette.height()/2-etqMargin,
                paintText
            )
        }

        for (connection in this.graph.getListConnection()) {
            canvas.drawLine(
                connection.getObj1Connection().x, connection.getObj1Connection().y,
                connection.getObj2Connection().x, connection.getObj2Connection().y,
                paintConnection
            )
            val etiquette = this.createEtiquetteConnection(connection)
            canvas.drawRect(etiquette, paintEtiquette)
            canvas.drawText(
                connection.name,
                etiquette.centerX(),
                etiquette.centerY()+etiquette.height()/2-etqMargin,
                paintText
            )

        }

        if (graph.tempConnection != null) {
            canvas.drawPath(graph.tempConnection!!, paintConnection)
        }

        super.onDraw(canvas)

    }

    private fun connectedObjectToRectF(co: ConnectedObject): RectF {
        return RectF(
            co.x - this.objWidth / 2,
            co.y - this.objHeight / 2,
            co.x + this.objWidth / 2,
            co.y + this.objHeight / 2
        )
    }

    private fun createEtiquetteObj(str: String, rectF: RectF): RectF {
        val bound = Rect()
        paintText.getTextBounds(str, 0, str.length, bound)
        val height = bound.height()
        val width = bound.width()
        val x = rectF.centerX()-width/2
        val y = rectF.centerY()+rectF.height()/2

        return RectF(x-etqMargin*2, y-etqMargin, x+width+etqMargin*2, y+height+etqMargin)
    }

    private fun createEtiquetteConnection(connection: Connection): RectF {
        val bound1 = Rect()
        val bound2 = RectF()
        paintText.getTextBounds(connection.name, 0, connection.name.length, bound1)
        connection.computeBounds(bound2, false)
        val height = bound1.height()
        val width = bound1.width()
        val x = bound2.centerX()
        val y = bound2.centerY()
        return RectF(x-width/2-etqMargin, y-height/2-etqMargin, x+width/2+etqMargin, y+height/2+etqMargin)
    }

    fun setGraph(graph: Graph) {
        this.graph = graph
    }
}