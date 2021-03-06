package com.example.pedro.feedsense.modules.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pedro.feedsense.*
import com.example.pedro.feedsense.databinding.FragmentLineChartBinding
import com.example.pedro.feedsense.modules.hideKeyboard
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.utils.Utils
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
        setupChartEmptyState(view.context, view.line_chart)
        return view
    }

    private fun setupChartEmptyState(context: Context, chart: LineChart) {
        chart.setNoDataText("Escolha uma sessão acima para ver o gráfico")
        val typeface = ResourcesCompat.getFont(context, R.font.lato_light)
        chart.setNoDataTextTypeface(typeface)
        chart.getPaint(Chart.PAINT_INFO).textSize = Utils.convertDpToPixel(18f)

        // set marker
//        val marker = CustomMarkerView(context, R.layout.line_chart_marker_view)
//        chart.marker = marker
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.configureLineChart.observe(this, Observer {
            if (it != null) configureLineChart(it)
        })

        viewModel.updateSessionsSpinner.observe(this, Observer {
            if (it != null) updateSessionsSpinner(it)
            if (plot_chart_with_session_button.isAnimating) plot_chart_with_session_button.revertAnimation()
        })

        viewModel.lineChartFragmentShowAlert.observe(this, Observer {
            if (it != null) (activity as? HomeActivity)?.showSimpleDialog(it)
            if (plot_chart_with_session_button.isAnimating) plot_chart_with_session_button.revertAnimation()
        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.plot_chart_with_session_button -> {
                hideKeyboard(activity)
                plot_chart_with_session_button.startAnimation()
                val sessionId = plot_chart_with_session_field.selectedItem.toString().split(" - ").first()
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

        val lineData = LineData(dataSet, dataSet1, dataSet2)
        line_chart.data = lineData
        line_chart.useSimpleStyle()
        line_chart.setTextColor(Color.WHITE)
        line_chart.animateX(3000)

        line_chart.invalidate()
    }

    private fun updateSessionsSpinner(sessions: List<String>) {
        val context = activity!!.applicationContext

        val adapter = ArrayAdapter<String>(context, R.layout.spinner_item, sessions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        plot_chart_with_session_field.adapter = adapter
    }
}