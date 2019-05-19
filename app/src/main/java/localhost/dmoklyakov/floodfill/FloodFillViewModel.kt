package localhost.dmoklyakov.floodfill

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FloodFillViewModel : ViewModel() {

    var frameRate = 5
    private val handler = Handler()
    var viewStates = ArrayList<FloodFillViewState>()
    var isWorking = MutableLiveData<Boolean>().apply { value = false }
    var width = 5
    var height = 5
    var newWidth = width
    var newHeight = height

    init {
        viewStates.add(FloodFillViewState(width, height))
        viewStates.add(FloodFillViewState(width, height))
    }

    private var runnable: Runnable = object : Runnable {
        override fun run() {
            var shouldPost = false
            for (viewState in viewStates) {
                when {
                    viewState.state.value == FloodFillViewState.State.SCHEDULED_TO_START -> {
                        handler.post { viewState.start() }
                        shouldPost = true
                    }
                    viewState.state.value == FloodFillViewState.State.WORKING_IDLE -> {
                        handler.post { viewState.next() }
                        shouldPost = true
                    }
                    viewState.state.value == FloodFillViewState.State.WORKING -> shouldPost = true
                }
            }
            isWorking.value = shouldPost
            if (shouldPost) {
                handler.postDelayed(this, (1000f / frameRate).toLong())
            }
        }
    }

    fun generate() {
        width = newWidth
        height = newHeight
        for (viewState in viewStates) {
            viewState.generate(width, height)
        }
        // TODO: var image = generateImage()
        // for (viewState in viewStates) {
        //     viewState.setImage(image.clone)
        // }
    }

    fun start(x: Int, y: Int, index: Int) {
        viewStates[index].scheduleStart(x, y)
        if (isWorking.value == false) {
            handler.post(runnable)
        }
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(null)
    }
}