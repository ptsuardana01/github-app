package com.ps2001.githubapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ps2001.githubapp.databinding.ItemUserBinding
import com.ps2001.githubapp.responses.ItemsItem

class FollowAdapter(private val listFollow: List<ItemsItem>) :
    RecyclerView.Adapter<FollowAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val list = listFollow[position]
        holder.binding.usernameGithub.text = list.login
        Glide.with(holder.binding.imgUserGithub)
            .load(list.avatarUrl)
            .into(holder.binding.imgUserGithub)
    }

    override fun getItemCount(): Int = listFollow.size

    class ListViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)
}