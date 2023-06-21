package my.packlol.myquizapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import my.packlol.myquizapp.databinding.FragmentResultBinding

class ResultFragment : Fragment(R.layout.fragment_result) {

    private var _binding: FragmentResultBinding? = null
    private val binding: FragmentResultBinding
        get() = requireNotNull(_binding)


    private var username: String = ""
    private var correct: Int = 0
    private var total: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val args = ResultFragmentArgs.fromBundle(it)
            username = args.username
            correct = args.correct
            total = args.total
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvName.text = username

        binding.tvScore.text = "Your Score is $correct out of $total"

        binding.btnFinish.setOnClickListener{
            val action = ResultFragmentDirections.actionResultFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}