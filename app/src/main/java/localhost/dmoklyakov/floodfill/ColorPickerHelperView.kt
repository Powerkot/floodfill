package localhost.dmoklyakov.floodfill

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

// TODO: можно хранить координаты выделения в диапазоне от 0 до 1 (чтоб вьюхе было плевать на её размеры)
// TODO: настройка вьюхи из xml

class ColorPickerHelperView(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {

    interface OnColorSelectedListener {
        fun onColorSelected(color: Int)
    }

    var onColorSelectedListener: OnColorSelectedListener? = null
    private var selection = 0
    var thumbSize = 30f
        set(value) {
            field = value
            invalidate()
        }
    var thumbBorderWidth = 8f
        set(value) {
            field = value
            paint.strokeWidth = value / 2f
            invalidate()
        }
    private var paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = thumbBorderWidth / 2
        isAntiAlias = false
    }

    private lateinit var gradient: LinearGradient
    private lateinit var bitmap: Bitmap
    private lateinit var bitmapCanvas: Canvas

    init {
        setOnTouchListener { _, event ->
            selection = event.y.toInt()
            if (selection < 0) {
                selection = 0
            } else if (selection >= height - 1) {
                selection = height - 1
            }
            onColorSelectedListener?.onColorSelected(bitmap.getPixel(0, selection))
            invalidate()
            true
        }
    }

    private fun update() {
        paint.shader = gradient
        bitmapCanvas.drawPaint(paint)
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (width == 0 || height == 0) {
            return
        }

        gradient = LinearGradient(
            0f,
            0f,
            0f,
            height.toFloat(),
            intArrayOf(
                0xFFFF0000.toInt(),
                0xFFFFFF00.toInt(),
                0xFF00FF00.toInt(),
                0xFF00FFFF.toInt(),
                0xFF0000FF.toInt(),
                0xFFFF00FF.toInt(),
                0xFFFF0000.toInt()
            ),
            floatArrayOf(
                0f,
                1f / 6,
                1f / 3,
                0.5f,
                2f / 3,
                5f / 6,
                1f
            ),
            Shader.TileMode.CLAMP
        )
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmapCanvas = Canvas(bitmap)
        update()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (width == 0 || height == 0) {
            return
        }

        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        paint.shader = null
        paint.color = 0xFF000000.toInt()
        canvas.drawRect(
            0f,
            selection - thumbSize / 2,
            width.toFloat(),
            selection + thumbSize / 2,
            paint
        )
        paint.color = 0xFFFFFFFF.toInt()
        canvas.drawRect(
            0f,
            selection - thumbSize / 2 + thumbBorderWidth / 2,
            width.toFloat(),
            selection + thumbSize / 2 - thumbBorderWidth / 2,
            paint
        )
    }
}