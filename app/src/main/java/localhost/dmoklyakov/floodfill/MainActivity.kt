package localhost.dmoklyakov.floodfill

import android.app.Dialog
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.project.floodfill.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_color_picker.*
import java.util.*


class MainActivity : AppCompatActivity(), FloodFillView.OnTapListener {

    /* TODO:
     * создать стили, повыносить значения в ресурсы
     * обрабатывать тапы по кнопкам и прочее
     * (?) заюзать ConstraintLayout в разметке
     * ограничивать максимальный размер для генерации (придумать критерии для ограничения)
     * настройка фреймрейта
     * (?) дать юзеру возможность указать seed для генерации
     * СДЕЛАТЬ В КОДЕ КРАСИВО
     */

    private lateinit var floodFillViewModel: FloodFillViewModel


    private var sizeX = 7
    private var sizeY = 7

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        floodFillView1.setOnTapListener(this)

        floodFillViewModel =
            ViewModelProviders.of(this, FloodFillViewModelFactory(sizeX, sizeY))
                .get(FloodFillViewModel::class.java)

        floodFillViewModel.imageLiveData.observe(this, androidx.lifecycle.Observer<Array<BitSet>> {
            floodFillView1.setImage(it, sizeX, sizeY)
        })

        floodFillViewModel.isWorking.observe(this, androidx.lifecycle.Observer<Boolean> {
            btnGenerate.isEnabled = !it
            btnSize?.isEnabled = !it
            spAlgorithms1.isEnabled = !it
            spAlgorithms2.isEnabled = !it
            etWidth?.isEnabled = !it
            etHeight?.isEnabled = !it
            vColorSelected1.isEnabled = !it
            vColorSelected2.isEnabled = !it
            vColorUnselected1.isEnabled = !it
            vColorUnselected2.isEnabled = !it
        })

        floodFillViewModel.floodFillView1EnabledColor.observe(
            this,
            androidx.lifecycle.Observer<Int> {
                floodFillView1.enabledColor = it
                repaintRippleView(vColorSelected1, it)
            })
        floodFillViewModel.floodFillView1DisabledColor.observe(
            this,
            androidx.lifecycle.Observer<Int> {
                floodFillView1.disabledColor = it
                repaintRippleView(vColorUnselected1, it)
            })

        floodFillViewModel.floodFillView2EnabledColor.observe(
            this,
            androidx.lifecycle.Observer<Int> {
                floodFillView2.enabledColor = it
                repaintRippleView(vColorSelected2, it)
            })
        floodFillViewModel.floodFillView2DisabledColor.observe(
            this,
            androidx.lifecycle.Observer<Int> {
                floodFillView2.disabledColor = it
                repaintRippleView(vColorUnselected2, it)
            })

        sbFrameRate.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                floodFillViewModel.frameRate = progress + 1
                tvFrameRate.text =
                    resources.getString(
                        R.string.frame_rate,
                        floodFillViewModel.frameRate
                    )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        sbFrameRate.progress = floodFillViewModel.frameRate - 1

        repaintRippleView(vColorSelected1, 0xFFFFFFFF.toInt())
        repaintRippleView(vColorUnselected1, 0xFF000000.toInt())
        repaintRippleView(vColorSelected2, 0xFFFFFFFF.toInt())
        repaintRippleView(vColorUnselected2, 0xFF000000.toInt())

        vColorSelected1.setOnClickListener {
            showColorPickerDialog(floodFillViewModel.floodFillView1EnabledColor)
        }
        vColorUnselected1.setOnClickListener {
            showColorPickerDialog(floodFillViewModel.floodFillView1DisabledColor)
        }
        vColorSelected2.setOnClickListener {
            showColorPickerDialog(floodFillViewModel.floodFillView2EnabledColor)
        }
        vColorUnselected2.setOnClickListener {
            showColorPickerDialog(floodFillViewModel.floodFillView2DisabledColor)
        }
    }


    private fun repaintRippleView(view: View, color: Int) {
        val background = view.background
        if (background is RippleDrawable) {
            val backgroundDrawable = background.findDrawableByLayerId(android.R.id.background)
            backgroundDrawable?.let {
                if (it is GradientDrawable) {
                    it.setColor(color)
                }
            }
        }
    }

    override fun onTapAtCell(x: Int, y: Int) {
        floodFillViewModel.start(x, y)
    }

    private fun showColorPickerDialog(liveData: MutableLiveData<Int>) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_color_picker)
        dialog.colorPickerHelperView.onColorSelectedListener =
            object : ColorPickerHelperView.OnColorSelectedListener {
                override fun onColorSelected(color: Int) {
                    dialog.colorPickerView.mainColor = color
                }
            }
        dialog.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.btnApply.setOnClickListener {
            liveData.value = dialog.colorPickerView.getSelectedColor()
            dialog.dismiss()
        }
        dialog.show()
    }

}