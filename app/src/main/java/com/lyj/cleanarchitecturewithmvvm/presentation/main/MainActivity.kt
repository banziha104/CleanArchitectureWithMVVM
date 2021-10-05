package com.lyj.cleanarchitecturewithmvvm.presentation.main


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.lyj.cleanarchitecturewithmvvm.common.base.*
import com.lyj.cleanarchitecturewithmvvm.common.extension.android.longToast
import com.lyj.cleanarchitecturewithmvvm.common.extension.android.selectedObserver
import com.lyj.cleanarchitecturewithmvvm.common.extension.android.unwrappedValue
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.SchedulerType
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.observeOn
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.testTag
import com.lyj.cleanarchitecturewithmvvm.common.utils.DefaultDiffUtil
import com.lyj.cleanarchitecturewithmvvm.databinding.ActivityMainBinding
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.presentation.main.adapter.TrackAdapterViewModel
import com.lyj.cleanarchitecturewithmvvm.presentation.main.adapter.TrackPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), DisposableLifecycleController {

    override val disposableLifecycleObserver: DisposableLifecycleObserver = DisposableLifecycleObserver(this)

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
                    .disposeByOnDestory(this)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .switchMapSingle { (index,data)->
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
        observeBottomNavigation()
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
                viewModel.mainDataChangeObserver.toFlowable(BackpressureStrategy.LATEST)
            }
            .delay(20,TimeUnit.MILLISECONDS)
            .observeOn(SchedulerType.MAIN)
            .compose(disposeByOnPause())
            .subscribe({ position ->
                Log.d(testTag, "아래 호출 $position")
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
    }

    private fun observeBottomNavigation(){
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