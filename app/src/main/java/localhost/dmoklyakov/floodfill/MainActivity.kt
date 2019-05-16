package localhost.dmoklyakov.floodfill

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.floodfill.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sizeX = 3
        val sizeY = 3
        val image = Array(sizeY) {
            BitSet().apply {
                for (j in 0 until sizeX) {
                    set(j, Math.random() <= 0.5)
                }
            }
        }
        floodFillView.setImage(image, sizeX, sizeY)
    }
}