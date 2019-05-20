package localhost.dmoklyakov.floodfill


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.algorithm.Interaction
import com.project.algorithm.InteractionImpl
import com.project.algorithm.LogicCallback
import com.project.algorithm.algorithms.AlgorithmType
import java.util.*

class FloodFillViewState : LogicCallback, ViewModel() {

    enum class State {
        IDLE, // не делаем ничего
        SCHEDULED_TO_START, // стартанёт со следущим тиком
        WORKING_IDLE, // ждём команду next()
        WORKING, // ждём, пока алгоритм отрабатывает
    }

    private var interaction: Interaction = InteractionImpl()
    private var startX = -1
    private var startY = -1
    var algorithmType = AlgorithmType.LINE
    var image = MutableLiveData<Array<BitSet>>()
    var state = MutableLiveData<State>().apply { value = State.IDLE }
    var color1 = MutableLiveData<Int>().apply { value = 0xFFFFFFFF.toInt() }
    var color2 = MutableLiveData<Int>().apply { value = 0xFF000000.toInt() }

    fun setImage(width: Int, height: Int, image: Array<BitSet>) {
        interaction.setImg(width, height, image)
        this.image.value = image
    }

    internal fun scheduleStart(x: Int, y: Int) {
        if (state.value != State.IDLE) {
            return
        }
        startX = x
        startY = y
        state.value = State.SCHEDULED_TO_START
    }

    internal fun start() {
        if (startX < 0 || startY < 0) {
            return
        }
        if (state.value != State.SCHEDULED_TO_START) {
            return
        }

        state.value = State.WORKING
        interaction.start(startX, startY, algorithmType, this)
    }

    fun next() {
        if (state.value != State.WORKING_IDLE) {
            return
        }

        state.value = State.WORKING
        interaction.next()
    }

    override fun onSingleIterationCompleted() {
        image.value = interaction.getImage()
        state.value = State.WORKING_IDLE
    }

    override fun onAllIterationsCompleted() {
        startX = -1
        startY = -1
        image.value = interaction.getImage()
        state.value = State.IDLE
    }

}