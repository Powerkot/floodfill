package localhost.dmoklyakov.floodfill

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*

/**
 * Simple and stupid view for testing algorithms
 */
class FloodFillView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var paint = Paint()
    private var image: Array<BitSet>? = null
    private var imgSizeX = 0
    private var imgSizeY = 0

    init {
        paint.isAntiAlias = false
        paint.color = 0xFFFFFFFF.toInt()
    }

    fun setImage(image: Array<BitSet>, sizeX: Int, sizeY: Int) {
        this.image = image
        this.imgSizeX = sizeX
        this.imgSizeY = sizeY
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(0xFF000000.toInt())
        image?.let {
            val cellSizeX = 1f * width / imgSizeX
            val cellSizeY = 1f * height / imgSizeY
            for (y in 0 until imgSizeY) {
                for (x in 0 until imgSizeX) {
                    if (it[y][x]) {
                        canvas.drawRect(x * cellSizeX, y * cellSizeY, (x + 1) * cellSizeX, (y + 1) * cellSizeY, paint)
                    }
                }
            }
        }
    }

}