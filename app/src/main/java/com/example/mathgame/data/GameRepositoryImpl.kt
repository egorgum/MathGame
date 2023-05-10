package com.example.mathgame.data

import com.example.mathgame.domain.entity.GameSettings
import com.example.mathgame.domain.entity.Level
import com.example.mathgame.domain.entity.Question
import com.example.mathgame.domain.repository.GameRepository
import java.lang.Integer.min
import kotlin.math.max
import kotlin.random.Random

object GameRepositoryImpl: GameRepository {

    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1

    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        //Вычисление суммы
        val sum = Random.nextInt(MIN_SUM_VALUE,maxSumValue + 1)
        //Вычисление видимого числа
        val visibleNumber = Random.nextInt(MIN_ANSWER_VALUE, sum)
        //Установка вариантов ответа
        val options = HashSet<Int>()
        val rightAnswer = sum - visibleNumber//Правильный ответ
        options.add(rightAnswer)
        val from = max(rightAnswer - countOfOptions, MIN_ANSWER_VALUE)
        val to = min(maxSumValue,rightAnswer + countOfOptions)
        while (options.size < countOfOptions){
            options.add(Random.nextInt(from, to))
        }
        return Question(sum,visibleNumber,options.toList())
    }
    //Параметры для уровней сложности
    override fun getGameSettings(level: Level): GameSettings {
        return when(level){
            Level.TEST -> {
                GameSettings(
                    10,
                    3,
                    50,
                    8
                )
            }
            Level.EASY -> {
                GameSettings(
                    10,
                    10,
                    70,
                    60
                )
            }
            Level.NORMAL-> {
                GameSettings(
                    20,
                    20,
                    80,
                    40
                )
            }
            Level.HARD -> {
                GameSettings(
                    30,
                    30,
                    90,
                    40
                )
            }
        }
    }
}