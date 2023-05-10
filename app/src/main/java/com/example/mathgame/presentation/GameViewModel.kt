package com.example.mathgame.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mathgame.R
import com.example.mathgame.data.GameRepositoryImpl
import com.example.mathgame.domain.entity.GameResult
import com.example.mathgame.domain.entity.GameSettings
import com.example.mathgame.domain.entity.Level
import com.example.mathgame.domain.entity.Question
import com.example.mathgame.domain.usecases.GenerateQuestionsUseCase
import com.example.mathgame.domain.usecases.GetGameSettingsUseCase

class GameViewModel(
    private val application: Application,
    private val level: Level
) : ViewModel() {
    //Параметры игры
    private lateinit var settings: GameSettings

    //Репозиторий
    private val repository = GameRepositoryImpl

    //UseCases
    private val getGamesSettingsUseCase = GetGameSettingsUseCase(repository)
    private val generateQuestionsUseCase = GenerateQuestionsUseCase(repository)

    //Таймер как строка
    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    //Вопрос
    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    //Процент правильных ответов
    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    //Прогресс
    private val _progressAnswers = MutableLiveData<String>()
    val processAnswers: LiveData<String>
        get() = _progressAnswers

    //Достаточно ли правильных ответов для победы
    private val _enoughCountOfRightAnswers = MutableLiveData<Boolean>()
    val enoughCountOfRightAnswers: LiveData<Boolean>
        get() = _enoughCountOfRightAnswers

    //Достаточно ли ответов в целом для победы
    private val _enoughPercentOfRightAnswers = MutableLiveData<Boolean>()
    val enoughPercentOfRightAnswers: LiveData<Boolean>
        get() = _enoughPercentOfRightAnswers

    //Минимальный процент отвеченых вопросов
    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    //Результат игры
    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    //Таймер
    private var timer: CountDownTimer? = null

    //Количество правильных ответов
    private var countOfRightAnswers = 0

    //Количество вопросов
    private var countOfQuestions = 0

    init {
        startGame()
    }

    private fun startGame() {
        getGameSettings()
        startTimer()
        generateQuestion()
        upgradeProgress()
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        upgradeProgress()
        generateQuestion()
    }

    private fun upgradeProgress() {
        val percent = calculatePercentOrRightAnswers()
        _percentOfRightAnswers.value = percent
        _progressAnswers.value = String.format(
            application.resources.getString(R.string.progress_answers),
            countOfRightAnswers,
            settings.minCountOfRightAnswers
        )
        _enoughCountOfRightAnswers.value = countOfRightAnswers >= settings.minCountOfRightAnswers
        _enoughPercentOfRightAnswers.value = percent >= settings.minPercentOfRightAnswers
    }

    private fun calculatePercentOrRightAnswers(): Int {
        if (countOfQuestions == 0) {
            return 0
        }
        return (countOfRightAnswers / countOfQuestions.toDouble() * 100).toInt()
    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestions++
    }

    private fun generateQuestion() {
        _question.value = generateQuestionsUseCase(settings.maxSumValue)
    }

    private fun getGameSettings() {
        this.settings = getGamesSettingsUseCase(level)
        _minPercent.value = settings.minPercentOfRightAnswers
    }

    private fun formatTime(millsUntilFinished: Long): String {
        val seconds = millsUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            winner = enoughCountOfRightAnswers.value == true && enoughPercentOfRightAnswers.value == true,
            countOfRightAnswers = countOfRightAnswers,
            countOfQuestion = countOfQuestions,
            gameSettings = settings
        )
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            settings.gameTimeInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millsUntilFinished: Long) {
                _formattedTime.value = formatTime(millsUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }

        }
        timer?.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()

    }

    companion object {
        const val MILLIS_IN_SECONDS = 1000L
        const val SECONDS_IN_MINUTES = 60
    }
}