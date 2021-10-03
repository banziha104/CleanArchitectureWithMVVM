package com.lyj.cleanarchitecturewithmvvm.presentation.main.fragment.list

import com.lyj.cleanarchitecturewithmvvm.presentation.main.adapter.TrackAdapterViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.paging.map
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.SchedulerType
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.observeOn
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.subscribeOn
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.testTag
import com.lyj.cleanarchitecturewithmvvm.common.utils.DefaultDiffUtil
import com.lyj.cleanarchitecturewithmvvm.data.source.remote.entity.ITunesSearchResponse
import com.lyj.cleanarchitecturewithmvvm.databinding.ListFragmentBinding
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackDataDiffUtils
import com.lyj.cleanarchitecturewithmvvm.presentation.main.MainViewModel
import com.lyj.cleanarchitecturewithmvvm.presentation.main.adapter.TrackAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Flowable

@AndroidEntryPoint
class ListFragment : Fragment() {

    companion object {
        internal val instance: ListFragment by lazy {
            ListFragment()
        }
    }

    private val viewModel: ListViewModel by viewModels<ListViewModel>()

    private val activityViewModel: MainViewModel by activityViewModels<MainViewModel>()

    private val pagingAdapter: TrackAdapter by lazy {
        TrackAdapter(
            TrackAdapterViewModel(requireContext(), TrackDataDiffUtils()) { observable, position ->
                observable.subscribe({
                    activityViewModel
                        .insertOrDeleteTrackData(trackData = it)
                        .subscribeOn(SchedulerType.IO)
                        .observeOn(SchedulerType.MAIN)
                        .subscribe {
                            pagingAdapter.refresh()
                        }
                }, {
                    it.printStackTrace()
                })
            }
        )
    }

    private lateinit var binding: ListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerviewInit()
        observeRxSource()
    }

    private fun observeRxSource() {
        Flowable
            .combineLatest(
                viewModel.pagingAdapterData,
                activityViewModel.currentLocalTrackData
            ) { remote, local ->
                remote to local
            }
            .subscribeOn(SchedulerType.IO)
            .observeOn(SchedulerType.MAIN)
            .subscribe({ (remoteData, localData) ->
                localData.forEach {
                    Log.d(testTag, "database : ${it}")
                }
                val data = if (localData.isEmpty()) remoteData else remoteData.map { trackData ->
                    trackData.apply {
                        isFavorite = viewModel.tabType.checkFavorite.func(
                            localData.mapNotNull { it.trackId },
                            trackId
                        )
                    }
                }
                Log.d(testTag, "호출")
                pagingAdapter.submitData(lifecycle, data)
            }, { it.printStackTrace() })
    }

    private fun recyclerviewInit() {
        binding.listRecyclerView.apply {
            adapter = pagingAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
