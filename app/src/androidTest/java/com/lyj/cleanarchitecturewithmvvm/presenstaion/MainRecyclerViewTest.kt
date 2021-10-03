package com.lyj.cleanarchitecturewithmvvm.presenstaion

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.lyj.cleanarchitecturewithmvvm.R
import com.lyj.cleanarchitecturewithmvvm.presentation.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivity() {
        val recyclerViewIntercation =  onView(withId(R.id.mainRecyclerView))

        recyclerViewIntercation
            .perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5)
            )


        recyclerViewIntercation
            .check { view, noViewFoundException ->
                assert(
                    view.run {
                        if (this is RecyclerView){
                            val count = adapter?.itemCount
                            count != null && count > 0
                        }else {
                            false
                        }
                    }
                )
            }
    }
}