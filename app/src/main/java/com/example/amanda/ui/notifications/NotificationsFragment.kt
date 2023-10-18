package com.example.amanda.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.amanda.databinding.FragmentNotificationsBinding
import com.example.neruapp.gachaSystem.GachaViewModel

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var gachaViewModel: GachaViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Obtenha uma instância do GachaViewModel
        gachaViewModel = ViewModelProvider(this)[GachaViewModel::class.java]

        // Infla o layout para este fragmento
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Configure um ouvinte de clique no seu botão
        binding.button.setOnClickListener {
            // Suponha que userId e coins são obtidos de algum lugar
            val userId = "1"
            val coins = 10
            gachaViewModel.activateGacha(userId, coins)
        }

        // Se você tem um NotificationsViewModel e quer observar algum texto:
        val notificationsViewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]
        val textView: TextView = binding.textView
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}