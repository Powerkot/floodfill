package localhost.dmoklyakov.floodfill

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.project.algorithm.algorithms.AlgorithmType
import com.project.floodfill.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_color_picker.*
import kotlinx.android.synthetic.main.dialog_color_picker.btnApply
import kotlinx.android.synthetic.main.dialog_color_picker.btnCancel
import kotlinx.android.synthetic.main.dialog_size.*
import localhost.dmoklyakov.floodfill.Utils.repaintRippleView
import java.util.*

class MainActivity : AppCompatActivity() {

    /* TODO:
     * создать стили, повыносить значения в ресурсы
     * СДЕЛАТЬ В КОДЕ КРАСИВО
     */

    private lateinit var floodFillViewModel: FloodFillViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floodFillViewModel =
            ViewModelProviders.of(this, FloodFillViewModelFactory())
                .get(FloodFillViewModel::class.java)

        floodFillView1.setOnTapListener(object : FloodFillView.OnTapListener {
            override fun onTapAtCell(x: Int, y: Int) {
                when (spAlgorithms1.selectedItemPosition) {
                    0 -> floodFillViewModel.viewStates[0].algorithmType = AlgorithmType.FOUR_WAY
                    1 -> floodFillViewModel.viewStates[0].algorithmType = AlgorithmType.LINE_HORIZONTAL
                    2 -> floodFillViewModel.viewStates[0].algorithmType = AlgorithmType.LINE_VERTICAL
                }
                floodFillViewModel.start(x, y, 0)
            }
        })

        floodFillViewModel.viewStates[0].image.observe(this, Observer<Array<BitSet>> {
            floodFillView1.setImage(it, floodFillViewModel.width, floodFillViewModel.height)
        })

        floodFillView2.setOnTapListener(object : FloodFillView.OnTapListener {
            override fun onTapAtCell(x: Int, y: Int) {
                when (spAlgorithms2.selectedItemPosition) {
                    0 -> floodFillViewModel.viewStates[1].algorithmType = AlgorithmType.FOUR_WAY
                    1 -> floodFillViewModel.viewStates[1].algorithmType = AlgorithmType.LINE_HORIZONTAL
                    2 -> floodFillViewModel.viewStates[1].algorithmType = AlgorithmType.LINE_VERTICAL
                }
                floodFillViewModel.start(x, y, 1)
            }
        })

        floodFillViewModel.viewStates[1].image.observe(this, Observer<Array<BitSet>> {
            floodFillView2.setImage(it, floodFillViewModel.width, floodFillViewModel.height)
        })

        floodFillViewModel.isWorking.observe(this, Observer<Boolean> {
            btnGenerate.isEnabled = !it
            btnSize?.isEnabled = !it
            etWidth?.isEnabled = !it
            etHeight?.isEnabled = !it
        })

        floodFillViewModel.viewStates[0].state.observe(this, Observer<FloodFillViewState.State> {
            spAlgorithms1.isEnabled = it == FloodFillViewState.State.IDLE
        })

        floodFillViewModel.viewStates[1].state.observe(this, Observer<FloodFillViewState.State> {
            spAlgorithms2.isEnabled = it == FloodFillViewState.State.IDLE
        })

        floodFillViewModel.viewStates[0].color1.observe(
            this,
            Observer<Int> {
                floodFillView1.color1 = it
                repaintRippleView(vColor1_1, it)
            })
        floodFillViewModel.viewStates[0].color2.observe(
            this,
            Observer<Int> {
                floodFillView1.color2 = it
                repaintRippleView(vColor1_2, it)
            })
        floodFillViewModel.viewStates[1].color1.observe(
            this,
            Observer<Int> {
                floodFillView2.color1 = it
                repaintRippleView(vColor2_1, it)
            })
        floodFillViewModel.viewStates[1].color2.observe(
            this,
            Observer<Int> {
                floodFillView2.color2 = it
                repaintRippleView(vColor2_2, it)
            })

