package com.example.pedro.feedsense.modules.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.pedro.feedsense.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import kotlinx.android.synthetic.main.fragment_line_chart.*
import kotlinx.android.synthetic.main.fragment_line_chart.view.*
import org.koin.android.architecture.ext.sharedViewModel
import java.util.*

class LineChartFragment: Fragment(), View.OnClickListener {

    val viewModel: HomeViewModel by sharedViewModel()

    companion object {

        fun newInstance(): LineChartFragment {
            return LineChartFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_line_chart, container, false)
        view.plot_chart_with_session_button.setOnClickListener(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureLineChart()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.plot_chart_with_session_button -> {
                view.plot_chart_with_session_button.startAnimation()
                configureLineChart()
            }
            else -> {}
        }
    }

    private fun configureLineChart() {
        val context = activity!!.applicationContext
        view?.plot_chart_with_session_button?.revertAnimation()
        val entries = ArrayList<Entry>()
        val entries1 = ArrayList<Entry>()
        val entries2 = ArrayList<Entry>()

        val y: FloatArray = floatArrayOf(0f, 1f, 4f, 1f, 4f, 3f, 3f, 1f, 2f, 30f, 5f, 1f, 4f, 1f, 1f, 3f)

        for (i in 0 until y.size) {
            entries.add(Entry(i.toFloat(), y[i]))
            entries1.add(Entry(i.toFloat()+1f, y[i] + i%2))
            entries2.add(Entry(i.toFloat()+2f, y[i] - i%2))
        }

        val green = ContextCompat.getColor(context, R.color.green)
        val yellow = ContextCompat.getColor(context, R.color.yellow)
        val red = ContextCompat.getColor(context, R.color.red)

        val dataSet = entries.parseToLineDataSet(green)
        val dataSet1 = entries1.parseToLineDataSet(yellow)
        val dataSet2 = entries2.parseToLineDataSet(red)

        // set marker
//        val marker = CustomMarkerView(context, R.layout.line_chart_marker_view)
//        line_chart.marker = marker

        val lineData = LineData(dataSet, dataSet1, dataSet2)
        line_chart.data = lineData
        line_chart.useSimpleStyle()
        line_chart.setTextColor(Color.WHITE)
        line_chart.animateX(3000)

        line_chart.invalidate()
    }

}