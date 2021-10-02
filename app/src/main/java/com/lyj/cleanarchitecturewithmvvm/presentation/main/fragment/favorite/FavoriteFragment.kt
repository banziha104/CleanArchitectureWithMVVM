package com.lyj.cleanarchitecturewithmvvm.presentation.main.fragment.favorite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.SchedulerType
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.applyScheduler
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.testTag
import com.lyj.cleanarchitecturewithmvvm.common.utils.DefaultDiffUtil
import com.lyj.cleanarchitecturewithmvvm.data.source.remote.entity.ITunesSearchResponse
import com.lyj.cleanarchitecturewithmvvm.databinding.FavoriteFragmentBinding
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData
import com.lyj.cleanarchitecturewithmvvm.presentation.main.MainViewModel
import com.lyj.cleanarchitecturewithmvvm.presentation.main.adapter.TrackAdapter
import com.lyj.cleanarchitecturewithmvvm.presentation.main.adapter.TrackAdapterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    companion object{
        internal val instance : FavoriteFragment by lazy {
            FavoriteFragment()
        }
    }
    private val viewModel : FavoriteViewModel by viewModels<FavoriteViewModel>()

    private val activityViewModel : MainViewModel by activityViewModels<MainViewModel>()

    private lateinit var binding : FavoriteFragmentBinding


    private val pagingAdapter: TrackAdapter by lazy {
        TrackAdapter(
            TrackAdapterViewModel(requireContext(), DefaultDiffUtil<Int, TrackData>()){ observable ->

            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FavoriteFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerviewInit()
        observeRxSource()
    }

    private fun observeRxSource(){
        viewModel
            .pagingAdapterData
            .applyScheduler(subscribeOn = SchedulerType.IO, observeOn = SchedulerType.IO)
            .subscribe({
                       pagingAdapter.submitData(lifecycle,it)
            },{
                it.printStackTrace()
            })
    }

    private fun recyclerviewInit() {
        binding.favoriteRecyclerView.apply {
            adapter = pagingAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}