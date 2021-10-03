package com.lyj.cleanarchitecturewithmvvm.presentation.main


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jakewharton.rxbinding4.recyclerview.dataChanges
import com.lyj.cleanarchitecturewithmvvm.R
import com.lyj.cleanarchitecturewithmvvm.common.extension.android.longToast
import com.lyj.cleanarchitecturewithmvvm.common.extension.android.selectedObserver
import com.lyj.cleanarchitecturewithmvvm.common.extension.android.unwrappedValue
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.SchedulerType
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.applyScheduler
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.observeOn
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.testTag
import com.lyj.cleanarchitecturewithmvvm.common.utils.DefaultDiffUtil
import com.lyj.cleanarchitecturewithmvvm.databinding.ActivityMainBinding
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.presentation.main.adapter.TrackAdapterViewModel
import com.lyj.cleanarchitecturewithmvvm.presentation.main.adapter.TrackPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import okhttp3.internal.notifyAll
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    public val recyclerView : RecyclerView by lazy{ binding.mainRecyclerView }
    private val viewModel: MainViewModel by viewModels<MainViewModel>()
    private val pagingAdapter: TrackPagingAdapter by lazy {
        TrackPagingAdapter(
            TrackAdapterViewModel(
                this,
                DefaultDiffUtil<Int, TrackData>(),
            ) { observable, i ->
                observable
                    .observeOn(SchedulerType.IO)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .flatMap {
                        viewModel.insertOrDeleteTrackData(it).andThen(Observable.just(it))
                    }
                    .delay(50,TimeUnit.MILLISECONDS)
                    .observeOn(SchedulerType.MAIN)
                    .subscribe({
                        Log.d(testTag,"여기 호출 !! ${i}")
                        when (viewModel.currentMainTabType.unwrappedValue) {
                            MainTabType.LIST -> {
                                pagingAdapter.notifyItemChanged(i)
                            }
                            MainTabType.FAVORITE -> {
                                pagingAdapter.refresh()
                            }
                        }
                    }, {
                        longToast("데이터를 저장 혹은 삭제하는데 실패하였습니다.")
                        it.printStackTrace()
                    })
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        observeRxSource()
    }

    private fun initView() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding
            .mainRecyclerView
            .also { recyclerView ->
                recyclerView.adapter = pagingAdapter
                recyclerView.layoutManager = LinearLayoutManager(this)
            }
    }

    private fun observeRxSource() {
        observeTrackData()
    }

    private fun observeTrackData() {
        Flowable
            .combineLatest(
                viewModel.currentLocalTrackData,
                viewModel.getPagingRepository(viewModel.currentMainTabType),
            ) { local, paging ->
                local to paging
            }
            .observeOn(SchedulerType.MAIN)
            .subscribe({ (localData, pagingData) ->
                Log.d(testTag, "호출 ")
                val tabType = viewModel.currentMainTabType.unwrappedValue
                val pagingDataSource = pagingData
                    .map { data ->
                        data.apply {
                            isFavorite = tabType.checkFavorite.func(
                                localData.mapNotNull { it.trackId },
                                trackId
                            )
                        }
                    }
                pagingAdapter.submitData(lifecycle, pagingDataSource)
            }, {
                it.printStackTrace()
            })


        binding
            .mainBottomNavigationView
            .selectedObserver(this, viewModel.currentMainTabType.value)
            .filter { it != viewModel.currentMainTabType.unwrappedValue }
            .subscribe {
                viewModel.currentMainTabType.value = it
                pagingAdapter.refresh()
            }
    }

}