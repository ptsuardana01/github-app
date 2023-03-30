package com.ps2001.githubapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ps2001.githubapp.data.User
import com.ps2001.githubapp.databinding.ItemUserBinding

class MainAdapter(private val listUser: List<String>) : RecyclerView.Adapter<MainAdapter.ListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val username = listUser[position]
        holder.binding.usernameGithub.text = username
//        Glide.with(holder.binding.imgUserGithub)
//            .load(avatar_img)
//            .into(holder.binding.imgUserGithub)
    }

    override fun getItemCount(): Int = listUser.size

    class ListViewHolder(var binding : ItemUserBinding) : RecyclerView.ViewHolder(binding.root)
}