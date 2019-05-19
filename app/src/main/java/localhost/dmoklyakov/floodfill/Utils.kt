package localhost.dmoklyakov.floodfill

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.util.DisplayMetrics
import android.view.View

object Utils {

    fun dpToPx(context: Context, dp: Float): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun repaintRippleView(view: View, color: Int) {
        val background = view.background
        if (background is RippleDrawable) {
            val backgroundDrawable = background.findDrawableByLayerId(android.R.id.background)
            backgroundDrawable?.let {
                if (it is GradientDrawable) {
                    it.setColor(color)
                }
            }
        }
    }

}
