package fr.istic.mob.networkLR

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.room.util.StringUtil


class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var mCurrentCanvas = Canvas()
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            mCurrentCanvas = canvas
        }
        if (MainActivity.mGraph.smartObject?.size == 0) {
            val clearPaint = Paint()
            clearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            canvas!!.drawRect(0f, 0f, 0f, 0f, clearPaint)
        }
        // drawObject()
        drawObject2()
        drawConnection()
    }

    private fun drawObject() {

        MainActivity.mGraph.smartObject?.forEach {
            if (it.fileName == "ic_bed"){
                var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_bed)
                bitmap = Bitmap.createScaledBitmap(bitmap!!, 100, 100, false)

            }
            val matrix = Matrix()
            matrix.postTranslate(it.positionX.toFloat(), it.positionY.toFloat())

        }
    }

    fun drawObject2() {
        MainActivity.mGraph.smartObject?.forEach {
            val mpaint = Paint()
            mpaint.color = it.color
            mpaint.style = Paint.Style.FILL
            val paint2 = Paint()
            paint2.color = Color.WHITE
            paint2.textSize = 50f //set text size

            val w: Float = paint2.measureText(it.id) / 2
            val textSize: Float = paint2.textSize


            paint2.color = Color.BLACK
            paint2.textSize = 40f
            paint2.typeface = Typeface.DEFAULT_BOLD
            if (it.fileName != null)
            {

                if (it.fileName == "ic_bed"){
                    var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_bed)
                    bitmap = Bitmap.createScaledBitmap(bitmap!!, 100, 100, false)
                    val matrix = Matrix()
                    matrix.postTranslate(it.positionX.toFloat(), it.positionY.toFloat())
                    mCurrentCanvas.drawBitmap(bitmap, matrix, null)
                }
                if (it.fileName == "ic_bath"){
                    var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_bath)
                    bitmap = Bitmap.createScaledBitmap(bitmap!!, 100, 100, false)
                    val matrix = Matrix()
                    matrix.postTranslate(it.positionX.toFloat(), it.positionY.toFloat())
                    mCurrentCanvas.drawBitmap(bitmap, matrix, null)
                }
                if (it.fileName == "ic_table"){
                    var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_table)
                    bitmap = Bitmap.createScaledBitmap(bitmap!!, 100, 100, false)
                    val matrix = Matrix()
                    matrix.postTranslate(it.positionX.toFloat(), it.positionY.toFloat())
                    mCurrentCanvas.drawBitmap(bitmap, matrix, null)
                }
                if (it.fileName == "ic_basin"){
                    var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_basin)
                    bitmap = Bitmap.createScaledBitmap(bitmap!!, 100, 100, false)
                    val matrix = Matrix()
                    matrix.postTranslate(it.positionX.toFloat(), it.positionY.toFloat())
                    mCurrentCanvas.drawBitmap(bitmap, matrix, null)
                }
                if (it.fileName == "ic_desk"){
                    var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_desk)
                    bitmap = Bitmap.createScaledBitmap(bitmap!!, 100, 100, false)
                    val matrix = Matrix()
                    matrix.postTranslate(it.positionX.toFloat(), it.positionY.toFloat())
                    mCurrentCanvas.drawBitmap(bitmap, matrix, null)
                }
                if (it.fileName == "ic_dt"){
                    var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_dt)
                    bitmap = Bitmap.createScaledBitmap(bitmap!!, 100, 100, false)
                    val matrix = Matrix()
                    matrix.postTranslate(it.positionX.toFloat(), it.positionY.toFloat())
                    mCurrentCanvas.drawBitmap(bitmap, matrix, null)
                }
                if (it.fileName == "ic_tel"){
                    var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_tel)
                    bitmap = Bitmap.createScaledBitmap(bitmap!!, 100, 100, false)
                    val matrix = Matrix()
                    matrix.postTranslate(it.positionX.toFloat(), it.positionY.toFloat())
                    mCurrentCanvas.drawBitmap(bitmap, matrix, null)
                }
                if (it.fileName == "ic_tn"){
                    var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_tn)
                    bitmap = Bitmap.createScaledBitmap(bitmap!!, 100, 100, false)
                    val matrix = Matrix()
                    matrix.postTranslate(it.positionX.toFloat(), it.positionY.toFloat())
                    mCurrentCanvas.drawBitmap(bitmap, matrix, null)
                }
                if (it.fileName == "ic_div"){
                    var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_div)
                    bitmap = Bitmap.createScaledBitmap(bitmap!!, 100, 100, false)
                    val matrix = Matrix()
                    matrix.postTranslate(it.positionX.toFloat(), it.positionY.toFloat())
                    mCurrentCanvas.drawBitmap(bitmap, matrix, null)
                }



            }

            else
            {
                val rect = Rect(it.positionX-2*45, it.positionY-45, it.positionX+2*45, it.positionY+45)
                mCurrentCanvas.drawRect(rect, mpaint)
            }
            mCurrentCanvas.drawText(  it.name,
                (it.positionX+2*45).toFloat() ,  (it.positionY-45).toFloat() + 40 , paint2)

        }
    }

    private fun drawConnection()
    {
        MainActivity.mGraph.connection?.forEach {
            val mConnectionPaint = Paint()
            mConnectionPaint.color = it.color
            mConnectionPaint.isAntiAlias=true
            mConnectionPaint.style=Paint.Style.STROKE
            mConnectionPaint.strokeWidth=it.episseur
            val path=Path()
            path.moveTo(it.startSmartObject.positionX.toFloat(), it.startSmartObject.positionY.toFloat())
            path.lineTo(it.endSmartObject?.positionX?.toFloat()!!, it.endSmartObject!!.positionY.toFloat())
            path.quadTo(it.startSmartObject.positionX.toFloat(), it.startSmartObject.positionY.toFloat() ,it.endSmartObject?.positionX?.toFloat()!!, it.endSmartObject!!.positionY.toFloat() )
            mCurrentCanvas.drawPath(path,mConnectionPaint)
            mConnectionPaint.color = Color.BLACK
            mConnectionPaint.setStyle(Paint.Style.FILL)
            mConnectionPaint.textSize = 40f
            mConnectionPaint.typeface = Typeface.DEFAULT_BOLD

            mCurrentCanvas.drawTextOnPath(it.name, path,300f,0f, mConnectionPaint);

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()

        val eventAction = event.action
        Log.i("onTouchEvent", "X=$x \n Y=$y \n $eventAction \n --------- \n ")
        return false
    }


}