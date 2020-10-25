package fr.istic.mob.networkLR

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class GraphView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var graph: Graph = Graph()
    private val paintObj: Paint = Paint()
    private val paintText: Paint = Paint()
    private val paintConnection: Paint = Paint()
    private val objHeight = 75f
    private val objWidth = 100f

    override fun onDraw(canvas: Canvas) {
        paintObj.apply {
            color = Color.RED
            style = Paint.Style.FILL
        }
        paintText.apply {
            color = Color.BLACK
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
            textSize = 50f
        }

        paintConnection.apply {
            color = Color.GREEN
            style = Paint.Style.FILL
            strokeWidth = 5f
        }

        for (obj in this.graph.getListConnectedObject()) {
            val rectF = this.connectedObjectToRectF(obj)
            canvas.drawRoundRect(rectF, 10f, 10f, paintObj)
            canvas.drawText(obj.name, rectF.centerX(), rectF.centerY(), paintText)
        }

        for (connection in this.graph.getListConnection()) {
            canvas.drawLine(connection.getObj1Connection().x, connection.getObj1Connection().y,
                connection.getObj2Connection().x, connection.getObj2Connection().y, paintConnection)
        }

//        canvas.drawPath(graph.tempConnection, paintConnection)
        if (graph.tempConnection != null) {
            canvas.drawLine(
                graph.tempConnection!!.getObj1Connection().x,
                graph.tempConnection!!.getObj1Connection().y,
                graph.tempConnection!!.getObj2Connection().x,
                graph.tempConnection!!.getObj2Connection().y,
                paintConnection
            )
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

    fun setGraph(graph: Graph) {
        this.graph = graph
    }
}