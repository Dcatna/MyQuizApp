package my.packlol.myquizapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import my.packlol.myquizapp.databinding.FragmentHomeBinding

class HomeFragment: Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonStart.setOnClickListener {
            if(binding.etName.text.isNullOrEmpty()) {
                Toast.makeText(
                    context,
                    "Please Enter Your Name",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToQuizQuestionFragment(
                        username = binding.etName.text.toString()
                    )
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}