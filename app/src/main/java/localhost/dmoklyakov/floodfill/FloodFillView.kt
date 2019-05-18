package localhost.dmoklyakov.floodfill

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*

class FloodFillView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    interface OnTapListener {
        fun onTapAtCell(x: Int, y: Int)
    }

    private var paint = Paint()
    private var image: Array<BitSet>? = null
    private var imgSizeX = 0
    private var imgSizeY = 0
    private var onTapListener: OnTapListener? = null

    /* TODO:
     * настройка каких-нибудь параметров через xml
     * (?) запилить GLSurfaceView с рендером по запросу,
      * сгенерить один квадрат и рисовать его многократно через glDrawArraysInstanced()
     * (?) ИЛИ передавать кучу данных о закрашенных ячейках
      * во фрагментный шейдер и рисовать прямоугольнички в нём
      * (кажется, это будет сильно медленнее)
     */

    init {
        paint.isAntiAlias = false
        paint.color = 0xFFFFFFFF.toInt()

        setOnTouchListener { _, event ->
            // TODO: определять просто тап, а не отпускание пальца (вроде GestureDetector умеет это)
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_POINTER_UP) {
                onTapListener?.onTapAtCell(
                    (event.x / width * imgSizeX).toInt(),
                    (event.y / height * imgSizeY).toInt()
                )
            }
            return@setOnTouchListener true
        }
    }

    fun setImage(image: Array<BitSet>, sizeX: Int, sizeY: Int) {
        if (sizeX < 1 || sizeY < 1) throw IllegalArgumentException("sizeX and sizeY should be greater than 1!")
        this.image = image
        this.imgSizeX = sizeX
        this.imgSizeY = sizeY
        invalidate()
    }

    fun setOnTapListener(onTapListener: OnTapListener) {
        this.onTapListener = onTapListener
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(0xFF000000.toInt())
        paint.color = 0xFFFFFFFF.toInt()
        image?.let {
            val cellSizeX = 1f * width / imgSizeX
            val cellSizeY = 1f * height / imgSizeY
            for (y in 0 until imgSizeY) {
                for (x in 0 until imgSizeX) {
                    if (it[y][x]) {
                        canvas.drawRect(
                            x * cellSizeX + 1,
                            y * cellSizeY + 1,
                            (x + 1) * cellSizeX - 1,
                            (y + 1) * cellSizeY - 1,
                            paint
                        )
                    }
                }
            }
        }
    }

}