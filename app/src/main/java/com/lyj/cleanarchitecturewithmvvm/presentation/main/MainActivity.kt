package com.lyj.cleanarchitecturewithmvvm.presentation.main


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lyj.cleanarchitecturewithmvvm.R
import com.lyj.cleanarchitecturewithmvvm.common.extension.android.selectedObserver
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.SchedulerType
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.applyScheduler
import com.lyj.cleanarchitecturewithmvvm.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeLiveData()
        observeRxSource()
    }


    private fun observeLiveData() {
        observeBottomNavigation(binding.mainBottomNavigationView)
        observeTrackData()
    }

    private fun observeTrackData(){

    }

    private fun observeBottomNavigation(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView
            .selectedObserver(this, viewModel.currentMainTabType.value)
            .applyScheduler(subscribeOn = SchedulerType.MAIN, observeOn = SchedulerType.MAIN)
            .subscribe {
                viewModel.currentMainTabType.value = it
            }
    }

    private fun observeRxSource() {
        viewModel.currentMainTabType.observe(this) { type: FragmentInstanceGettable ->
            supportFragmentManager.commit {
                replace(R.id.mainFrameLayout, type.getFragment())
            }
        }
    }
}