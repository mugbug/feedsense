package com.example.pedro.feedsense

import android.content.Context
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.ViewPortHandler
import kotlinx.android.synthetic.main.line_chart_marker_view.view.*

class LineChartValueFormatter: IValueFormatter {
    override fun getFormattedValue(value: Float, entry: Entry?, dataSetIndex: Int, viewPortHandler: ViewPortHandler?): String {
        return "" + value.toInt()
    }
}

class CustomMarkerView(context: Context?, layoutResource: Int) : MarkerView(context, layoutResource) {

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        line_chart_marker_value.text = e?.y?.toInt().toString()
    }

    override fun getLeftPaddingOffset(): Int {
        return -(width / 2)
    }

    override fun getBottomPaddingOffset(): Int {
        return 0
    }
}

fun List<Entry>.parseToLineDataSet(color: Int): LineDataSet {
    val dataSet = LineDataSet(this, "")
    dataSet.lineWidth = 3f
    dataSet.color = color // changes line color
    dataSet.setDrawValues(false)
    dataSet.circleRadius = 5f
    dataSet.setCircleColor(color)
    dataSet.setCircleColorHole(color)
    return dataSet
}

fun LineChart.useSimpleStyle() {
    this.setDrawGridBackground(false)
    this.setDrawBorders(false)

    // remove useless text
    val desc = Description()
    desc.text = ""
    this.description = desc
    this.axisRight.setDrawLabels(false)
    this.xAxis.setDrawLabels(false)
    this.legend.isEnabled = false

    // remove grid lines
    this.axisLeft.setDrawGridLines(false)
    this.axisRight.setDrawGridLines(false)
    this.xAxis.setDrawGridLines(false)
    this.axisLeft.textSize = 15f

    // and borders
    this.setDrawBorders(false)
}

fun LineChart.setTextColor(color: Int) {
    this.axisRight.textColor = color
    this.axisLeft.textColor = color
    this.xAxis.textColor = color
}