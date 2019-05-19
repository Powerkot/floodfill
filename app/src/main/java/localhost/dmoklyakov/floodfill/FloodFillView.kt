package localhost.dmoklyakov.floodfill

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.project.floodfill.R
import java.util.*

/* TODO:
 * (?) запилить GLSurfaceView с рендером по запросу,
  * сгенерить один квадрат и рисовать его многократно через glDrawArraysInstanced()
 * (?) ИЛИ передавать кучу данных о закрашенных ячейках
  * во фрагментный шейдер и рисовать прямоугольнички в нём
  * (кажется, это будет сильно медленнее)
 * (?) ну или хотя бы для сокращения количества вызовов отрисовки в этой вьюхе смотреть, сколько ячеек закрашено
 * и сколько не закрашено и рисовать те, которых меньше
 */

class FloodFillView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    interface OnTapListener {
        fun onTapAtCell(x: Int, y: Int)
    }

    private var image: Array<BitSet>? = null
    private var imgSizeX = 0
    private var imgSizeY = 0
    private var onTapListener: OnTapListener? = null
    var color1 = 0
        set(value) {
            field = value
            paint.color = value
            invalidate()
        }
    var color2 = 0
        set(value) {
            field = value
            invalidate()
        }
    private var paint = Paint().apply {
        color = color1
        isAntiAlias = false
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.FloodFillView, 0, 0)
            .apply {
                color1 =
                    getColor(R.styleable.FloodFillView_ffv_color1, 0xFFFFFFFF.toInt())
                color2 =
                    getColor(R.styleable.FloodFillView_ffv_color2, 0xFF000000.toInt())
            }.recycle()

        val gestureDetector = GestureDetector(context, object : GestureDetector.OnGestureListener {
            override fun onShowPress(event: MotionEvent?) {}

            override fun onDown(event: MotionEvent) = true

            override fun onFling(
                event1: MotionEvent, event2: MotionEvent, velocityX: Float, velocityY: Float
            ) = false

            override fun onScroll(
                event1: MotionEvent, event2: MotionEvent, distanceX: Float, distanceY: Float
            ) = false

            override fun onLongPress(event: MotionEvent) {}

            override fun onSingleTapUp(event: MotionEvent): Boolean {
                onTapListener?.onTapAtCell(
                    (event.x / width * imgSizeX).toInt(),
                    (event.y / height * imgSizeY).toInt()
                )
                return true
            }

        })
        setOnTouchListener { _, event ->
            return@setOnTouchListener gestureDetector.onTouchEvent(event)
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
        canvas.drawColor(color2)
        image?.let {
            val cellSizeX = 1f * width / imgSizeX
            val cellSizeY = 1f * height / imgSizeY
            for (y in 0 until imgSizeY) {
                for (x in 0 until imgSizeX) {
                    if (it[y][x]) {
                        canvas.drawRect(
                            x * cellSizeX,
                            y * cellSizeY,
                            (x + 1) * cellSizeX,
                            (y + 1) * cellSizeY,
                            paint
                        )
                    }
                }
            }
        }
    }

}