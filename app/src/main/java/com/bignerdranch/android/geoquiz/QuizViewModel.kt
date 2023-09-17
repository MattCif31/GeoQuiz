package com.bignerdranch.android.geoquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
const val COUNT = "COUNT"
var endOfBank = false


class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)) //question bank

    var count: Int
        get() = savedStateHandle.get(COUNT) ?: 0
        set(value) = savedStateHandle.set(COUNT, count+1)


    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)


    private var currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {

        currentIndex = (currentIndex+1)// % questionBank.size
        if (currentIndex == questionBank.size-1) {
            endOfBank = true
        }

    }






//    fun moveToPrev() {
//        if (currentIndex == 0) {
//            currentIndex = questionBank.size-1
//        }
//        else {
//            currentIndex = (currentIndex - 1) % questionBank.size
//        }
//    }


}