package localhost.dmoklyakov.floodfill

import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.algorithm.Generator
import java.util.*
import kotlin.collections.ArrayList

class FloodFillViewModel : ViewModel() {

    var frameRate = 5
    private val handler = Handler()
    var viewStates = ArrayList<FloodFillViewState>()
    var isWorking = MutableLiveData<Boolean>().apply { value = false }
    var width = 5
    var height = 5
    var newWidth = width
    var newHeight = height
    val maxWidth = 512
    val maxHeight = 512
    val maxFrameRate = 60

    init {
        viewStates.add(FloodFillViewState())
        viewStates.add(FloodFillViewState())
        generate()
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
        val image = Generator().generate(width, height, System.currentTimeMillis())
        for (viewState in viewStates) {
            val clonedImg = Array(height) {
                image[it].clone() as BitSet
            }
            viewState.setImage(width, height, clonedImg)
        }
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