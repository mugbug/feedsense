package com.example.pedro.feedsense

import com.example.pedro.feedsense.models.Reaction
import com.example.pedro.feedsense.models.ReactionModel
import com.example.pedro.feedsense.modules.home.HomeViewModel
import com.nhaarman.mockito_kotlin.mock
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.junit.Test
import java.util.*

class HomeViewModelTests {

    private val viewModel: HomeViewModel = mock()

    @Test
    fun testParseToEntriesForEmptyReactions() {
        val reactions = ArrayList<ReactionModel>()
        val listOfEntries = viewModel.parseReactionsToLineChartEntries(reactions)

        listOfEntries.isEmpty() `should be` true
    }

    @Test
    fun testParseToEntriesForLovingReaction() {
        val reactions = ArrayList<ReactionModel>()
        val startDate = Date(10)
        reactions.add(ReactionModel("1", Reaction.LOVING, startDate))
        val listOfEntries = viewModel.parseReactionsToLineChartEntries(reactions)

        listOfEntries.size `should equal` 3

        listOfEntries[0].size `should equal` 1
        listOfEntries[0].first().x `should equal` 0f
        listOfEntries[0].first().y `should equal` 1f

        listOfEntries[1].size `should equal` 1
        listOfEntries[1].first().x `should equal` 0f
        listOfEntries[1].first().y `should equal` 0f

        listOfEntries[2].size `should equal` 1
        listOfEntries[2].first().x `should equal` 0f
        listOfEntries[2].first().y `should equal` 0f
    }

    @Test
    fun testParseToEntriesForWhateverReaction() {
        val reactions = ArrayList<ReactionModel>()
        val startDate = Date(10)
        reactions.add(ReactionModel("1", Reaction.WHATEVER, startDate))
        val listOfEntries = viewModel.parseReactionsToLineChartEntries(reactions)

        listOfEntries.size `should equal` 3

        listOfEntries[0].size `should equal` 1
        listOfEntries[0].first().x `should equal` 0f
        listOfEntries[0].first().y `should equal` 0f

        listOfEntries[1].size `should equal` 1
        listOfEntries[1].first().x `should equal` 0f
        listOfEntries[1].first().y `should equal` 1f

        listOfEntries[2].size `should equal` 1
        listOfEntries[2].first().x `should equal` 0f
        listOfEntries[2].first().y `should equal` 0f
    }


    @Test
    fun testParseToEntriesForHatingReaction() {
        val reactions = ArrayList<ReactionModel>()
        val startDate = Date(10)
        reactions.add(ReactionModel("1", Reaction.HATING, startDate))
        val listOfEntries = viewModel.parseReactionsToLineChartEntries(reactions)

        listOfEntries.size `should equal` 3

        listOfEntries[0].size `should equal` 1
        listOfEntries[0].first().x `should equal` 0f
        listOfEntries[0].first().y `should equal` 0f

        listOfEntries[1].size `should equal` 1
        listOfEntries[1].first().x `should equal` 0f
        listOfEntries[1].first().y `should equal` 0f

        listOfEntries[2].size `should equal` 1
        listOfEntries[2].first().x `should equal` 0f
        listOfEntries[2].first().y `should equal` 1f
    }

    @Test
    fun testParseToEntriesForMultipleReactions() {
        val reactions = ArrayList<ReactionModel>()
        val startDate = Date(1000)
        reactions.add(ReactionModel("1", Reaction.LOVING, startDate))
        reactions.add(ReactionModel("1", Reaction.LOVING, Date(3000)))
        reactions.add(ReactionModel("1", Reaction.LOVING, Date(6000)))
        val listOfEntries = viewModel.parseReactionsToLineChartEntries(reactions)

        listOfEntries.size `should equal` 3

        listOfEntries[0][0].x `should equal` 0f
        listOfEntries[0][1].x `should equal` 2f
        listOfEntries[0][2].x `should equal` 5f

        listOfEntries[0][0].y `should equal` 1f
        listOfEntries[0][1].y `should equal` 2f
        listOfEntries[0][2].y `should equal` 3f
    }
}