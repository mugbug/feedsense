package com.example.pedro.feedsense.modules.home

import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.Observer
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.pedro.feedsense.LineChartValueFormatter
import com.example.pedro.feedsense.PreferenceHelper.defaultPrefs
import com.example.pedro.feedsense.PreferenceHelper.get
import com.example.pedro.feedsense.R
import com.example.pedro.feedsense.databinding.ActivityHomeBinding
import com.example.pedro.feedsense.models.Reaction
import com.example.pedro.feedsense.modules.BaseActivity
import com.example.pedro.feedsense.modules.login.LoginActivity
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.architecture.ext.viewModel


class HomeActivity : BaseActivity() {

    private val viewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val binding = DataBindingUtil.setContentView<ActivityHomeBinding>(
                this,
                R.layout.activity_home
        )
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        checkHomeState()

        configureLineChart()
        setupObservers()
    }

    private fun configureLineChart() {
        val entries = ArrayList<Entry>()
        val entries1 = ArrayList<Entry>()
        val entries2 = ArrayList<Entry>()

        val y: FloatArray = floatArrayOf(0f, 1f, 4f, 1f, 4f, 3f, 3f, 1f, 2f, 30f, 5f, 1f, 4f, 1f, 1f, 3f)

        for (i in 0 until y.size) {
            entries.add(Entry(i.toFloat(), y[i]))
            entries1.add(Entry(i.toFloat()+1f, y[i] + i%2))
            entries2.add(Entry(i.toFloat()+2f, y[i] - i%2))
        }

        val dataSetColor = ContextCompat.getColor(this, R.color.green)
        val dataSet = getDatasetFromEntries(entries, dataSetColor)

        val dataSet1Color = ContextCompat.getColor(this, R.color.red)
        val dataSet1 = getDatasetFromEntries(entries1, dataSet1Color)

        val dataSet2Color = ContextCompat.getColor(this, R.color.yellow)
        val dataSet2 = getDatasetFromEntries(entries2, dataSet2Color)

        val lineData = LineData(dataSet, dataSet1, dataSet2)
        line_chart.data = lineData

        clearChart()
        setupChartColors()

        line_chart.invalidate()
    }

    private fun getDatasetFromEntries(entries: List<Entry>, color: Int): LineDataSet {
        val dataSet = LineDataSet(entries, "")
        dataSet.lineWidth = 3f
        dataSet.color = color // changes line color
        //dataSet.valueTextColor = Color.WHITE
        //dataSet.valueFormatter = LineChartValueFormatter()
        //dataSet.valueTextSize = 15f
        dataSet.setDrawValues(false)
        dataSet.circleRadius = 5f
        dataSet.setCircleColor(color)
        dataSet.setCircleColorHole(color)
        return dataSet
    }

    private fun clearChart() {
        line_chart.setDrawGridBackground(false)
        line_chart.setDrawBorders(false)

        // remove useless text
        val desc = Description()
        desc.text = ""
        line_chart.description = desc
        line_chart.axisRight.setDrawLabels(false)
        line_chart.xAxis.setDrawLabels(false)
        line_chart.legend.isEnabled = false

        // remove grid lines
        line_chart.axisLeft.setDrawGridLines(false)
        line_chart.axisRight.setDrawGridLines(false)
        line_chart.xAxis.setDrawGridLines(false)
        line_chart.axisLeft.textSize = 15f

        // and borders
        line_chart.setDrawBorders(false)
    }

    private fun setupChartColors() {
        line_chart.axisRight.textColor = Color.WHITE
        line_chart.axisLeft.textColor = Color.WHITE
        line_chart.xAxis.textColor = Color.WHITE
    }

    private fun checkHomeState() {
        val prefs = defaultPrefs(this)
        val sessionId: String? = prefs["sessionId"]
        if (sessionId != null) {
            viewModel.setCurrentSession(sessionId)
            shouldShowReactionButtons()
        }
    }

    private fun setupObservers() {
        viewModel.showAlert.observe(this, Observer {
            if (it != null) {
                showSimpleDialog(it.title, it.message, it.buttonText, it.isCancelable)
            }
        })

        viewModel.showToast.observe(this, Observer {
            if (it != null) {
                showToast(it)
            }
        })

        viewModel.hideJoinSessionFields.observe(this, Observer {
            shouldHideJoinSessionFields()
        })
    }

    // Actions

    fun didTapLogout(view: View) {
        defaultPrefs(this).edit().clear().apply()
        val intent = Intent(this, LoginActivity::class.java)
        finish()
        startActivity(intent)
    }

//    @Suppress("UNUSED_PARAMETER")
//    fun didTapCreateSession(view: View) {
//        val pin = session_code_field.text.toString()
//        viewModel.createSession(pin)
//    }

    fun didTapWannaJoinSession(view: View) {
        join_session_fields.visibility = View.VISIBLE
    }

    private fun shouldHideJoinSessionFields() {
        join_session_fields.visibility = View.GONE
        session_code_field.setText("")
        session_code_field.clearFocus()
    }

    private fun shouldShowReactionButtons() {
        reaction_buttons.visibility = View.VISIBLE
    }

    @Suppress("UNUSED_PARAMETER")
    fun didTapJoinSession(view: View) {
        val pin = session_code_field.text.toString()
        viewModel.joinSession(pin)
    }

    @Suppress("UNUSED_PARAMETER")
    fun didTapGreenButton(view: View) {
        val reaction = Reaction.LOVING
        viewModel.reactToSession(reaction)
    }

    @Suppress("UNUSED_PARAMETER")
    fun didTapYellowButton(view: View) {
        val reaction = Reaction.WHATEVER
        viewModel.reactToSession(reaction)
    }

    @Suppress("UNUSED_PARAMETER")
    fun didTapRedButton(view: View) {
        val reaction = Reaction.HATING
        viewModel.reactToSession(reaction)
    }
}
