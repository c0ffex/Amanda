package com.example.amanda.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amanda.databinding.FragmentHomeBinding
import com.example.neruapp.usageStats.UsageStatsRepository

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val usageStatsRepository = UsageStatsRepository(requireContext())
        homeViewModel = ViewModelProvider(this, HomeViewModelFactory(usageStatsRepository)).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        homeViewModel.showUsageTime()
        val textView: TextView = binding.textHome

        homeViewModel.totalTimeInForeground.observe(viewLifecycleOwner) { totalTime ->
            val text = "Time spent on Instagram: $totalTime minutes"
            textView.text = text
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Update the factory to accept the UsageStatsRepository for injection
class HomeViewModelFactory(private val repository: UsageStatsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}