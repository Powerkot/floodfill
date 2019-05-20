package localhost.dmoklyakov.floodfill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FloodFillViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FloodFillViewModel() as T // да-да, unchecked cast
    }

}