        sbFrameRate.max = floodFillViewModel.maxFrameRate - 1
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

        tvFrameRate.hint = resources.getString(R.string.frame_rate, floodFillViewModel.maxFrameRate + 1)

        btnSize?.setOnClickListener {
            showSizeDialog()
        }

        etWidth?.hint = floodFillViewModel.maxWidth.toString()
        etHeight?.hint = floodFillViewModel.maxHeight.toString()
        etWidth?.setText(floodFillViewModel.newWidth.toString())
        etHeight?.setText(floodFillViewModel.newHeight.toString())
        etWidth?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.isNotBlank()) {
                    if (s.toString().toInt() > floodFillViewModel.maxWidth) {
                        s.replace(0, s.length, floodFillViewModel.maxWidth.toString(), 0, floodFillViewModel.maxWidth.toString().length)
                    }
                    floodFillViewModel.newWidth = s.toString().toInt()
                } else {
                    floodFillViewModel.newWidth = floodFillViewModel.maxWidth
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        etHeight?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.isNotBlank()) {
                    if (s.toString().toInt() > floodFillViewModel.maxHeight) {
                        s.replace(0, s.length, floodFillViewModel.maxHeight.toString(), 0, floodFillViewModel.maxHeight.toString().length)
                    }
                    floodFillViewModel.newHeight = s.toString().toInt()
                } else {
                    floodFillViewModel.newHeight = floodFillViewModel.maxHeight
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        btnGenerate.setOnClickListener {
            floodFillViewModel.generate()
        }

        repaintRippleView(vColor1_1, 0xFFFFFFFF.toInt())
        repaintRippleView(vColor1_2, 0xFF000000.toInt())
        repaintRippleView(vColor2_1, 0xFFFFFFFF.toInt())
        repaintRippleView(vColor2_2, 0xFF000000.toInt())

        vColor1_1.setOnClickListener { showColorPickerDialog(floodFillViewModel.viewStates[0].color1) }
        vColor1_2.setOnClickListener { showColorPickerDialog(floodFillViewModel.viewStates[0].color2) }
        vColor2_1.setOnClickListener { showColorPickerDialog(floodFillViewModel.viewStates[1].color1) }
        vColor2_2.setOnClickListener { showColorPickerDialog(floodFillViewModel.viewStates[1].color2) }
    }

    private fun showSizeDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_size)
        dialog.etDlgWidth.hint = floodFillViewModel.maxWidth.toString()
        dialog.etDlgHeight.hint = floodFillViewModel.maxHeight.toString()
        dialog.etDlgWidth.setText(floodFillViewModel.newWidth.toString())
        dialog.etDlgHeight.setText(floodFillViewModel.newHeight.toString())

        dialog.etDlgWidth.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.isNotBlank() && s.toString().toInt() > floodFillViewModel.maxWidth) {
                    s.replace(0, s.length, floodFillViewModel.maxWidth.toString(), 0, floodFillViewModel.maxWidth.toString().length)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        dialog.etDlgHeight.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.isNotBlank() && s.toString().toInt() > floodFillViewModel.maxHeight) {
                    s.replace(0, s.length, floodFillViewModel.maxWidth.toString(), 0, floodFillViewModel.maxHeight.toString().length)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        dialog.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.btnApply.setOnClickListener {
            if (dialog.etDlgWidth.text.isNotBlank()) {
                floodFillViewModel.newWidth = dialog.etDlgWidth.text.toString().toInt()
            } else {
                floodFillViewModel.newWidth = floodFillViewModel.maxWidth
            }
            if (dialog.etDlgHeight.text.isNotBlank()) {
                floodFillViewModel.newHeight = dialog.etDlgHeight.text.toString().toInt()
            } else {
                floodFillViewModel.newHeight = floodFillViewModel.maxHeight
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showColorPickerDialog(colorLiveData: MutableLiveData<Int>) {
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
            colorLiveData.value = dialog.colorPickerView.getSelectedColor()
            dialog.dismiss()
        }
        dialog.show()
    }

}