package com.example.mathgame.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
//Результат игры
data class GameResult(
    val winner: Boolean,
    val countOfRightAnswers: Int,
    val countOfQuestion: Int,
    val gameSettings: GameSettings
) : Parcelable