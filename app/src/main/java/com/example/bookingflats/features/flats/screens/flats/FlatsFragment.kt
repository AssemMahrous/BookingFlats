package com.example.bookingflats.features.flats.screens.flats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.example.bookingflats.R
import com.example.bookingflats.basemodule.base.data.local.IPermissionsManager
import com.example.bookingflats.basemodule.base.platform.BaseFragment
import com.example.bookingflats.basemodule.utils.getKoinInstance
import com.example.bookingflats.basemodule.utils.viewbinding.viewBinding
import com.example.bookingflats.databinding.FragmentFlatsBinding
import com.example.bookingflats.features.flats.screens.BaseLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FlatsFragment : BaseFragment<FlatsViewModel>() {
    private val binding by viewBinding(FragmentFlatsBinding::bind)
    val args by navArgs<FlatsFragmentArgs>()
    private val permissionsManager by getKoinInstance<IPermissionsManager>()
    private val locationPermission =
        permissionsManager.registerForLocationPermission(this) { isGranted, _ ->
            val data = args.filterOption
            getData(data.numberOfBedrooms, data.startDate, data.endDate, isGranted)
        }

    private val adapter = FlatsAdapter {
        viewModel.bookFlat(it.id, args.filterOption.startDate!!, args.filterOption.endDate!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initLocationPermission()
    }

    private fun initAdapter() {
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = BaseLoadStateAdapter { adapter.retry() },
            footer = BaseLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->

            // show empty list
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyList(isListEmpty)

            // Only show the list if refresh succeeds.
            binding.list.isVisible = loadState.mediator?.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.mediator?.refresh is LoadState.Error
            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    requireContext(),
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun initLocationPermission() {
        if (permissionsManager.checkLocationPermission()) {
            val data = args.filterOption
            getData(data.numberOfBedrooms, data.startDate, data.endDate, true)
        } else {
            locationPermission.launch()
        }
    }

    private fun getData(numberOfBeds: Int?, StartDate: Long?, endDate: Long?, isGranted: Boolean) {
        lifecycleScope.launch {
            viewModel.searchFlats(numberOfBeds, endDate, StartDate, isGranted).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            binding.emptyList.visibility = View.VISIBLE
            binding.list.visibility = View.GONE
        } else {
            binding.emptyList.visibility = View.GONE
            binding.list.visibility = View.VISIBLE
        }
    }
}