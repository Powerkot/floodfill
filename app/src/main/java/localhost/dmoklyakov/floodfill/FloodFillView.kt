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
    var image: Array<BitSet>? = null

    init {
        paint.isAntiAlias = false
        paint.color = 0xFFFFFFFF.toInt()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(0xFF000000.toInt())
        image?.let {
            val sizeX = 1f * width / it.size
            val sizeY = 1f * height / it[0].length()
            for (y in 0 until it.size) {
                for (x in 0 until it[y].length()) {
                    if (it[y][x]) {
                        canvas.drawRect(x * sizeX, y * sizeY, (x + 1) * sizeX, (y + 1) * sizeY, paint)
                    }
                }
            }
        }
    }

}