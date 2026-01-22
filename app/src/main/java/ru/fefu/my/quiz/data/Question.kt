package ru.fefu.my.quiz.data

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

object QuizRepository {
    val questions = listOf(
        Question(
            text = "Что сказал Терминатор перед тем, как опуститься в расплавленный металл?",
            options = listOf("I'll be back", "Hasta la vista, baby", "Мне нужна твоя одежда", "Палец вверх (молча)"),
            correctAnswerIndex = 3
        ),
        Question(
            text = "Какого цвета была таблетка, которую выбрал Нео в «Матрице»?",
            options = listOf("Синяя", "Красная", "Зеленая", "Аскорбинка"),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Первое правило Бойцовского клуба?",
            options = listOf("Всегда носить рубашку", "Никому не рассказывать о Бойцовском клубе", "Приводить друзей", "Не бить по лицу"),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Кто на самом деле был отцом Люка Скайуокера?",
            options = listOf("Оби-Ван Кеноби", "Йода", "Дарт Вейдер", "Палпатин"),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Что нужно было сделать, чтобы выжить в фильме «Тихое место»?",
            options = listOf("Не спать", "Не говорить и не шуметь", "Не смотреть в глаза", "Не выходить из комнаты"),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Как звали домашнего эльфа в «Гарри Поттере», который любил носки?",
            options = listOf("Голлум", "Добби", "Йода", "Кикимер"),
            correctAnswerIndex = 1
        ),
        Question(
            text = "В «Титанике» места на двери хватило бы...",
            options = listOf("Только Розе", "Двоим, если бы Роза подвинулась", "Никому", "Коту"),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Кто щелкнул пальцами и уничтожил половину Вселенной?",
            options = listOf("Железный человек", "Танос", "Тор", "Доктор Стрэндж"),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Какое животное является символом факультета Гриффиндор?",
            options = listOf("Змея", "Барсук", "Лев", "Орел"),
            correctAnswerIndex = 2
        ),
        Question(
            text = "В фильме «Начало» (Inception) герои использовали этот предмет, чтобы проверить реальность:",
            options = listOf("Волчок", "Монетку", "Карты", "Телефон"),
            correctAnswerIndex = 0
        )
    )
}