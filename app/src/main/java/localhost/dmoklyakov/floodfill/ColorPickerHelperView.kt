package localhost.dmoklyakov.floodfill

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.project.floodfill.R
import localhost.dmoklyakov.floodfill.Utils.dpToPx

// TODO: можно хранить координаты выделения в диапазоне от 0 до 1 (чтоб вьюхе было плевать на ресайз)

class ColorPickerHelperView(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {

    interface OnColorSelectedListener {
        fun onColorSelected(color: Int)
    }

    var onColorSelectedListener: OnColorSelectedListener? = null
    private var selection = 0
    var thumbSize = 0f
        set(value) {
            field = value
            updateSelectionToolRect()
            invalidate()
        }
    var thumbBorderWidth = 0f
        set(value) {
            field = value
            paint.strokeWidth = value
            updateSelectionToolRect()
            invalidate()
        }
    private var paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = thumbBorderWidth
        isAntiAlias = false
    }

    private lateinit var gradient: LinearGradient
    private lateinit var bitmap: Bitmap
    private lateinit var bitmapCanvas: Canvas
    private var selectionToolRect = RectF()

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ColorPickerHelperView, 0, 0)
            .apply {
                thumbSize =
                    getDimension(
                        R.styleable.ColorPickerHelperView_cphv_thumbSize,
                        dpToPx(context, 30f)
                    )
                thumbBorderWidth = getDimension(
                    R.styleable.ColorPickerHelperView_cphv_thumbBorderWidth,
                    dpToPx(context, 2f)
                )
            }.recycle()
        setOnTouchListener { _, event ->
            selection = event.y.toInt()
            if (selection < 0) {
                selection = 0
            } else if (selection >= height - 1) {
                selection = height - 1
            }
            onColorSelectedListener?.onColorSelected(bitmap.getPixel(0, selection))
            updateSelectionToolRect()
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
        updateSelectionToolRect()
        update()
    }

    fun updateSelectionToolRect() {
        selectionToolRect.set(
            thumbBorderWidth / 2f,
            selection - thumbSize / 2f - thumbBorderWidth / 2f,
            width.toFloat() - thumbBorderWidth / 2f,
            selection + thumbSize / 2f + thumbBorderWidth / 2f
        )
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (width == 0 || height == 0) {
            return
        }

        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        paint.shader = null
        paint.style = Paint.Style.FILL
        paint.color = bitmap.getPixel(0, selection)
        canvas.drawRect(selectionToolRect, paint)
        paint.style = Paint.Style.STROKE
        paint.color = 0xFF000000.toInt()
        canvas.drawRect(selectionToolRect, paint)
    }
}