package my.packlol.myquizapp

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import my.packlol.myquizapp.databinding.ActivityQuizQuestionBinding

class QuizQuestionFragment: Fragment(R.layout.activity_quiz_question) {

    private var _binding: ActivityQuizQuestionBinding? = null
    private val binding: ActivityQuizQuestionBinding
        get() = requireNotNull(_binding)

    private val questions = Constants.questionsList

    private val currentPosition = MutableStateFlow(0)

    private val mutSelectedOption = MutableStateFlow<Pair<TextView?, Int>>(null to 0)
    private val selectedOption = mutSelectedOption.asStateFlow()

    private var correctAnswers: Int = 0
    private var userName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityQuizQuestionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userName = it.getString("username").toString()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            launch {
                selectedOption.collect { (view: TextView?, num: Int) ->
                    defaultOptionsView()
                    view?.apply {
                        setTextColor(Color.parseColor("#363A43"))
                        setTypeface(typeface, Typeface.BOLD)
                        background = ContextCompat.getDrawable(context, R.drawable.selected_option_border_bg)
                    }
                }
            }
            launch {
                currentPosition.collect { pos ->
                    mutSelectedOption.emit(null to 0)
                    setQuestion(
                        currentPosition = pos,
                        question = questions.getOrNull(pos) ?: return@collect
                    )
                }
            }
        }
        binding.progressBar.max = questions.size

        listOf<TextView>(
            binding.optionOne, binding.optionTwo, binding.optionThree, binding. optionFour
        ).forEachIndexed { index, textView ->
            textView.setOnClickListener {
                mutSelectedOption.tryEmit(textView to index)
            }
        }

        binding.btnGoNext.setOnClickListener {
            if (currentPosition.value < questions.lastIndex) {
                currentPosition.getAndUpdate { it + 1 }
            } else {
                findNavController()
                    .navigate(
                        QuizQuestionFragmentDirections.actionQuizQuestionFragmentToResultFragment(
                            username = userName,
                            correct = correctAnswers,
                            total = questions.size
                        )
                    )
            }
            binding.btnGoNext.visibility = View.GONE
            binding.btnSubmit.visibility = View.VISIBLE
        }

        binding.btnSubmit.setOnClickListener {

            val question = questions[currentPosition.value]
            val correctIdx = question.correctAnswer - 1

            selectedOption.value.let { (tv, idx) ->

                if (tv == null) {
                    Toast.makeText(
                        context,
                        "Answer Not Selected",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }

                if (correctIdx == idx) {
                    correctAnswers += 1
                }
                applyAnswerBackgrounds(correctIdx, idx)
            }
            binding.btnSubmit.visibility = View.GONE
            binding.btnGoNext.text = if (currentPosition.value == questions.lastIndex) {
                 "Finish"
            } else {
                "Go To Next Question"
            }
            binding.btnGoNext.visibility = View.VISIBLE
        }
    }

    private fun applyAnswerBackgrounds(
        correctIdx: Int,
        selectedIdx: Int
    ) {
        val textViews =  listOf(
            binding.optionOne, binding.optionTwo, binding.optionThree, binding. optionFour
        )
        if (correctIdx != selectedIdx) {
            textViews[selectedIdx]
                .apply {
                    background = ContextCompat
                        .getDrawable(
                            context,
                            R.drawable.wrong_option_border_bg
                        )
                }
        }
       textViews[correctIdx]
            .apply {
                background = ContextCompat
                    .getDrawable(
                        context,
                        R.drawable.correct_option_border_bg
                    )
            }
    }

    private fun defaultOptionsView() = with(binding) {
        listOf(
            optionOne, optionTwo, optionThree, optionFour
        )
            .forEachIndexed { index, textView ->
                textView.setTextColor(Color.parseColor("#7A8089"))
                textView.typeface = Typeface.DEFAULT
                textView.background = ContextCompat
                        .getDrawable(
                            requireContext(),
                            R.drawable.default_option_border_bg
                        )
            }
    }


    private fun setQuestion(
        currentPosition: Int,
        question: Question
    ) = with(binding) {
        defaultOptionsView()
        ivImage.setImageResource(question.image)
        progressBar.progress = currentPosition
        tvProgress.text = "$currentPosition/ ${progressBar.max}"
        tvQuestion.text = question.question
        optionOne.text = question.optionOne
        optionTwo.text = question.optionTwo
        optionThree.text = question.optionThree
        optionFour.text = question.optionFour
        btnSubmit.text = "Submit"
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}