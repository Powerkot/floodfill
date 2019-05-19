package localhost.dmoklyakov.floodfill

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.algorithm.AlgorithmType
import com.project.algorithm.Interaction
import com.project.algorithm.InteractionImpl
import com.project.algorithm.LogicCallback
import java.util.*

class FloodFillViewState(width: Int, height: Int) : LogicCallback, ViewModel() {

    enum class State {
        IDLE, // не делаем ничего
        SCHEDULED_TO_START, // стартанёт со следущим тиком
        WORKING_IDLE, // ждём команду next()
        WORKING, // ждём, пока алгоритм отрабатывает
    }

    private var interaction: Interaction = InteractionImpl()
    private var startX = -1
    private var startY = -1
    var algorithmType = AlgorithmType.SIMPLE_FOUR_LINKED
    var image = MutableLiveData<Array<BitSet>>()
    var state = MutableLiveData<State>().apply { value = State.IDLE }
    var color1 = MutableLiveData<Int>().apply { value = 0xFFFFFFFF.toInt() }
    var color2 = MutableLiveData<Int>().apply { value = 0xFF000000.toInt() }

    init { // TODO: удали меня
        generate(width, height)
    }

    fun setImage(image: Array<BitSet>) {
        //TODO: interaction.setImage(image)
        this.image.value = image
    }

    fun generate(width: Int, height: Int) { // TODO: удали меня
        interaction.generate(width, height)
        this.image.value = interaction.getImage()
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

    override fun onCellChanged() {
        image.value = interaction.getImage()
        state.value = State.WORKING_IDLE
    }

    override fun onWorkCompleted() {
        startX = -1
        startY = -1
        image.value = interaction.getImage()
        state.value = State.IDLE
    }

}