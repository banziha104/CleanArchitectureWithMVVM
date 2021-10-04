package com.lyj.cleanarchitecturewithmvvm.presentation.main


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lyj.cleanarchitecturewithmvvm.common.extension.android.longToast
import com.lyj.cleanarchitecturewithmvvm.common.extension.android.selectedObserver
import com.lyj.cleanarchitecturewithmvvm.common.extension.android.unwrappedValue
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.SchedulerType
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.observeOn
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.testTag
import com.lyj.cleanarchitecturewithmvvm.common.utils.DefaultDiffUtil
import com.lyj.cleanarchitecturewithmvvm.data.source.local.dao.FavoriteDaoEventType
import com.lyj.cleanarchitecturewithmvvm.databinding.ActivityMainBinding
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.presentation.main.adapter.TrackAdapterViewModel
import com.lyj.cleanarchitecturewithmvvm.presentation.main.adapter.TrackPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.CompletableSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.reactive.asPublisher
import kotlinx.coroutines.rx3.asFlowable
import kotlinx.coroutines.rx3.asObservable
import okhttp3.internal.wait
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels<MainViewModel>()

    private val pagingAdapter: TrackPagingAdapter by lazy {
        TrackPagingAdapter(
            TrackAdapterViewModel(
                this,
                DefaultDiffUtil<Int, TrackData>(),
            ) { onClickObserver ->
                onClickObserver
                    .observeOn(SchedulerType.IO)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .flatMapSingle { (index,data)->
                        viewModel.insertOrDeleteTrackData(data).map { index }
                    }
                    .subscribe({ index ->
                        viewModel.mainDataChangeObserver.onNext(index)
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
        initRefreshView()
    }

    private fun initRefreshView() {
        binding
            .mainSwipeRefreshLayout
            .setOnRefreshListener {
                binding.mainSwipeRefreshLayout.isRefreshing = false
                pagingAdapter.refresh()
            }
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
                viewModel.localDataObserver,
                viewModel.getPagingRepository(viewModel.currentMainTabType),
            ) { local, paging ->
                local to paging
            }.map { (local, paging) ->
                Log.d(testTag, "위에 호출")
                val tabType = viewModel.currentMainTabType.unwrappedValue
                val pagingDataSource = paging
                    .map { data ->
                        data.apply {
                            isFavorite = tabType.checkFavorite.func(
                                local.mapNotNull { it.trackId },
                                trackId
                            )
                        }
                    }
                pagingAdapter.submitData(lifecycle,pagingDataSource)
            }
            .concatMapEager {
                viewModel.mainDataChangeObserver.toFlowable(BackpressureStrategy.MISSING)
            }
            .delay(40,TimeUnit.MILLISECONDS)
            .observeOn(SchedulerType.MAIN)
            .subscribe({ position ->
                Log.d(testTag, "아래 호출")
                when (viewModel.currentMainTabType.unwrappedValue) {
                    MainTabType.LIST -> {
                        pagingAdapter.notifyItemChanged(position)
                    }
                    MainTabType.FAVORITE -> {
                        pagingAdapter.refresh()
                    }
                }
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