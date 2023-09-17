package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()
    private var lastQ: Boolean = false
    private var answered: Boolean = false
    private lateinit var tempToast: Toast

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater = result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater) //turns xml layout to UI
        setContentView(binding.root)

        //Log.d(TAG, "Got a QuizViewModel: $quizViewModel")





        binding.trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }
        binding.falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        } //actions on click

        binding.nextButton.setOnClickListener {
            if (tempToast != null) {
                tempToast.cancel()
            }
            if (lastQ) {
                finalToast()
            } else {
                answered = false
                quizViewModel.moveToNext()
                lastQ = endOfBank
                updateQuestion()
            }
        }

//        binding.prevButton.setOnClickListener {
//            quizViewModel.moveToPrev()
//            updateQuestion()
//        }

        binding.cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }

        updateQuestion()

    }

//    override fun onStart() {
//        super.onStart()
//        Log.d(TAG, "onStart() called")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Log.d(TAG, "onResume() called")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.d(TAG, "onPause() called")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.d(TAG, "onStop() called")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d(TAG, "onDestroy() called")
//    }


    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgement_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        if (messageResId == R.string.correct_toast && !answered) {
            quizViewModel.count += 1
        }
        answered = true

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

        if (answered && !lastQ) {

            tempToast = Toast.makeText(this, R.string.move_toast, Toast.LENGTH_SHORT)
            tempToast.show()
        }
    }
    private fun finalToast() {
        val toastMessage = when {
            quizViewModel.isCheater -> R.string.cheating_score_toast
            quizViewModel.count == 0 -> R.string.score_0_toast
            quizViewModel.count == 1 -> R.string.score_1_toast
            quizViewModel.count == 2 -> R.string.score_2_toast
            quizViewModel.count == 3 -> R.string.score_3_toast
            quizViewModel.count == 4 -> R.string.score_4_toast
            quizViewModel.count == 5 -> R.string.score_5_toast
            else -> R.string.score_6_toast
        }

        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()

    }
}

