package localhost.dmoklyakov.floodfill

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.project.floodfill.R
import localhost.dmoklyakov.floodfill.Utils.dpToPx

// TODO: можно хранить координаты выделения в диапазоне от 0 до 1 (чтоб вьюхе было плевать на её ресайз)

class ColorPickerView(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {

    var thumbSize = 0f
        set(value) {
            field = value
            invalidate()
        }
    var thumbBorderWidth = 0f
        set(value) {
            field = value
            paint.strokeWidth = value / 2f
            invalidate()
        }

    private var selectionX = 0
    private var selectionY = 0
    private var paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = thumbBorderWidth / 2f
        isAntiAlias = true
    }

    private lateinit var whiteHorizontalGradient: LinearGradient
    private lateinit var blackVerticalGradient: LinearGradient
    private lateinit var bitmap: Bitmap
    private lateinit var bitmapCanvas: Canvas

    var mainColor = 0xFFFF0000.toInt()
        set(value) {
            field = value
            update()
        }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ColorPickerView, 0, 0)
            .apply {
                thumbSize =
                    getDimension(R.styleable.ColorPickerView_cpv_thumbSize, dpToPx(context, 60f))
                thumbBorderWidth = getDimension(
                    R.styleable.ColorPickerView_cpv_thumbBorderWidth,
                    dpToPx(context, 4f)
                )
            }.recycle()

        setOnTouchListener { _, event ->
            selectionX = event.x.toInt()
            selectionY = event.y.toInt()
            if (selectionX < 0) {
                selectionX = 0
            } else if (selectionX >= width - 1) {
                selectionX = width - 1
            }
            if (selectionY < 0) {
                selectionY = 0
            } else if (selectionY >= height - 1) {
                selectionY = height - 1
            }
            invalidate()
            true
        }
    }

    fun getSelectedColor() = bitmap.getPixel(selectionX, selectionY)

    private fun update() {
        bitmapCanvas.drawColor(mainColor)
        paint.shader = whiteHorizontalGradient
        bitmapCanvas.drawPaint(paint)
        paint.shader = blackVerticalGradient
        bitmapCanvas.drawPaint(paint)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val specWidth = MeasureSpec.getSize(widthMeasureSpec)
        val specHeight = MeasureSpec.getSize(heightMeasureSpec)
        val lesserDimension = if (specWidth > specHeight) specHeight else specWidth
        setMeasuredDimension(lesserDimension, lesserDimension)
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (width == 0 || height == 0) {
            return
        }

        whiteHorizontalGradient = LinearGradient(
            0f,
            0f,
            width.toFloat(),
            0f,
            0xFFFFFFFF.toInt(),
            0x00FFFFFF,
            Shader.TileMode.CLAMP
        )
        blackVerticalGradient = LinearGradient(
            0f,
            0f,
            0f,
            height.toFloat(),
            0x00000000,
            0xFF000000.toInt(),
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
        paint.style = Paint.Style.FILL
        paint.color = getSelectedColor()
        canvas.drawCircle(
            selectionX.toFloat(),
            selectionY.toFloat(),
            thumbSize / 2,
            paint
        )

        paint.style = Paint.Style.STROKE
        paint.color = if (selectionY > height / 2) 0xFFFFFFFF.toInt() else 0xFF000000.toInt()
        canvas.drawCircle(
            selectionX.toFloat(),
            selectionY.toFloat(),
            thumbSize / 2 + thumbBorderWidth/4,
            paint
        )
    }
}