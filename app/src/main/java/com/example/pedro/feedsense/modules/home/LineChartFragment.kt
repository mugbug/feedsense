package com.example.pedro.feedsense.modules.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.pedro.feedsense.R
import com.example.pedro.feedsense.parseToLineDataSet
import com.example.pedro.feedsense.setTextColor
import com.example.pedro.feedsense.useSimpleStyle
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import kotlinx.android.synthetic.main.fragment_line_chart.*
import org.koin.android.architecture.ext.sharedViewModel

class LineChartFragment: Fragment() {

    val viewModel: HomeViewModel by sharedViewModel()

    companion object {

        fun newInstance(): LineChartFragment {
            return LineChartFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_line_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureLineChart(activity!!.applicationContext)
    }

    private fun configureLineChart(context: Context) {
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

        val lineData = LineData(dataSet, dataSet1, dataSet2)
        line_chart.data = lineData
        line_chart.useSimpleStyle()
        line_chart.setTextColor(Color.WHITE)
        line_chart.animateX(3000)

        line_chart.invalidate()
    }

}