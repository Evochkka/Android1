package ru.fefu.my.quiz.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun QuizApp(viewModel: QuizViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (uiState.screenState) {
                QuizScreenState.WELCOME -> WelcomeScreen(
                    onStartClick = viewModel::startQuiz
                )
                QuizScreenState.QUESTION -> QuestionScreen(
                    uiState = uiState,
                    onOptionSelected = viewModel::selectAnswer,
                    onNextClick = viewModel::onNextPressed
                )
                QuizScreenState.RESULT -> ResultScreen(
                    score = uiState.score,
                    total = uiState.totalQuestions,
                    onRestartClick = viewModel::restartQuiz
                )
            }
        }
    }
}

@Composable
fun WelcomeScreen(onStartClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Movie Quiz",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.width(60.dp).height(4.dp)
        ) {}
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Тест на знание культовых сцен, персонажей и цитат из мира кино.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(56.dp))
        
        Button(
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Начать",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun QuestionScreen(
    uiState: QuizUiState,
    onOptionSelected: (Int) -> Unit,
    onNextClick: () -> Unit
) {
    val question = uiState.currentQuestion ?: return
    val progress = (uiState.currentQuestionIndex + 1).toFloat() / uiState.totalQuestions

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Вопрос ${uiState.currentQuestionIndex + 1} из ${uiState.totalQuestions}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.outline
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
        
        Spacer(modifier = Modifier.height(40.dp))
        
        Text(
            text = question.text,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            lineHeight = androidx.compose.ui.unit.TextUnit.Unspecified
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        question.options.forEachIndexed { index, optionText ->
            OptionItem(
                text = optionText,
                index = index,
                isSelected = uiState.selectedOptionIndex == index,
                isConfirmed = uiState.isAnswerConfirmed,
                isCorrect = index == question.correctAnswerIndex,
                onOptionSelected = { onOptionSelected(index) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNextClick,
            enabled = uiState.selectedOptionIndex != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (uiState.isAnswerConfirmed) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                text = if (uiState.isAnswerConfirmed) "Продолжить" else "Подтвердить",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun OptionItem(
    text: String,
    index: Int,
    isSelected: Boolean,
    isConfirmed: Boolean,
    isCorrect: Boolean,
    onOptionSelected: () -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = when {
            isConfirmed && isCorrect -> Color(0xFFE8F5E9)
            isConfirmed && isSelected && !isCorrect -> Color(0xFFFFEBEE)
            isSelected -> MaterialTheme.colorScheme.surfaceVariant
            else -> MaterialTheme.colorScheme.surface
        },
        label = "color"
    )

    val borderColor = when {
        isConfirmed && isCorrect -> Color(0xFF4CAF50)
        isConfirmed && isSelected && !isCorrect -> Color(0xFFD32F2F)
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    }

    Surface(
        onClick = onOptionSelected,
        enabled = !isConfirmed,
        shape = RoundedCornerShape(12.dp),
        color = containerColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = null
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun ResultScreen(
    score: Int,
    total: Int,
    onRestartClick: () -> Unit
) {
    val percentage = if (total > 0) (score.toFloat() / total) * 100 else 0f
    
    val comment = when {
        percentage < 50 -> "Стоит пересмотреть классику."
        percentage < 80 -> "Хороший результат."
        else -> "Отличные знания."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Результат",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = "$score / $total",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = "правильных ответов",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.outline
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = comment,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(64.dp))
        
        OutlinedButton(
            onClick = onRestartClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Пройти еще раз",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}