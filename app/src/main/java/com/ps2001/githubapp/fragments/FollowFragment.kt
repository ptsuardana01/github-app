package com.ps2001.githubapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ps2001.githubapp.adapters.FollowAdapter
import com.ps2001.githubapp.databinding.FragmentFollowBinding
import com.ps2001.githubapp.model.MainViewModel

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel : MainViewModel by viewModels()

    private var position: Int? = 0
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        showRecyclerView()

        if (position == 1) {
            username?.let { mainViewModel.getListFollowers(it) }
            mainViewModel.vmListFollowers.observe(requireActivity()) { listFollowers ->
                val list = FollowAdapter(listFollowers)
                binding.rvFollowers.adapter = list
            }
        } else {
            username?.let { mainViewModel.getListFollowings(it) }
            mainViewModel.vmListFollowings.observe(requireActivity()) { listFollowings ->
                val list = FollowAdapter(listFollowings)
                binding.rvFollowers.adapter = list
            }
        }

        mainViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingScreen.progressBar.visibility = View.VISIBLE
        } else {
            binding.loadingScreen.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollowers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollowers.addItemDecoration(itemDecoration)
    }


    companion object {
        const val ARG_USERNAME = "username"
        const val ARG_POSITION = "position"
    }
}