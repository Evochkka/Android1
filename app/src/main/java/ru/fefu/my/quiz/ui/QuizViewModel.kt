package ru.fefu.my.quiz.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.fefu.my.quiz.data.Question
import ru.fefu.my.quiz.data.QuizRepository

enum class QuizScreenState {
    WELCOME, QUESTION, RESULT
}

data class QuizUiState(
    val screenState: QuizScreenState = QuizScreenState.WELCOME,
    val currentQuestionIndex: Int = 0,
    val score: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerConfirmed: Boolean = false,
    val totalQuestions: Int = 0,
    val currentQuestion: Question? = null
)

class QuizViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val questions = QuizRepository.questions

    init {
        _uiState.update { it.copy(totalQuestions = questions.size) }
    }

    fun startQuiz() {
        if (questions.isEmpty()) return
        _uiState.update {
            it.copy(
                screenState = QuizScreenState.QUESTION,
                currentQuestionIndex = 0,
                score = 0,
                selectedOptionIndex = null,
                isAnswerConfirmed = false,
                currentQuestion = questions[0]
            )
        }
    }

    fun selectAnswer(index: Int) {
        if (_uiState.value.isAnswerConfirmed) return
        _uiState.update { it.copy(selectedOptionIndex = index) }
    }

    fun onNextPressed() {
        val currentState = _uiState.value
        
        if (!currentState.isAnswerConfirmed) {
            if (currentState.selectedOptionIndex == null) return

            val isCorrect = currentState.selectedOptionIndex == currentState.currentQuestion?.correctAnswerIndex
            
            _uiState.update {
                it.copy(
                    isAnswerConfirmed = true,
                    score = if (isCorrect) it.score + 1 else it.score
                )
            }
        } else {
            if (currentState.currentQuestionIndex < questions.size - 1) {
                val nextIndex = currentState.currentQuestionIndex + 1
                _uiState.update {
                    it.copy(
                        currentQuestionIndex = nextIndex,
                        currentQuestion = questions[nextIndex],
                        selectedOptionIndex = null,
                        isAnswerConfirmed = false
                    )
                }
            } else {
                _uiState.update { it.copy(screenState = QuizScreenState.RESULT) }
            }
        }
    }

    fun restartQuiz() {
        startQuiz()
    }
}
