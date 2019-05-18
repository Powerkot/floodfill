package localhost.dmoklyakov.floodfill

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.project.floodfill.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), FloodFillView.OnTapListener {

    /* TODO:
     * доверстать экран, создать стили, повыносить значения в ресурсы
     * обрабатывать тапы по кнопкам и прочее
     * (?) заюзать ConstraintLayout в разметке
     * ограничивать максимальный размер для генерации (придумать критерии для ограничения)
     * решить что делать, если алгоритм отрабатывает слишком медленно (маловероятно, но мало ли)?
     * (?) дать юзеру возможность указать seed для генерации
     * СДЕЛАТЬ В КОДЕ КРАСИВО
     */

    private lateinit var floodFillViewModel: FloodFillViewModel

    private var imageObserver = androidx.lifecycle.Observer<Array<BitSet>> {
        floodFillView1.setImage(it, sizeX, sizeY)
    }
    private var isInWorkObserver = androidx.lifecycle.Observer<Boolean> {
        btnGenerate.isEnabled = !it
        btnSize?.isEnabled = !it
        spAlgorithms1.isEnabled = !it
        spAlgorithms2.isEnabled = !it
        etWidth?.isEnabled = !it
        etHeight?.isEnabled = !it
    }

    private var sizeX = 5
    private var sizeY = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        floodFillView1.setOnTapListener(this)

        floodFillViewModel =
            ViewModelProviders.of(this, FloodFillViewModelFactory(sizeX, sizeY))
                .get(FloodFillViewModel::class.java)
        floodFillViewModel.imageLiveData.observe(this, imageObserver)
        floodFillViewModel.isWorking.observe(this, isInWorkObserver)

        sbFrameRate.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                floodFillViewModel.frameRate = progress + 1
                tvFrameRate.text =
                    resources.getString(R.string.frame_rate, floodFillViewModel.frameRate)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        sbFrameRate.progress = floodFillViewModel.frameRate - 1
    }

    override fun onTapAtCell(x: Int, y: Int) {
        floodFillViewModel.start(x, y)
    }

}