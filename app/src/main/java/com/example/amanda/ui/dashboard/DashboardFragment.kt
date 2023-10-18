package com.example.amanda.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amanda.databinding.FragmentDashboardBinding
import com.example.neruapp.usageStats.UsageStatsRepository
import com.patrykandpatrick.vico.core.entry.entryModelOf

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val usageStatsRepository by lazy { UsageStatsRepository(requireContext()) }
    private val viewModelFactory by lazy { DashboardViewModelFactory(usageStatsRepository) }

    private val dashboardViewModel: DashboardViewModel by activityViewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dashboardViewModel.fetchInstagramUsageForLast7Days()


        updateChartData()

        return root
    }

    private fun updateChartData() {
        dashboardViewModel.floatData.observe(viewLifecycleOwner) { floatList ->
            // Convert List<Float> to vararg Number
            val chartEntryModel = entryModelOf(*floatList.toTypedArray())
            binding.chartView.setModel(chartEntryModel)
        }

    }

    class DashboardViewModelFactory(private val repository: UsageStatsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                return DashboardViewModel(repository) as T
            }
            throw IllegalArgumentException(
                "Unknown ViewMode\n" +
                        "    }l class"
            )
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
