package com.example.pedro.feedsense.modules.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pedro.feedsense.*
import com.example.pedro.feedsense.databinding.FragmentLineChartBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import kotlinx.android.synthetic.main.fragment_line_chart.*
import kotlinx.android.synthetic.main.fragment_line_chart.view.*
import org.koin.android.architecture.ext.sharedViewModel

class LineChartFragment: Fragment(), View.OnClickListener {

    val viewModel: HomeViewModel by sharedViewModel()

    companion object {

        fun newInstance(): LineChartFragment {
            return LineChartFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentLineChartBinding>(inflater, R.layout.fragment_line_chart, container, false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        val view = binding.root
        view.plot_chart_with_session_button.setOnClickListener(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.configureLineChart.observe(this, Observer {
            if (it != null) configureLineChart(it)
        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.plot_chart_with_session_button -> {
                plot_chart_with_session_button.startAnimation()
                val sessionId = plot_chart_with_session_field.text.toString()
                viewModel.fetchReactions(sessionId)
            }
            else -> {}
        }
    }

    private fun configureLineChart(data: List<List<Entry>>) {
        val context = activity!!.applicationContext
        view?.plot_chart_with_session_button?.revertAnimation()

        val green = ContextCompat.getColor(context, R.color.green)
        val yellow = ContextCompat.getColor(context, R.color.yellow)
        val red = ContextCompat.getColor(context, R.color.red)

        val dataSet = data[0].parseToLineDataSet(green)
        val dataSet1 = data[1].parseToLineDataSet(yellow)
        val dataSet2 = data[2].parseToLineDataSet(red)

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