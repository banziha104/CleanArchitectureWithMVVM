package com.lyj.cleanarchitecturewithmvvm.presentation.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding4.view.clicks
import com.lyj.cleanarchitecturewithmvvm.R
import com.lyj.cleanarchitecturewithmvvm.common.extension.lang.testTag
import com.lyj.cleanarchitecturewithmvvm.domain.model.TrackData

class TrackAdapter(private val viewModel: TrackAdapterViewModel): PagingDataAdapter<TrackData, TrackAdapter.TrackViewHolder>(viewModel.diffUtil) {

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val data : TrackData? = getItem(position)
        holder.apply {
            trackName.text = data?.trackName
            collectionName.text = data?.collectionName
            artistName.text = data?.artistName
//            btnFavorite.setImageDrawable(viewModel.resDrawble(if (data?.isFavorite != null && data?.isFavorite) R.drawable.ic_star_normal else R.drawable.ic_star_inverted))

            Glide
                .with(viewModel.context)
                .load(data?.url)
                .into(imageView)

            if (data != null) {
                viewModel
                    .onFavoriteButtonClick(
                        btnFavorite.clicks().map {
                            Log.d(testTag,"clicked")
                            data
                        }
                    )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    inner class TrackViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val trackName: TextView = view.findViewById(R.id.trackTxtTrackName)
        val collectionName: TextView = view.findViewById(R.id.trackTxtCollectionName)
        val artistName: TextView = view.findViewById(R.id.trackTxtArtistName)
        val imageView: ImageView = view.findViewById(R.id.trackImgAlbum)
        val btnFavorite: AppCompatImageButton = view.findViewById(R.id.trackBtnFavorite)
    }
}