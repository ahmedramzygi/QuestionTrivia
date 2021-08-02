/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    data class Question(
            val text: String,
            val answers: List<String>)

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (or better yet, not define the questions in code...)
    private val questions: MutableList<Question> = mutableListOf(
            Question(text = "Who is the president of Egypt?",
                    answers = listOf("Elsisi", "Morsi", "Mobarak", "Gamal Gimi")),
            Question(text = "Who is the skillfull player in the History of Egypt?",
                    answers = listOf("Shikabala", "Wael Gomaa", "Elhadary", "ElWensh")),
            Question(text = "What is the most important factor to have a good shape?",
                    answers = listOf("Diet", "Workout", "Sleep", "Supplements")),
            Question(text = "Which is the European club of the century(1900-2000) ?",
                    answers = listOf("Real Madrid", "Porto", "Barcelona", "Bayern Munschen")),
            Question(text = "What is the current population of Egypt?",
                    answers = listOf("104 Million", "90 Million", "102Million", "35 Million")),
            Question(text = "What is the Capital of Egypt?",
                    answers = listOf("Cairo", "Alexandria", "Suez", "Aswan")),
            Question(text = "What is the ranking of the Egyption Army in 2020 ?",
                    answers = listOf("9th", "10th", "3rd", "7th")),
            Question(text = "Who is the actor that performed the Godfather charachter in the first movie of GodFather's triology?",
                    answers = listOf("Marlon Brando", "AlPachino", "Robert De Niro", "Joe Peschi")),
            Question(text = "Who did the Rocky Balboa fictional charchter?",
                    answers = listOf("Sylvester Stallone", "Michael B. Jordan", "Carl Wealth", "Van Damme")),
            Question(text = "Which country has won World Cup the most?",
                    answers = listOf("Brazil", "France", "Germany", "Egypt"))
    )

    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private var questionsWrong=0
//    private val numQuestions = ((questions.size + 1) / 2).coerceAtMost(3)
private val numQuestions = ((questions.size + 1) / 2).coerceAtMost(3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
                inflater, R.layout.fragment_game, container, false)

        // Shuffles the questions and sets the question index to the first question.
        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.game = this

        // Set the onClickListener for the submitButton
        binding.submitButton.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.
                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    questionIndex++

                    // Advance to the next question
                    if (questionIndex < numQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameWonFragment(numQuestions,questionIndex))
                    }
                } else {
                    questionsWrong++
                    view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment2(numQuestions,questionsWrong))
                }
            }
        }
        return binding.root
    }

    // randomize the questions and set the first question
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        // and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions)
    }

}
