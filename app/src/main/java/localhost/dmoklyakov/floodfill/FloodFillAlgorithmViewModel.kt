package localhost.dmoklyakov.floodfill

import android.util.Log
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

class FloodFillAlgorithmViewModel(private var width: Int, private var height: Int) : ViewModel() {

    private var interaction: Interaction = InteractionImpl()
    var framerate: Int = 1
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
            override fun onCellChanged(x: Int, y: Int) {
                imageLiveData.value = interaction.getImage().apply {
                    get(y).set(
                        x,
                        !(get(y).get(x))
                    )
                } // TODO: пока нет алгоритма, просто инвертирую ячейку

                GlobalScope.launch(Dispatchers.Main) {
                    while (true) {
                        delay(5000) // TODO: вычесть время выполнения
                        if (!isWorking.value!!) { // мамой клянусь, тут не будет NPE
                            break
                        } else {
                            interaction.next()
                        }
                    }
                }
            }

            override fun onWorkCompleted() {
                isWorking.value = false
            }
        })
    }

    fun next() {
        interaction.next()
    }

}