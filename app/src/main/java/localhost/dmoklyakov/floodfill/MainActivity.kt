package localhost.dmoklyakov.floodfill

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.algorithm.AlgorithmType
import com.project.algorithm.Interaction
import com.project.algorithm.InteractionImpl
import com.project.algorithm.LogicCallback
import com.project.floodfill.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), FloodFillView.OnTapListener {

    /* TODO:
     * сверстать экран
     * разная разметка ландшафт/портрет
     * сохранять данные при повороте экрана
     * ограничивать максимальный размер для генерации (придумать критерии для ограничения)
     * запилить MVP, но без библиотек, а ручками
     * решить что делать, если алгоритм отрабатывает слишком медленно (маловероятно, но мало ли)?
     * (?) дать юзеру возможность указать seed для генерации
     */

    private var isWorkCompleted = false
    private lateinit var interaction: Interaction
    private var sizeX = 3
    private var sizeY = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        interaction = InteractionImpl().apply { generate(sizeX, sizeY) }
        floodFillView.setImage(interaction.getImage(), sizeX, sizeY)
        floodFillView.setOnTapListener(this)
    }

    override fun onTapAtCell(x: Int, y: Int) {
        // TODO: return, если в работе
        interaction.start(x, y, AlgorithmType.SIMPLE_FOUR_LINKED, object : LogicCallback {
            override fun onCellChanged(x: Int, y: Int) {
                floodFillView.setImage(
                    interaction.getImage().apply { get(y).set(x, !(get(y).get(x))) }, // TODO: пока нет алгоритма, просто инвертирую ячейку
                    sizeX,
                    sizeY
                )

                Toast.makeText(this@MainActivity, "onCellChanged", Toast.LENGTH_SHORT).show()
                GlobalScope.launch(Dispatchers.Main) {
                    while (!isWorkCompleted) {
                        delay(500) // TODO: вычесть время выполнения
                        if (!isWorkCompleted) {
                            interaction.next()
                        }
                    }
                }
            }

            override fun onWorkCompleted() {
                isWorkCompleted = true
                Toast.makeText(this@MainActivity, "onWorkCompleted", Toast.LENGTH_SHORT).show()
            }
        })
    }

}