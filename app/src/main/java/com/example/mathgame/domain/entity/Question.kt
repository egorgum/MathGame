package com.example.mathgame.domain.entity
//Сумма, видимое число,варианты ответов и правильный ответ
data class Question(
    val sum: Int,
    val visibleNumber: Int,
    val options: List<Int>
){
    val rightAnswer: Int
        get() = sum - visibleNumber
}