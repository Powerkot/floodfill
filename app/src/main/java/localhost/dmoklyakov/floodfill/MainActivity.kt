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
        val size = 100
        val image = Array(size) {
            when (it % 2) {
                0 -> BitSet().apply {
                    for (i in 0..size) {
                        set(i, i%2==0)
                    }
                }
                else -> BitSet().apply {
                    for (i in 0..size) {
                        set(i, i%2!=0)
                    }
                }
            }
        }
        floodFillView.image = image
    }
}