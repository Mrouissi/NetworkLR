package fr.istic.mob.networkLR

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class GraphView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var graph: Graph = Graph()
    private val paintObj: Paint = Paint()
    private val paintText: Paint = Paint()
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

        for (obj in this.graph.getListConnectedObject()) {
            val rectF = this.connectedObjectToRectF(obj)
            canvas.drawRoundRect(rectF, 10f, 10f, paintObj)
            canvas.drawText(obj.name, rectF.centerX(), rectF.centerY(), paintText)
        }

        for (connection in this.graph.getListConnection()) {
            canvas.drawLine(connection.getObj1Connection().x + objWidth/2, connection.getObj1Connection().y + objHeight/2,
                connection.getObj2Connection().x + objWidth/2, connection.getObj2Connection().y + objHeight/2, paintObj)
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

    public fun setGraph(graph: Graph) {
        this.graph = graph
    }
}