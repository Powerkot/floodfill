package localhost.dmoklyakov.floodfill

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.algorithm.AlgorithmType
import com.project.algorithm.Interaction
import com.project.algorithm.InteractionImpl
import com.project.algorithm.LogicCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class FloodFillViewModel(private var width: Int, private var height: Int) : ViewModel() {

    private var interaction: Interaction = InteractionImpl()
    var frameRate: Int = 5
    var imageLiveData: MutableLiveData<Array<BitSet>> = MutableLiveData()
    var isWorking: MutableLiveData<Boolean> = MutableLiveData()

    init {
        generateImage(width, height)
    }

    fun generateImage(width: Int, height: Int) {
        interaction.generate(width, height)
        this.width = width
        this.height = height
        imageLiveData.value = interaction.getImage()
    }

    fun start(x: Int, y: Int) {
        isWorking.value?.let {
            if (it) {
                return@start
            }
        }
        isWorking.value = true
        interaction.start(x, y, AlgorithmType.SIMPLE_FOUR_LINKED, object : LogicCallback {
            override fun onCellChanged() {
                GlobalScope.launch(Dispatchers.Main) {
                    imageLiveData.value = interaction.getImage()
                    delay(100) // TODO: вычесть время выполнения и заюзать frameRate
                    interaction.next()
                }
            }

            override fun onWorkCompleted() {
                imageLiveData.value = interaction.getImage()
                isWorking.value = false
            }
        })
    }

    fun next() {
        interaction.next()
    }

}