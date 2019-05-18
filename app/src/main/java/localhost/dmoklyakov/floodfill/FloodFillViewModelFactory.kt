package localhost.dmoklyakov.floodfill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FloodFillViewModelFactory(var width: Int, var height: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FloodFillViewModel(width, height) as T // да-да, unchecked cast
    }

}