package com.example.mediaplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mediaplayer.databinding.ItemRvBinding

class MusicAdapter(var rvAction: RvAction,var list:MutableList<Music>):RecyclerView.Adapter<MusicAdapter.Vh>() {
    inner class Vh(var itemRvBinding: ItemRvBinding):ViewHolder(itemRvBinding.root){
        fun onBind(music: Music, position: Int){
            itemRvBinding.name.text = music.title
            itemRvBinding.auothor.text = music.author

            itemRvBinding.root.setOnClickListener {
                rvAction.onClick(music,position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position],position)
    }
    interface RvAction{
        fun onClick(music: Music,position: Int)
    }
}