package com.lyj.cleanarchitecturewithmvvm.presentation.main.fragment.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.lyj.cleanarchitecturewithmvvm.databinding.FavoriteFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private val viewModel : FavoriteViewModel by viewModels<FavoriteViewModel>()

    private lateinit var binding : FavoriteFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavoriteFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
}