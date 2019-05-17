package localhost.dmoklyakov.floodfill

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), FloodFillView.OnTapListener {

    /* TODO:
     * сверстать экран
     * разная разметка ландшафт/портрет
     * ограничивать максимальный размер для генерации (придумать критерии для ограничения)
     * решить что делать, если алгоритм отрабатывает слишком медленно (маловероятно, но мало ли)?
     * (?) дать юзеру возможность указать seed для генерации
     * СДЕЛАТЬ В КОДЕ КРАСИВО
     */

    private lateinit var floodFillAlgorithmViewModel: FloodFillAlgorithmViewModel

    private var imageObserver = androidx.lifecycle.Observer<Array<BitSet>> {
        floodFillView.setImage(it, sizeX, sizeY)
    }

    private var sizeX = 300
    private var sizeY = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.project.floodfill.R.layout.activity_main)
        floodFillView.setOnTapListener(this)

        floodFillAlgorithmViewModel =
            ViewModelProviders.of(this, FloodFillViewModelFactory(sizeX, sizeY))
                .get(FloodFillAlgorithmViewModel::class.java)
        floodFillAlgorithmViewModel.imageLiveData.observe(this, imageObserver)
    }

    override fun onTapAtCell(x: Int, y: Int) {
        floodFillAlgorithmViewModel.start(x, y)
    }

}